package com.kizunavi.security;

import com.kizunavi.dto.Role;
import com.kizunavi.entity.Customer;
import com.kizunavi.entity.Employee;
import com.kizunavi.entity.User;
import com.kizunavi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("テナント有効時は UserDetails を返す")
    void loadUserByUsername_success() {
        User user = activeUser();
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.of(user));

        UserDetails details = customUserDetailsService.loadUserByUsername("user@example.com");

        assertThat(details.getUsername()).isEqualTo("user@example.com");
        assertThat(details.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("顧客が削除済みの場合は BadCredentialsException")
    void loadUserByUsername_rejectsDeletedCustomer() {
        User user = activeUser();
        user.getCustomer().setDelFlg("1");
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("user@example.com"))
            .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("従業員が削除済みの場合は BadCredentialsException")
    void loadUserByUsername_rejectsDeletedEmployee() {
        User user = activeUser();
        user.getEmployee().setDelFlg("1");
        when(userRepository.findByEmailWithEmployeeAndCustomer("user@example.com"))
            .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("user@example.com"))
            .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("ユーザー不存在時は UsernameNotFoundException")
    void loadUserByUsername_userNotFound() {
        when(userRepository.findByEmailWithEmployeeAndCustomer("missing@example.com"))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("missing@example.com"))
            .isInstanceOf(UsernameNotFoundException.class);
    }

    private static User activeUser() {
        return User.builder()
            .email("user@example.com")
            .passwordHash("hash")
            .name("テスト")
            .role(Role.ROLE_USER)
            .enabled(true)
            .failedLoginCount(0)
            .customer(Customer.builder().delFlg("0").build())
            .employee(Employee.builder().delFlg("0").build())
            .build();
    }
}
