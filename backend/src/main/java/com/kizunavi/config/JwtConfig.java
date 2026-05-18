package com.kizunavi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * JWT 署名およびトークン有効期限、リフレッシュトークン用 Cookie の属性を束ねる設定。
 *
 * <p>プロパティプレフィックスは {@code jwt}（例: {@code jwt.secret}）。</p>
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    /**
     * HMAC 署名に用いる共有秘密。
     * RFC 4648 の標準 Base64、または URL-safe Base64（{@code -} / {@code _}）でエンコードされた 256bit 以上の鍵素材、
     * あるいは UTF-8 で 32 バイト以上の文字列。
     * それより短い場合は {@link com.kizunavi.security.JwtTokenProvider} 側で SHA-256 により 32 バイトへ正規化する。
     */
    private String secret;
    /** アクセストークンの有効期限（ミリ秒）。 */
    private long accessTokenExpiration;
    /** リフレッシュトークンの有効期限（ミリ秒）。 */
    private long refreshTokenExpiration;
    /**
     * リフレッシュトークン Cookie の {@code Secure} フラグ。
     * {@code true} の場合 HTTPS のみで送信される。
     */
    private boolean cookieSecure = true;
}
