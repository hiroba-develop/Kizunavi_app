package com.kizunavi.exception;

import org.springframework.http.HttpStatus;

/**
 * JWT リフレッシュ等でトークンが無効な場合に送出する例外（HTTP 401）。
 */
public class InvalidTokenException extends ApiException {

    /**
     * @param message クライアントに返すメッセージ
     */
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }
}
