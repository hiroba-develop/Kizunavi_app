package com.kizunavi.service;

import com.kizunavi.auth.FirstLoginPasswordSupport;
import com.kizunavi.config.AuthProperties;
import com.kizunavi.config.JwtConfig;
import com.kizunavi.dto.FirstLoginRequest;
import com.kizunavi.dto.ForgotPasswordRequest;
import com.kizunavi.dto.LoginRequest;
import com.kizunavi.dto.ResetPasswordRequest;
import com.kizunavi.dto.Role;
import com.kizunavi.dto.SimpleStatusResponse;
import com.kizunavi.dto.UserResponse;
import com.kizunavi.entity.Customer;
import com.kizunavi.entity.Employee;
import com.kizunavi.entity.LoginAttempt;
import com.kizunavi.entity.PasswordResetToken;
import com.kizunavi.entity.RefreshToken;
import com.kizunavi.entity.User;
import com.kizunavi.exception.BadRequestException;
import com.kizunavi.exception.InvalidTokenException;
import com.kizunavi.repository.LoginAttemptRepository;
import com.kizunavi.repository.PasswordResetTokenRepository;
import com.kizunavi.repository.RefreshTokenRepository;
import com.kizunavi.repository.UserRepository;
import com.kizunavi.security.JwtTokenProvider;
import com.kizunavi.util.TokenHashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("null")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private LoginAttemptRepository loginAttemptRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private AuthProperties authProperties;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        when(authProperties.getMaxFailedLoginAttempts()).thenReturn(5);
        when(authProperties.getLockDurationMinutes()).thenReturn(15);
        when(jwtConfig.getRefreshTokenExpiration()).thenReturn(604_800_000L);
    }

    @Test
    @DisplayName("login: 認証成功後にユーザーが見つからない場合は RuntimeException")
    void loginThrowsWhenUserMissingAfterAuth() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        Authentication authentication =
            new UsernamePasswordAuthenticationToken("user@example.com", "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken("user@example.com")).thenReturn("refresh");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", null))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("login: 成功時にトークンを発行しログイン試行を記録する")
    void loginSuccess() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        Authentication authentication =
            new UsernamePasswordAuthenticationToken("user@example.com", "password");
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken("user@example.com")).thenReturn("refresh");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(inv -> inv.getArgument(0));
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthLoginResult result = authService.login(request, "127.0.0.1", "JUnit");

        assertThat(result.accessToken()).isEqualTo("access");
        assertThat(result.refreshToken()).isEqualTo("refresh");
        assertThat(result.name()).isEqualTo("田中太郎");
        verify(loginAttemptRepository).save(any(LoginAttempt.class));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("login: BadCredentialsException 時に失敗回数を記録して再スローする")
    void loginThrowsBadCredentialsAndRecordsFailure() {
        LoginRequest request = new LoginRequest("user@example.com", "wrong");
        User user = baseUser();
        user.setFailedLoginCount(4);

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", null))
            .isInstanceOf(BadCredentialsException.class);

        assertThat(user.getFailedLoginCount()).isEqualTo(5);
        assertThat(user.getLockedUntil()).isNotNull();
        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    @DisplayName("login: DisabledException 時に失敗理由を記録する")
    void loginThrowsDisabled() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new DisabledException("disabled"));
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", null))
            .isInstanceOf(DisabledException.class);

        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    @DisplayName("login: LockedException 時に失敗理由を記録する")
    void loginThrowsLocked() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new LockedException("locked"));
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", null))
            .isInstanceOf(LockedException.class);
    }

    @Test
    @DisplayName("login: その他の AuthenticationException 時に失敗を記録する")
    void loginThrowsOtherAuthError() {
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any()))
            .thenThrow(
                new org.springframework.security.core.AuthenticationException("other") {});
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> authService.login(request, "127.0.0.1", null))
            .isInstanceOf(org.springframework.security.core.AuthenticationException.class);

        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    @DisplayName("refreshToken: null のとき InvalidTokenException")
    void refreshTokenThrowsWhenNull() {
        assertThatThrownBy(() -> authService.refreshToken(null, "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("提供されていません");
    }

    @Test
    @DisplayName("refreshToken: 空文字のとき InvalidTokenException")
    void refreshTokenThrowsWhenBlank() {
        assertThatThrownBy(() -> authService.refreshToken("  ", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("refreshToken: JWT 無効のとき InvalidTokenException")
    void refreshTokenThrowsWhenJwtInvalid() {
        when(jwtTokenProvider.validateToken("bad")).thenReturn(false);

        assertThatThrownBy(() -> authService.refreshToken("bad", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("無効なリフレッシュトークン");
    }

    @Test
    @DisplayName("refreshToken: DB に無いとき InvalidTokenException")
    void refreshTokenThrowsWhenNotFound() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken("refresh", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("見つかりません");
    }

    @Test
    @DisplayName("refreshToken: 期限切れのとき InvalidTokenException")
    void refreshTokenThrowsWhenExpired() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        User user = baseUser();
        RefreshToken stored =
            RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refreshToken("refresh", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("有効期限が切れ");
    }

    @Test
    @DisplayName("refreshToken: メール不一致のとき InvalidTokenException")
    void refreshTokenThrowsWhenUsernameMismatch() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        User user = baseUser();
        RefreshToken stored =
            RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.of(stored));
        when(jwtTokenProvider.extractUsername("refresh")).thenReturn("other@example.com");

        assertThatThrownBy(() -> authService.refreshToken("refresh", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("一致しません");
    }

    @Test
    @DisplayName("refreshToken: テナント付きユーザー取得に失敗したとき RuntimeException")
    void refreshTokenThrowsWhenTenantLookupMissing() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        RefreshToken stored =
            RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.of(stored));
        when(jwtTokenProvider.extractUsername("refresh")).thenReturn("user@example.com");
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken("refresh", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("ユーザーが見つかりません");
    }

    @Test
    @DisplayName("verifyResetToken: 未使用かつ有効期限内だが検証不可のとき使用済みメッセージ")
    void verifyResetTokenReturnsUsedWhenTokenValidButInactive() {
        PasswordResetToken token =
            PasswordResetToken.builder()
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.empty())
            .thenReturn(Optional.of(token));

        var response = authService.verifyResetToken("active-token");

        assertThat(response.getValid()).isFalse();
        assertThat(response.getMessage().orElse(null)).isEqualTo("使用済み");
    }

    @Test
    @DisplayName("refreshToken: テナント無効のとき InvalidTokenException")
    void refreshTokenThrowsWhenTenantNotAllowed() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        User user = baseUser();
        user.setCustomer(Customer.builder().delFlg("1").build());
        RefreshToken stored =
            RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.of(stored));
        when(jwtTokenProvider.extractUsername("refresh")).thenReturn("user@example.com");
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.refreshToken("refresh", "127.0.0.1", null))
            .isInstanceOf(InvalidTokenException.class)
            .hasMessageContaining("利用できません");
    }

    @Test
    @DisplayName("refreshToken: 成功時にトークンをローテーションする")
    void refreshTokenSuccess() {
        when(jwtTokenProvider.validateToken("refresh")).thenReturn(true);
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        RefreshToken stored =
            RefreshToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(anyString()))
            .thenReturn(Optional.of(stored));
        when(jwtTokenProvider.extractUsername("refresh")).thenReturn("user@example.com");
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken("user@example.com")).thenReturn("new-access");
        when(jwtTokenProvider.generateRefreshToken("user@example.com")).thenReturn("new-refresh");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthRefreshResult result = authService.refreshToken("refresh", "127.0.0.1", "agent");

        assertThat(result.accessToken()).isEqualTo("new-access");
        assertThat(result.refreshToken()).isEqualTo("new-refresh");
        assertThat(stored.getRevokedAt()).isNotNull();
    }

    @Test
    @DisplayName("logout: ユーザーが存在するときリフレッシュトークンを失効する")
    void logoutRevokesTokensWhenUserExists() {
        User user = baseUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        authService.logout("user@example.com");

        verify(refreshTokenRepository).revokeAllByUser(eq(user), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("logout: ユーザーが存在しないときも例外なく完了する")
    void logoutNoOpWhenUserMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        authService.logout("missing@example.com");

        verify(refreshTokenRepository, never()).revokeAllByUser(any(), any());
    }

    @Test
    @DisplayName("forgotPassword: ユーザー存在時にトークン発行とメール送信を行う")
    void forgotPasswordIssuesTokenWhenUserExists() {
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        SimpleStatusResponse response =
            authService.forgotPassword(new ForgotPasswordRequest("user@example.com"), "127.0.0.1");

        assertThat(response.getResponseStatus()).isEqualTo("success");
        verify(passwordResetTokenRepository).invalidateUnusedByUser(eq(user), any(LocalDateTime.class));
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        verify(emailService).sendPasswordResetEmail(eq("user@example.com"), anyString());
    }

    @Test
    @DisplayName("forgotPassword: ユーザー不存在でも success を返す")
    void forgotPasswordSuccessWhenUserMissing() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        SimpleStatusResponse response =
            authService.forgotPassword(new ForgotPasswordRequest("unknown@example.com"), "127.0.0.1");

        assertThat(response.getResponseStatus()).isEqualTo("success");
        verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("forgotPassword: メール送信失敗時も success を返す")
    void forgotPasswordSuccessWhenEmailFails() {
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));
        doThrow(new RuntimeException("SES down"))
            .when(emailService)
            .sendPasswordResetEmail(anyString(), anyString());

        SimpleStatusResponse response =
            authService.forgotPassword(new ForgotPasswordRequest("user@example.com"), "127.0.0.1");

        assertThat(response.getResponseStatus()).isEqualTo("success");
    }

    @Test
    @DisplayName("verifyResetToken: 無効トークンは valid=false")
    void verifyResetTokenInvalid() {
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.empty());

        var response = authService.verifyResetToken("unknown");

        assertThat(response.getValid()).isFalse();
        assertThat(response.getMessage().orElse(null)).isEqualTo("無効なトークン");
    }

    @Test
    @DisplayName("verifyResetToken: 空トークンはメッセージを返す")
    void verifyResetTokenBlank() {
        var response = authService.verifyResetToken("  ");

        assertThat(response.getValid()).isFalse();
        assertThat(response.getMessage().orElse(null)).isEqualTo("トークンが指定されていません");
    }

    @Test
    @DisplayName("verifyResetToken: 期限切れトークンは期限切れメッセージ")
    void verifyResetTokenExpired() {
        PasswordResetToken token =
            PasswordResetToken.builder()
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.of(token));

        var response = authService.verifyResetToken("expired-token");

        assertThat(response.getValid()).isFalse();
        assertThat(response.getMessage().orElse(null)).isEqualTo("期限切れ");
    }

    @Test
    @DisplayName("verifyResetToken: 有効トークンは valid=true")
    void verifyResetTokenValid() {
        PasswordResetToken token =
            PasswordResetToken.builder()
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.of(token));

        var response = authService.verifyResetToken("abc");

        assertThat(response.getValid()).isTrue();
    }

    @Test
    @DisplayName("resetPassword: 成功時にパスワード更新とトークン失効を行う")
    void resetPasswordSuccess() {
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        String rawToken = "valid-reset-token";
        PasswordResetToken token =
            PasswordResetToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(TokenHashUtil.sha256Hex(rawToken)))
            .thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NewPass456")).thenReturn("new-hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        ResetPasswordRequest request = new ResetPasswordRequest(rawToken, "NewPass456");
        SimpleStatusResponse response = authService.resetPassword(request);

        assertThat(response.getResponseStatus()).isEqualTo("success");
        assertThat(user.getPasswordHash()).isEqualTo("new-hash");
        assertThat(token.getUsedAt()).isNotNull();
        verify(passwordResetTokenRepository).invalidateUnusedByUser(eq(user), any(LocalDateTime.class));
        verify(refreshTokenRepository).revokeAllByUser(eq(user), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("resetPassword: 無効トークンは InvalidTokenException")
    void resetPasswordThrowsWhenTokenInvalid() {
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.empty());

        ResetPasswordRequest request = new ResetPasswordRequest("bad-token", "NewPass456");

        assertThatThrownBy(() -> authService.resetPassword(request))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("firstLogin: 仮パスワード一致時に新パスワードを保存する")
    void firstLoginSuccess() {
        User user = baseUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("TempPass001", "hash")).thenReturn(true);
        when(passwordEncoder.encode("NewPass456")).thenReturn("new-hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        FirstLoginRequest request =
            new FirstLoginRequest("user@example.com", "TempPass001", "NewPass456");

        SimpleStatusResponse response = authService.firstLogin(request);

        assertThat(response.getResponseStatus()).isEqualTo("success");
        assertThat(user.getPasswordHash()).isEqualTo("new-hash");
        assertThat(user.getFailedLoginCount()).isZero();
        assertThat(user.getLockedUntil()).isNull();
        verify(refreshTokenRepository).revokeAllByUser(eq(user), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("firstLogin: 初回設定済みユーザーは BadRequestException")
    void firstLoginThrowsWhenNotPendingFirstLogin() {
        User user = baseUser();
        user.setLastPasswordChangedAt(LocalDateTime.now());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("CurrentPass", "hash")).thenReturn(true);

        FirstLoginRequest request =
            new FirstLoginRequest("user@example.com", "CurrentPass", "NewPass456");

        assertThatThrownBy(() -> authService.firstLogin(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("初回パスワード設定の対象ではありません");
    }

    @Test
    @DisplayName("firstLogin: 新パスワードが仮パスワードと同一の場合は BadRequestException")
    void firstLoginThrowsWhenNewPasswordSameAsTemp() {
        User user = baseUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("TempPass001", "hash")).thenReturn(true);

        FirstLoginRequest request =
            new FirstLoginRequest("user@example.com", "TempPass001", "TempPass001");

        assertThatThrownBy(() -> authService.firstLogin(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("仮パスワードと異なる");
    }

    @Test
    @DisplayName("firstLogin: 仮パスワード不一致時は InvalidTokenException")
    void firstLoginThrowsWhenTempPasswordMismatch() {
        User user = baseUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        FirstLoginRequest request =
            new FirstLoginRequest("user@example.com", "wrong", "NewPass456");

        assertThatThrownBy(() -> authService.firstLogin(request))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("firstLogin: ユーザー不存在時は InvalidTokenException")
    void firstLoginThrowsWhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        FirstLoginRequest request =
            new FirstLoginRequest("missing@example.com", "TempPass001", "NewPass456");

        assertThatThrownBy(() -> authService.firstLogin(request))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("toUserResponse: エンティティを UserResponse にマッピングする")
    void toUserResponseMapsFields() {
        LocalDateTime now = LocalDateTime.now();
        User user =
            User.builder()
                .userId(10L)
                .email("user@example.com")
                .name("田中")
                .role(Role.ROLE_ADMIN)
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserResponse response = AuthService.toUserResponse(user);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo("user@example.com");
        assertThat(response.getName()).isEqualTo("田中");
        assertThat(response.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    private static User baseUser() {
        return User.builder()
            .userId(1L)
            .email("user@example.com")
            .passwordHash("hash")
            .name("田中太郎")
            .role(Role.ROLE_USER)
            .enabled(true)
            .failedLoginCount(0)
            .employee(Employee.builder().delFlg("0").build())
            .lastPasswordChangedAt(FirstLoginPasswordSupport.pendingPasswordChangeTimestamp())
            .build();
    }
}
