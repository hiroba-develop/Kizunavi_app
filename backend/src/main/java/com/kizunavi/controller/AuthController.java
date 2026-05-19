package com.kizunavi.controller;

import com.kizunavi.config.JwtConfig;
import com.kizunavi.dto.*;
import com.kizunavi.security.CookieUtil;
import com.kizunavi.service.AuthLoginResult;
import com.kizunavi.service.AuthRefreshResult;
import com.kizunavi.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 認証 API（ログイン・トークン更新・ログアウト・パスワード管理）の REST コントローラ。
 *
 * <p>リフレッシュトークンは HttpOnly Cookie で扱い、JSON ボディには含めない。</p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;

    /**
     * ログインを実行し、アクセストークンと表示名を返却する。
     *
     * @param request     ログイン情報
     * @param httpRequest IP・User-Agent 取得用
     * @param response    リフレッシュトークン Cookie 設定用
     * @return {@link LoginResponse}
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String clientIp = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        AuthLoginResult result = authService.login(request, clientIp, userAgent);
        CookieUtil.addRefreshTokenCookie(
                response,
                result.refreshToken(),
                jwtConfig.getRefreshTokenExpiration(),
                jwtConfig.isCookieSecure()
        );
        return ResponseEntity.ok(
            new LoginResponse(result.accessToken(), result.name())
        );
    }

    /**
     * Cookie のリフレッシュトークンを用いて新しいアクセストークンを発行する。
     *
     * @param request  Cookie 読み取り用
     * @param response 新リフレッシュトークン Cookie 設定用
     * @return {@link TokenRefreshResponse}
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = CookieUtil.extractRefreshToken(request);
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        AuthRefreshResult result = authService.refreshToken(refreshToken, clientIp, userAgent);
        CookieUtil.addRefreshTokenCookie(
                response,
                result.refreshToken(),
                jwtConfig.getRefreshTokenExpiration(),
                jwtConfig.isCookieSecure()
        );
        return ResponseEntity.ok(new TokenRefreshResponse(result.accessToken()));
    }

    /**
     * ログアウトし、サーバー側のリフレッシュトークンおよび Cookie を無効化する。
     *
     * @param userDetails 認証済みユーザー
     * @param response    Cookie 削除用
     * @return {@link StatusMessage}
     */
    @PostMapping("/logout")
    public ResponseEntity<StatusMessage> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(userDetails.getUsername());
        CookieUtil.clearRefreshTokenCookie(response, jwtConfig.isCookieSecure());
        StatusMessage body = new StatusMessage("success");
        body.setMessage(JsonNullable.of("ログアウトしました"));
        return ResponseEntity.ok(body);
    }

    /**
     * 仮パスワードを検証し、新しいパスワードを設定する。
     *
     * @param request 初回ログイン情報
     * @return 設定成功
     */
    @PutMapping("/firstlogin")
    public ResponseEntity<SimpleStatusResponse> firstLogin(
            @Valid @RequestBody FirstLoginRequest request) {
        return ResponseEntity.ok(authService.firstLogin(request));
    }

    /**
     * パスワードリセット用メールを送信する。
     */
    @PostMapping("/password/forgot")
    public ResponseEntity<SimpleStatusResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            HttpServletRequest httpRequest) {
        SimpleStatusResponse status =
            authService.forgotPassword(request, httpRequest.getRemoteAddr());
        return ResponseEntity.ok(status);
    }

    /**
     * パスワードリセットトークンの有効性を検証する。
     */
    @GetMapping("/password/reset/verify/{token}")
    public ResponseEntity<PasswordResetVerifyResponse> verifyResetToken(@PathVariable String token) {
        return ResponseEntity.ok(authService.verifyResetToken(token));
    }

    /**
     * トークンを検証し、新しいパスワードを設定する。
     */
    @PutMapping("/password/reset")
    public ResponseEntity<SimpleStatusResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
