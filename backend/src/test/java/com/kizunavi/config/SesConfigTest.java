package com.kizunavi.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SesConfigTest {

  @Test
  void hasUsableStaticCredentials_空文字列はfalse() {
    assertFalse(SesConfig.hasUsableStaticCredentials("", ""));
    assertFalse(SesConfig.hasUsableStaticCredentials("AKIA123", ""));
  }

  @Test
  void hasUsableStaticCredentials_プレースホルダはfalse() {
    assertFalse(SesConfig.hasUsableStaticCredentials("your-aws-access-key", "your-aws-secret-key"));
    assertFalse(SesConfig.hasUsableStaticCredentials("your-key", "real-secret"));
  }

  @Test
  void hasUsableStaticCredentials_実キーはtrue() {
    assertTrue(SesConfig.hasUsableStaticCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"));
  }
}
