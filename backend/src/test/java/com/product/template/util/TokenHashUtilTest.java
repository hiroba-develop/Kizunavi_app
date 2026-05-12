package com.product.template.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenHashUtilTest {

    @Test
    @DisplayName("同じ入力文字列は同じ SHA-256 ハッシュになる")
    void sha256HexReturnsDeterministicHash() {
        // Arrange
        String rawToken = "refresh-token-sample";

        // Act
        String actual = TokenHashUtil.sha256Hex(rawToken);

        // Assert
        assertThat(actual).hasSize(64);
        assertThat(actual).isEqualTo(TokenHashUtil.sha256Hex(rawToken));
        assertThat(actual).matches("^[0-9a-f]{64}$");
    }

    @Test
    @DisplayName("null を渡すと IllegalArgumentException を送出する")
    void sha256HexThrowsWhenTokenIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TokenHashUtil.sha256Hex(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("rawToken must not be null");
    }
}
