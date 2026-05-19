package com.kizunavi.security;

import com.kizunavi.auth.TenantLoginSupport;
import com.kizunavi.entity.User;
import com.kizunavi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * メールアドレスをキーに {@link UserDetails} を構築する Spring Security 用サービス。
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 認証時に呼ばれ、メールに対応するユーザーを DB から読み込む。
     *
     * @param email ログイン ID（メールアドレス）
     * @return Spring Security の {@link UserDetails}
     * @throws UsernameNotFoundException ユーザーが存在しない場合
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithEmployeeAndCustomer(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with email: " + email
            ));

        if (!TenantLoginSupport.isLoginAllowed(user)) {
            throw new BadCredentialsException("Tenant is not active");
        }

        boolean accountNonLocked = !isAccountLocked(user);

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash(),
            user.isEnabled(),
            true,
            true,
            accountNonLocked,
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    private boolean isAccountLocked(User user) {
        LocalDateTime lockedUntil = user.getLockedUntil();
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }
}
