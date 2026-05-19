package com.kizunavi.service;

/**
 * ログイン成功時にコントローラへ返す内部結果（アクセストークン・リフレッシュ・表示名）。
 *
 * @param accessToken JWT アクセストークン
 * @param refreshToken リフレッシュトークン（Cookie 設定用、レスポンス body には含めない）
 * @param name ユーザー表示名
 */
public record AuthLoginResult(String accessToken, String refreshToken, String name) {}
