package com.product.template.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@code Authorization: Bearer} ヘッダーの JWT を検証し、
 * 有効な場合は {@link SecurityContextHolder} に認証を設定するフィルタ。
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** トークンの検証およびクレーム抽出。 */
    private final JwtTokenProvider jwtTokenProvider;
    /** トークン内のユーザー名から {@link UserDetails} を取得。 */
    private final UserDetailsService userDetailsService;

    /** Bearer トークンを受け取る HTTP ヘッダー名。 */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    /** Bearer スキームの接頭辞。 */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * リクエストごとに JWT を解釈し、認証コンテキストを更新してチェーンを継続する。
     *
     * @param request HTTP リクエスト
     * @param response HTTP レスポンス
     * @param filterChain 後続フィルタ
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * {@code Authorization} ヘッダーから Bearer トークン部分のみを抽出する。
     *
     * @param request HTTP リクエスト
     * @return JWT 文字列。未設定または形式不正時は {@code null}
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
