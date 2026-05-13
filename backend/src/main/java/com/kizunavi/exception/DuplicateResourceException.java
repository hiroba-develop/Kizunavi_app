package com.kizunavi.exception;

import org.springframework.http.HttpStatus;

/**
 * 一意制約違反など、リソースの重複が検出された場合に送出する例外（HTTP 409）。
 */
public class DuplicateResourceException extends ApiException {

    /**
     * @param message クライアントに返すメッセージ
     */
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }
}
