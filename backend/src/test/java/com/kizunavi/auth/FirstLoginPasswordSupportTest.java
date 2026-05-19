package com.kizunavi.auth;

import com.kizunavi.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FirstLoginPasswordSupportTest {

    @Test
    @DisplayName("user が null のときは false")
    void requiresFirstLoginPasswordChangeReturnsFalseWhenUserNull() {
        assertThat(FirstLoginPasswordSupport.requiresFirstLoginPasswordChange(null)).isFalse();
    }

    @Test
    @DisplayName("lastPasswordChangedAt が null のときは false")
    void requiresFirstLoginPasswordChangeReturnsFalseWhenTimestampNull() {
        User user = User.builder().build();
        assertThat(FirstLoginPasswordSupport.requiresFirstLoginPasswordChange(user)).isFalse();
    }

    @Test
    @DisplayName("センチネル時刻のときは true")
    void requiresFirstLoginPasswordChangeReturnsTrueWhenPending() {
        User user = User.builder()
            .lastPasswordChangedAt(FirstLoginPasswordSupport.pendingPasswordChangeTimestamp())
            .build();
        assertThat(FirstLoginPasswordSupport.requiresFirstLoginPasswordChange(user)).isTrue();
    }

    @Test
    @DisplayName("通常の変更日時のときは false")
    void requiresFirstLoginPasswordChangeReturnsFalseWhenAlreadyChanged() {
        User user = User.builder().lastPasswordChangedAt(LocalDateTime.now()).build();
        assertThat(FirstLoginPasswordSupport.requiresFirstLoginPasswordChange(user)).isFalse();
    }

    @Test
    @DisplayName("pendingPasswordChangeTimestamp はセンチネル値を返す")
    void pendingPasswordChangeTimestampReturnsSentinel() {
        assertThat(FirstLoginPasswordSupport.pendingPasswordChangeTimestamp())
            .isEqualTo(FirstLoginPasswordSupport.PENDING_PASSWORD_CHANGE_AT);
    }
}
