package com.product.template.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

/**
 * リフレッシュトークン用 HttpOnly Cookie の設定・削除・取得を行うユーティリティ。
 *
 * <p>Cookie のパスは認証 API 配下（{@code /api/auth}）に限定し、他エンドポイントへの送信を抑える。</p>
 */
public final class CookieUtil {

    /** リフレッシュトークンを格納する Cookie 名。 */
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    /** Cookie の有効パス（認証 API のみ）。 */
    private static final String COOKIE_PATH = "/api/auth";

    /** インスタンス化禁止。 */
    private CookieUtil() {}

    /**
     * リフレッシュトークンを HttpOnly Cookie としてレスポンスに付与する。
     *
     * @param response HTTP レスポンス
     * @param refreshToken 格納するトークン値
     * @param expirationMs Cookie の最大寿命（ミリ秒）
     * @param secure {@code true} の場合 {@code Secure} 属性を付与（HTTPS のみ送信）
     */
    public static void addRefreshTokenCookie(
            HttpServletResponse response,
            String refreshToken,
            long expirationMs,
            boolean secure
    ) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path(COOKIE_PATH)
                .maxAge(Duration.ofMillis(expirationMs))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * リフレッシュトークン Cookie を無効化する（空値・即時失効）。
     *
     * @param response HTTP レスポンス
     * @param secure {@code Secure} 属性（追加時と同一にする）
     */
    public static void clearRefreshTokenCookie(HttpServletResponse response, boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path(COOKIE_PATH)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * リクエスト Cookie からリフレッシュトークン文字列を取り出す。
     *
     * @param request HTTP リクエスト
     * @return トークン値。該当 Cookie が無い場合は {@code null}
     */
    public static String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
