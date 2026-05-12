package com.product.template.exception;

import org.springframework.http.HttpStatus;

/**
 * リクエスト内容が不正またはビジネスルール違反の場合に送出する例外（HTTP 400）。
 */
public class BadRequestException extends ApiException {

    /**
     * @param message クライアントに返すメッセージ
     */
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST");
    }
}
