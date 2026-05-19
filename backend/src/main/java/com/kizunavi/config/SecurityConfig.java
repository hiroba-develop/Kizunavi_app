package com.kizunavi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.kizunavi.security.JwtAuthEntryPoint;
import com.kizunavi.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security のフィルタチェーン、認証プロバイダ、パスワードエンコーダを定義する。
 *
 * <p>JWT によるステートレス認証を前提とし、認証 API・OpenAPI・ヘルスチェックのみ匿名許可する。</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    /** リクエストごとに JWT を検証し {@link org.springframework.security.core.context.SecurityContext} に載せるフィルタ。 */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /** 未認証アクセス時に 401 等を返すエントリポイント。 */
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    /** ユーザー名（メール）から {@link org.springframework.security.core.userdetails.UserDetails} を読み込むサービス。 */
    private final UserDetailsService userDetailsService;
    /** CORS 設定（{@link CorsConfig} で定義）。 */
    private final CorsConfigurationSource corsConfigurationSource;

    /** 認証不要でアクセス可能な認証 API パス。 */
    private static final String[] PUBLIC_AUTH_ENDPOINTS = {
        "/api/auth/login",
        "/api/auth/refresh",
        "/api/auth/firstlogin",
        "/api/auth/password/**",
    };

    /** 認証不要でアクセス可能なその他パス。 */
    private static final String[] PUBLIC_ENDPOINTS = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/actuator/health"
    };

    /**
     * HTTP セキュリティフィルタチェーンを構成する。
     *
     * @param http {@link HttpSecurity} ビルダー
     * @return 構成済みの {@link SecurityFilterChain}
     * @throws Exception 設定処理中の例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_AUTH_ENDPOINTS).permitAll()
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/api/auth/logout").authenticated()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DAO 方式の {@link AuthenticationProvider}（ユーザー詳細 + BCrypt）。
     *
     * @return アプリケーションで利用する認証プロバイダ
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 認証マネージャを公開する（ログイン時の {@code authenticate} に利用）。
     *
     * @param config Spring Security の認証設定
     * @return {@link AuthenticationManager}
     * @throws Exception 取得時の例外
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * パスワードのハッシュ化に用いるエンコーダ（BCrypt）。
     *
     * @return {@link PasswordEncoder} 実装
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
