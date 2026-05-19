package com.kizunavi.service;

import com.kizunavi.config.AuthProperties;
import com.kizunavi.dto.FirstLoginRequest;
import com.kizunavi.dto.ResetPasswordRequest;
import com.kizunavi.dto.Role;
import com.kizunavi.dto.SimpleStatusResponse;
import com.kizunavi.entity.PasswordResetToken;
import com.kizunavi.entity.User;
import com.kizunavi.auth.FirstLoginPasswordSupport;
import com.kizunavi.exception.BadRequestException;
import com.kizunavi.exception.InvalidTokenException;
import com.kizunavi.repository.LoginAttemptRepository;
import com.kizunavi.repository.PasswordResetTokenRepository;
import com.kizunavi.repository.RefreshTokenRepository;
import com.kizunavi.repository.UserRepository;
import com.kizunavi.security.JwtTokenProvider;
import com.kizunavi.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    @DisplayName("resetPassword: 無効トークンは InvalidTokenException")
    void resetPasswordThrowsWhenTokenInvalid() {
        when(passwordResetTokenRepository.findByTokenHashAndUsedAtIsNull(anyString()))
            .thenReturn(Optional.empty());

        ResetPasswordRequest request = new ResetPasswordRequest("bad-token", "NewPass456");

        assertThatThrownBy(() -> authService.resetPassword(request))
            .isInstanceOf(InvalidTokenException.class);
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
            .lastPasswordChangedAt(FirstLoginPasswordSupport.pendingPasswordChangeTimestamp())
            .build();
    }
}
