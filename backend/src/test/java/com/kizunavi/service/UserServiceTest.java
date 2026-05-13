package com.kizunavi.service;

import com.kizunavi.dto.Role;
import com.kizunavi.dto.UpdateUserRequest;
import com.kizunavi.dto.UserResponse;
import com.kizunavi.entity.User;
import com.kizunavi.exception.BadRequestException;
import com.kizunavi.exception.ResourceNotFoundException;
import com.kizunavi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getCurrentUser: メール一致のユーザー情報を返す")
    void getCurrentUserReturnsMappedResponse() {
        // Given
        User user = baseUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // When
        UserResponse actual = userService.getCurrentUser("user@example.com");

        // Then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getEmail()).isEqualTo("user@example.com");
        assertThat(actual.getName()).isEqualTo("田中太郎");
        assertThat(actual.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("getCurrentUser: ユーザーが存在しない場合は ResourceNotFoundException")
    void getCurrentUserThrowsWhenNotFound() {
        // Given
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.getCurrentUser("missing@example.com"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("ユーザーが見つかりません");
    }

    @Test
    @DisplayName("updateUser: 名前のみ更新する")
    void updateUserUpdatesNameOnly() {
        // Given
        User user = baseUser();
        UpdateUserRequest request = new UpdateUserRequest().name("新しい名前");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0, User.class));

        // When
        UserResponse actual = userService.updateUser("user@example.com", request);

        // Then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("新しい名前");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hashed-password");
        assertThat(actual.getName()).isEqualTo("新しい名前");
    }

    @Test
    @DisplayName("updateUser: 現在パスワード一致時は新しいハッシュを保存する")
    void updateUserUpdatesPasswordWhenCurrentPasswordMatches() {
        // Given
        User user = baseUser();
        UpdateUserRequest request = new UpdateUserRequest()
            .password("newSecurePass123")
            .currentPassword("currentPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "hashed-password")).thenReturn(true);
        when(passwordEncoder.encode("newSecurePass123")).thenReturn("new-hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0, User.class));

        // When
        UserResponse actual = userService.updateUser("user@example.com", request);

        // Then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("new-hashed-password");
        assertThat(captor.getValue().getLastPasswordChangedAt()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("updateUser: 現在パスワード未入力で BadRequestException")
    void updateUserThrowsWhenCurrentPasswordIsMissing() {
        // Given
        User user = baseUser();
        UpdateUserRequest request = new UpdateUserRequest().password("newSecurePass123");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // When / Then
        assertThatThrownBy(() -> userService.updateUser("user@example.com", request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("現在のパスワードを入力してください");
    }

    private User baseUser() {
        return User.builder()
            .userId(1L)
            .email("user@example.com")
            .passwordHash("hashed-password")
            .name("田中太郎")
            .role(Role.ROLE_USER)
            .enabled(true)
            .lastPasswordChangedAt(LocalDateTime.now().minusDays(1))
            .build();
    }
}
