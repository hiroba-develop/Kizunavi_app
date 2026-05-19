package com.kizunavi.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 開発用 seed SQL 向けの BCrypt ハッシュ検証・生成。
 */
class DevPasswordHashTest {

    /** {@code dev_seed_first_login_user.sql} の仮パスワード {@code TempPass001} に対応 */
    private static final String SEED_TEMP_PASSWORD_HASH =
        "$2a$10$gVi9Ik7s7hSv8zWCMjdWeuoYC2yPRWsDuA9pD2qOeSWOR1wlrLExK";

    @Test
    void devSeedFirstLoginUser_tempPasswordHashMatches() {
        assertThat(new BCryptPasswordEncoder().matches("TempPass001", SEED_TEMP_PASSWORD_HASH))
            .isTrue();
    }

    @Test
    @Disabled("手動実行: ./gradlew test --tests com.kizunavi.util.DevPasswordHashTest.writeBcryptHashForSeedSql")
    void writeBcryptHashForSeedSql() throws Exception {
        String plain = "TempPass001";
        String hash = new BCryptPasswordEncoder().encode(plain);
        Files.writeString(Path.of("build/dev-password-hash.txt"), hash + System.lineSeparator());
        System.out.println("plain=" + plain);
        System.out.println("hash=" + hash);
    }
}
