package com.product.template.service;

import com.product.template.config.JwtConfig;
import com.product.template.dto.*;
import com.product.template.entity.LoginAttempt;
import com.product.template.entity.RefreshToken;
import com.product.template.entity.User;
import com.product.template.exception.DuplicateResourceException;
import com.product.template.exception.InvalidTokenException;
import com.product.template.repository.LoginAttemptRepository;
import com.product.template.repository.RefreshTokenRepository;
import com.product.template.repository.UserRepository;
import com.product.template.security.JwtTokenProvider;
import com.product.template.util.TokenHashUtil;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * ログイン・新規登録・トークン更新・ログアウトなど認証ドメインのユースケースを提供する。
 * 
 * <p>認証失敗時は {@code login_attempts} に監査ログとして記録する。</p>
 * <p>失敗を含むログイン試行はすべて {@code login_attempts} に監査ログとして記録する。</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    /** Spring Security の認証マネージャ（メール・パスワード検証に使用）。 */
    private final AuthenticationManager authenticationManager;
    /** ユーザーの永続化および照会。 */
    private final UserRepository userRepository;
    /** リフレッシュトークン（ハッシュ）の永続化。 */
    private final RefreshTokenRepository refreshTokenRepository;
    /** ログイン試行の監査ログ永続化。 */
    private final LoginAttemptRepository loginAttemptRepository;
    /** サインアップ時のパスワードハッシュ化に使用。 */
    private final PasswordEncoder passwordEncoder;
    /** アクセス／リフレッシュトークンの生成・検証。 */
    private final JwtTokenProvider jwtTokenProvider;
    /** トークン有効期限など JWT 関連設定。 */
    private final JwtConfig jwtConfig;

    /**
     * メールアドレスとパスワードで認証し、アクセス／リフレッシュトークンを発行する。
     *
     * <p>発行したリフレッシュトークンは平文を保存せず、SHA-256 ハッシュを {@code refresh_tokens} に保存する。
     * 認証の成否にかかわらず {@code login_attempts} に監査ログを記録する。
     * {@link AuthenticationException} 発生時もロールバックしないことで失敗ログを確実に永続化する。</p>
     *
     * @param request    ログインリクエスト（メール・パスワード）
     * @param ipAddress  クライアントの IP アドレス（監査ログ・リフレッシュトークン記録用）
     * @param userAgent  クライアントの User-Agent（リフレッシュトークン記録用、取得できない場合は {@code null}）
     * @return アクセス・リフレッシュトークンおよび有効期限（秒）を含むレスポンス
     * @throws AuthenticationException 認証失敗（不正な資格情報・アカウント無効 等）
     */
    @Transactional(noRollbackFor = AuthenticationException.class)
    public TokenResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String email = request.getEmail();
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );
        } catch (AuthenticationException ex) {
            saveLoginAttempt(email, ipAddress, false, resolveFailureReason(ex), null);
            throw ex;
        }

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        saveLoginAttempt(email, ipAddress, true, null, user);
        saveRefreshTokenRecord(user, refreshToken, ipAddress, userAgent);

        return new TokenResponse()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtConfig.getAccessTokenExpiration() / 1000);
    }

    /**
     * 新規ユーザーを登録する。
     *
     * @param request サインアップ情報（メール・パスワード・名前）
     * @return 作成されたユーザーの公開情報
     * @throws DuplicateResourceException 同一メールが既に登録されている場合
     */
    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("このメールアドレスは既に登録されています");
        }

        User user = User.builder()
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .role(Role.ROLE_USER)
            .enabled(true)
            .build();

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getEmail());

        return toUserResponse(savedUser);
    }

    /**
     * リフレッシュトークンを検証し、新しいアクセス／リフレッシュトークンを発行する。
     *
     * <p>ローテーション後のリフレッシュトークンは DB に保存される。</p>
     *
     * @param refreshToken クライアントから渡されたリフレッシュトークン
     * @param ipAddress    クライアントの IP アドレス（新トークン記録用）
     * @param userAgent    クライアントの User-Agent（新トークン記録用、取得できない場合は {@code null}）
     * @return 新しいトークンペアおよび有効期限（秒）
     * @throws InvalidTokenException トークンが欠落・無効・不一致の場合
     */
    @Transactional
    public TokenResponse refreshToken(String refreshToken, String ipAddress, String userAgent) {
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

        current.setRevokedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        refreshTokenRepository.save(current);

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        saveRefreshTokenRecord(user, newRefreshToken, ipAddress, userAgent);

        return new TokenResponse()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtConfig.getAccessTokenExpiration() / 1000);
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
     * ログイン試行を {@code login_attempts} テーブルに記録する。
     *
     * @param email         試行されたメールアドレス
     * @param ipAddress     クライアント IP（取得できない場合は {@code null}）
     * @param succeeded     認証成功なら {@code true}
     * @param failureReason 失敗理由コード（成功時は {@code null}）
     * @param user          認証済みユーザー（未存在や失敗時は {@code null}）
     */
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

    /**
     * {@link AuthenticationException} のサブクラスから失敗理由コード（最大 30 文字）を導出する。
     *
     * @param ex 認証例外
     * @return {@code login_attempts.failure_reason} に格納する文字列
     */
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
     * @param user 変換元ユーザー（パスワードは含めない）
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
