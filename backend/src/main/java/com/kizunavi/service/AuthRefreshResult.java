package com.kizunavi.service;

/**
 * トークンリフレッシュ成功時にコントローラへ返す内部結果。
 *
 * @param accessToken 新しい JWT アクセストークン
 * @param refreshToken 新しいリフレッシュトークン（Cookie 設定用）
 */
public record AuthRefreshResult(String accessToken, String refreshToken) {}
