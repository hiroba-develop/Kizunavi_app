package com.kizunavi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ログイン試行ロックなど認証関連の運用パラメータ。
 */
@Component
@ConfigurationProperties(prefix = "app.auth")
@Getter
@Setter
public class AuthProperties {

    /** 連続ログイン失敗でロックするまでの回数。 */
    private int maxFailedLoginAttempts = 5;

    /** アカウントロック時間（分）。 */
    private int lockDurationMinutes = 15;
}
