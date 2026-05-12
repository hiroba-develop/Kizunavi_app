package com.product.template.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * アプリケーション全体で共通的に扱う API 向け実行時例外。
 *
 * <p>HTTP ステータスと機械可読なエラーコードを保持し、
 * {@link GlobalExceptionHandler} から {@link com.product.template.dto.ErrorResponse} に変換される。</p>
 */
@Getter
public class ApiException extends RuntimeException {

    /** レスポンスにマッピングする HTTP ステータス。 */
    private final HttpStatus status;
    /** クライアント識別用のエラーコード（例: {@code BAD_REQUEST}）。 */
    private final String code;

    /**
     * エラーコードに HTTP ステータス名を用いるコンストラクタ。
     *
     * @param message ユーザー向けメッセージ
     * @param status HTTP ステータス
     */
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = status.name();
    }

    /**
     * 明示的なエラーコードを指定するコンストラクタ。
     *
     * @param message ユーザー向けメッセージ
     * @param status HTTP ステータス
     * @param code エラーコード文字列
     */
    public ApiException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
