package com.product.template.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * リフレッシュトークン等を DB へ保存する際の SHA-256 ハッシュ（hex 64 文字）を提供する。
 */
public final class TokenHashUtil {

    private TokenHashUtil() {
    }

    /**
     * 平文トークンを SHA-256 し、小文字の 16 進 64 文字列にする。
     *
     * @param rawToken 平文
     * @return 64 文字の hex
     */
    public static String sha256Hex(String rawToken) {
        if (rawToken == null) {
            throw new IllegalArgumentException("rawToken must not be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
