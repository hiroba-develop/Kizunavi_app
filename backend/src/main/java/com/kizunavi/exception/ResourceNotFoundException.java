package com.kizunavi.exception;

import org.springframework.http.HttpStatus;

/**
 * 要求されたリソースが存在しない場合に送出する例外（HTTP 404）。
 */
public class ResourceNotFoundException extends ApiException {

    /**
     * @param message クライアントに返すメッセージ
     */
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }
}
