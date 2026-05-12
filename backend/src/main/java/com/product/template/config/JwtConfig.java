package com.product.template.config;

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

    /** HMAC 署名に用いる共有秘密（Base64 文字列として扱われる想定）。 */
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
