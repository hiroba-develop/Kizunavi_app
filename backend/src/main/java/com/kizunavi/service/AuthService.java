package com.kizunavi.service;

import com.kizunavi.auth.FirstLoginPasswordSupport;
import com.kizunavi.auth.TenantLoginSupport;
import com.kizunavi.config.AuthProperties;
import com.kizunavi.config.JwtConfig;
import com.kizunavi.dto.*;
import com.kizunavi.exception.BadRequestException;
import com.kizunavi.entity.LoginAttempt;
import com.kizunavi.entity.PasswordResetToken;
import com.kizunavi.entity.RefreshToken;
import com.kizunavi.entity.User;
import com.kizunavi.exception.InvalidTokenException;
import com.kizunavi.repository.LoginAttemptRepository;
import com.kizunavi.repository.PasswordResetTokenRepository;
import com.kizunavi.repository.RefreshTokenRepository;
import com.kizunavi.repository.UserRepository;
import com.kizunavi.security.JwtTokenProvider;
import com.kizunavi.util.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Optional;

/**
 * ログイン・トークン更新・ログアウト・パスワード管理など認証ドメインのユースケースを提供する。
 *
 * <p>失敗を含むログイン試行はすべて {@code login_attempts} に監査ログとして記録する。</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final int PASSWORD_RESET_TOKEN_BYTES = 32;
    private static final int PASSWORD_RESET_VALID_HOURS = 24;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;
    private final AuthProperties authProperties;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * メールアドレスとパスワードで認証し、アクセス／リフレッシュトークンを発行する。
     *
     * @param request   ログインリクエスト（メール・パスワード）
     * @param ipAddress クライアントの IP アドレス
     * @param userAgent クライアントの User-Agent（取得できない場合は {@code null}）
     * @return アクセストークン・リフレッシュトークン・表示名
     * @throws AuthenticationException 認証失敗
     */
    @Transactional(noRollbackFor = AuthenticationException.class)
    public AuthLoginResult login(LoginRequest request, String ipAddress, String userAgent) {
        String email = request.getEmail();
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );
        } catch (AuthenticationException ex) {
            if (ex instanceof BadCredentialsException) {
                recordFailedLogin(email);
            }
            saveLoginAttempt(email, ipAddress, false, resolveFailureReason(ex), null);
            throw ex;
        }

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        clearLoginLockState(user);
        user.setLastLoginAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        userRepository.save(user);

        saveLoginAttempt(email, ipAddress, true, null, user);
        saveRefreshTokenRecord(user, refreshToken, ipAddress, userAgent);

        return new AuthLoginResult(accessToken, refreshToken, user.getName());
    }

    /**
     * リフレッシュトークンを検証し、新しいアクセス／リフレッシュトークンを発行する。
     *
     * @param refreshToken クライアントから渡されたリフレッシュトークン
     * @param ipAddress    クライアントの IP アドレス
     * @param userAgent    クライアントの User-Agent
     * @return 新しいトークンペア
     * @throws InvalidTokenException トークンが欠落・無効・不一致の場合
     */
    @Transactional
    public AuthRefreshResult refreshToken(String refreshToken, String ipAddress, String userAgent) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidTokenException("リフレッシュトークンが提供されていません");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("無効なリフレッシュトークンです");
        }

        String tokenHash = TokenHashUtil.sha256Hex(refreshToken);
        RefreshToken current = refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
            .orElseThrow(() -> new InvalidTokenException("リフレッシュトークンが見つかりません"));

        if (current.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("リフレッシュトークンの有効期限が切れています");
        }

        User user = current.getUser();
        String username = jwtTokenProvider.extractUsername(refreshToken);
        if (!username.equals(user.getEmail())) {
            throw new InvalidTokenException("トークンが一致しません");
        }

        User userWithTenant = userRepository.findByEmailWithEmployeeAndCustomer(user.getEmail())
            .orElseThrow(() -> new InvalidTokenException("ユーザーが見つかりません"));
        if (!TenantLoginSupport.isLoginAllowed(userWithTenant)) {
            throw new InvalidTokenException("アカウントは利用できません");
        }

        current.setRevokedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        refreshTokenRepository.save(current);

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        saveRefreshTokenRecord(user, newRefreshToken, ipAddress, userAgent);

        return new AuthRefreshResult(newAccessToken, newRefreshToken);
    }

    /**
     * 指定メールのユーザーのリフレッシュトークンを無効化する。
     *
     * @param email ログアウト対象ユーザーのメールアドレス
     */
    @Transactional
    public void logout(String email) {
        userRepository.findByEmail(email).ifPresent(user ->
            refreshTokenRepository.revokeAllByUser(user, LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
        );
        log.info("User logged out: {}", email);
    }

    /**
     * 仮パスワードを検証し、新しいパスワードを設定する。
     *
     * @param request メール・仮パスワード・新パスワード
     * @return 更新成功レスポンス
     * @throws InvalidTokenException ユーザー不存在または仮パスワード不一致
     */
    @Transactional
    public SimpleStatusResponse firstLogin(FirstLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidTokenException("メールアドレスまたは仮パスワードが正しくありません"));

        if (!FirstLoginPasswordSupport.requiresFirstLoginPasswordChange(user)) {
            throw new BadRequestException(
                "初回パスワード設定の対象ではありません。パスワードをお忘れの場合はパスワード再設定をご利用ください。");
        }

        if (!passwordEncoder.matches(request.getTempPassword(), user.getPasswordHash())) {
            throw new InvalidTokenException("メールアドレスまたは仮パスワードが正しくありません");
        }

        if (request.getTempPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("新しいパスワードは仮パスワードと異なるものを設定してください");
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setLastPasswordChangedAt(now);
        clearLoginLockState(user);
        userRepository.save(user);
        refreshTokenRepository.revokeAllByUser(user, now);

        log.info("First login password set for user: {}", user.getEmail());
        return new SimpleStatusResponse("success");
    }

    /**
     * パスワードリセット用メールを送信する。
     *
     * @param request   リセット対象メールアドレス
     * @param ipAddress リクエスト元 IP
     * @return 常に {@code success}
     */
    @Transactional
    public SimpleStatusResponse forgotPassword(ForgotPasswordRequest request, String ipAddress) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> issuePasswordResetToken(user, ipAddress));
        return new SimpleStatusResponse("success");
    }

    /**
     * パスワードリセットトークンの有効性を検証する。
     *
     * @param rawToken メールリンクに含まれる平文トークン
     * @return 検証結果
     */
    @Transactional(readOnly = true)
    public PasswordResetVerifyResponse verifyResetToken(String rawToken) {
        Optional<PasswordResetToken> token = findActiveResetToken(rawToken);
        if (token.isPresent()) {
            return new PasswordResetVerifyResponse()
                .responseStatus("success")
                .valid(true);
        }
        return new PasswordResetVerifyResponse()
            .responseStatus("success")
            .valid(false)
            .message(resolveInvalidReason(rawToken));
    }

    /**
     * トークンを検証し、新しいパスワードを設定する。
     *
     * @param request 平文トークンと新パスワード
     * @return 更新成功レスポンス
     * @throws InvalidTokenException トークンが無効・期限切れ・使用済みの場合
     */
    @Transactional
    public SimpleStatusResponse resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = findActiveResetToken(request.getToken())
            .orElseThrow(() -> new InvalidTokenException("無効または期限切れのトークンです"));

        User user = token.getUser();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setLastPasswordChangedAt(now);
        userRepository.save(user);

        token.setUsedAt(now);
        passwordResetTokenRepository.save(token);
        passwordResetTokenRepository.invalidateUnusedByUser(user, now);
        refreshTokenRepository.revokeAllByUser(user, now);

        log.info("Password reset completed for user: {}", user.getEmail());
        return new SimpleStatusResponse("success");
    }

    private void recordFailedLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            int current = user.getFailedLoginCount() != null ? user.getFailedLoginCount() : 0;
            int next = current + 1;
            user.setFailedLoginCount(next);
            if (next >= authProperties.getMaxFailedLoginAttempts()) {
                user.setLockedUntil(
                    LocalDateTime.now()
                        .plusMinutes(authProperties.getLockDurationMinutes())
                        .truncatedTo(ChronoUnit.MICROS));
                log.warn("Account locked for email: {}", email);
            }
            userRepository.save(user);
        });
    }

    private void clearLoginLockState(User user) {
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
    }

    private void issuePasswordResetToken(User user, String ipAddress) {
        String rawToken = generateResetToken();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        LocalDateTime expiresAt = now.plusHours(PASSWORD_RESET_VALID_HOURS);

        passwordResetTokenRepository.invalidateUnusedByUser(user, now);
        passwordResetTokenRepository.save(
            PasswordResetToken.builder()
                .user(user)
                .tokenHash(TokenHashUtil.sha256Hex(rawToken))
                .expiresAt(expiresAt)
                .createdAt(now)
                .requesterIp(ipAddress)
                .build());

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), rawToken);
            log.info("Password reset email sent to: {}", user.getEmail());
        } catch (RuntimeException ex) {
            log.error("Password reset email failed for: {}", user.getEmail(), ex);
        }
    }

    private Optional<PasswordResetToken> findActiveResetToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return Optional.empty();
        }
        return passwordResetTokenRepository
            .findByTokenHashAndUsedAtIsNull(TokenHashUtil.sha256Hex(rawToken))
            .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    private String resolveInvalidReason(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return "トークンが指定されていません";
        }
        return passwordResetTokenRepository
            .findByTokenHashAndUsedAtIsNull(TokenHashUtil.sha256Hex(rawToken))
            .map(
                token -> {
                    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                        return "期限切れ";
                    }
                    return "使用済み";
                })
            .orElse("無効なトークン");
    }

    private String generateResetToken() {
        byte[] bytes = new byte[PASSWORD_RESET_TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private void saveLoginAttempt(
            String email, String ipAddress, boolean succeeded, String failureReason, User user) {
        loginAttemptRepository.save(LoginAttempt.builder()
            .email(email)
            .ipAddress(ipAddress)
            .succeeded(succeeded)
            .failureReason(failureReason)
            .user(user)
            .attemptedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
            .build());
    }

    private String resolveFailureReason(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) return "BAD_CREDENTIALS";
        if (ex instanceof DisabledException)       return "ACCOUNT_DISABLED";
        if (ex instanceof LockedException)         return "ACCOUNT_LOCKED";
        return "AUTH_ERROR";
    }

    private void saveRefreshTokenRecord(
            User user, String rawRefreshToken, String ipAddress, String userAgent) {
        String hash = TokenHashUtil.sha256Hex(rawRefreshToken);
        long refreshMs = jwtConfig.getRefreshTokenExpiration();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        LocalDateTime exp = now.plus(refreshMs, ChronoUnit.MILLIS);
        refreshTokenRepository.save(RefreshToken.builder()
            .user(user)
            .tokenHash(hash)
            .issuedAt(now)
            .expiresAt(exp)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build());
    }

    /**
     * 永続化エンティティを API 用の {@link UserResponse} に変換する。
     *
     * @param user 変換元ユーザー
     * @return OpenAPI 生成モデル互換のユーザー応答
     */
    public static UserResponse toUserResponse(User user) {
        return new UserResponse()
            .id(user.getUserId())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt());
    }
}
