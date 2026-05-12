package com.product.template.controller;

import com.product.template.config.JwtConfig;
import com.product.template.dto.*;
import com.product.template.security.CookieUtil;
import com.product.template.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 認証 API（ログイン・サインアップ・トークン更新・ログアウト）の REST コントローラ。
 *
 * <p>リフレッシュトークンは HttpOnly Cookie で扱い、JSON ボディからは除去して返す。</p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /** 認証ユースケース。 */
    private final AuthService authService;
    /** Cookie の Secure 属性およびトークン有効期限の参照。 */
    private final JwtConfig jwtConfig;

    /**
     * ログインを実行し、アクセストークンを返却する。
     *
     * <p>リフレッシュトークンは HttpOnly Cookie として設定され、
     * レスポンスボディの {@code refreshToken} は {@code null} に置換される。</p>
     *
     * @param request ログイン情報（メールアドレス・パスワード）
     * @param response リフレッシュトークン用 Cookie を書き込む HTTP レスポンス
     * @return アクセストークンを含む {@link TokenResponse}（{@code refreshToken} は常に {@code null}）
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String clientIp = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        TokenResponse tokenResponse = authService.login(request, clientIp, userAgent);
        CookieUtil.addRefreshTokenCookie(
                response,
                tokenResponse.getRefreshToken(),
                jwtConfig.getRefreshTokenExpiration(),
                jwtConfig.isCookieSecure()
        );
        tokenResponse.setRefreshToken(null);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 新規ユーザー登録を行う。
     *
     * @param request サインアップ情報
     * @return 作成されたユーザー情報（HTTP 201）
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse userResponse = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    /**
     * Cookie のリフレッシュトークンを用いて新しいトークンペアを発行する。
     *
     * <p>新しいリフレッシュトークンは Cookie に再設定され、ボディの {@code refreshToken} は {@code null}。</p>
     *
     * @param request Cookie を読み取るための HTTP リクエスト
     * @param response 新しいリフレッシュトークン用 Cookie を書き込むレスポンス
     * @return アクセストークンを含む {@link TokenResponse}
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = CookieUtil.extractRefreshToken(request);
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        TokenResponse tokenResponse = authService.refreshToken(refreshToken, clientIp, userAgent);
        CookieUtil.addRefreshTokenCookie(
                response,
                tokenResponse.getRefreshToken(),
                jwtConfig.getRefreshTokenExpiration(),
                jwtConfig.isCookieSecure()
        );
        tokenResponse.setRefreshToken(null);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * ログアウトし、サーバー側のリフレッシュトークンおよび Cookie を無効化する。
     *
     * @param userDetails 認証済みユーザー（メールは {@code getUsername()}）
     * @param response リフレッシュトークン Cookie を削除するレスポンス
     * @return 本文なし（HTTP 204）
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletResponse response
    ) {
        authService.logout(userDetails.getUsername());
        CookieUtil.clearRefreshTokenCookie(response, jwtConfig.isCookieSecure());
        return ResponseEntity.noContent().build();
    }
}
