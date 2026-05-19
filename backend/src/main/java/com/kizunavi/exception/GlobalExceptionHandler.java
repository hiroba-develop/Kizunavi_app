package com.kizunavi.exception;

import com.kizunavi.dto.ErrorResponse;
import com.kizunavi.dto.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * コントローラ層で発生した例外を {@link ErrorResponse} 形式に正規化するグローバルハンドラ。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * {@link ApiException} およびそのサブクラスを処理する。
     *
     * @param ex 発生した API 例外
     * @param request 現在の HTTP リクエスト（パス記録用）
     * @return 例外に紐づくステータスと {@link ErrorResponse}
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
        ApiException ex, 
        HttpServletRequest request
    ) {
        log.error("API Exception: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(ex.getStatus().value())
            .error(ex.getCode())
            .message(ex.getMessage())
            .path(request.getRequestURI());
        
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    /**
     * {@code @Valid} 付きリクエストボディの Bean Validation 失敗を処理する。
     *
     * @param ex メソッド引数バリデーション例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 400 とフィールドエラー一覧
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        log.error("Validation Exception: {}", ex.getMessage());
        
        List<FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldError()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue()))
            .collect(Collectors.toList());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("VALIDATION_ERROR")
            .message("入力値が不正です")
            .path(request.getRequestURI())
            .fieldErrors(fieldErrors);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * フォーム／モデル属性のバインド失敗を処理する。
     *
     * @param ex バインド例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 400 とフィールドエラー一覧
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
        BindException ex,
        HttpServletRequest request
    ) {
        log.error("Bind Exception: {}", ex.getMessage());
        
        List<FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldError()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue()))
            .collect(Collectors.toList());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("BIND_ERROR")
            .message("リクエストの形式が不正です")
            .path(request.getRequestURI())
            .fieldErrors(fieldErrors);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * ログイン時の資格情報不一致を処理する。
     *
     * @param ex Spring Security の不正資格情報例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 401 と汎用メッセージ
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
        BadCredentialsException ex,
        HttpServletRequest request
    ) {
        log.error("Bad Credentials Exception: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("BAD_CREDENTIALS")
            .message("メールアドレスまたはパスワードが正しくありません")
            .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * アカウントロック中のログイン試行を処理する。
     *
     * @param ex ロック例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 423
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(
        LockedException ex,
        HttpServletRequest request
    ) {
        log.error("Locked Exception: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.LOCKED.value())
            .error("ACCOUNT_LOCKED")
            .message("アカウントがロックされています。しばらくしてから再度お試しください")
            .path(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.LOCKED).body(response);
    }

    /**
     * その他の認証例外（{@link BadCredentialsException} を除く）を処理する。
     *
     * @param ex 認証例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 401
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException ex,
        HttpServletRequest request
    ) {
        log.error("Authentication Exception: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("AUTHENTICATION_ERROR")
            .message("認証に失敗しました")
            .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 認可失敗（権限不足）を処理する。
     *
     * @param ex アクセス拒否例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
        AccessDeniedException ex,
        HttpServletRequest request
    ) {
        log.error("Access Denied Exception: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error("ACCESS_DENIED")
            .message("アクセス権限がありません")
            .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * マッピングされない URL へのアクセスを処理する。
     *
     * @param ex ハンドラ未検出例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
        NoHandlerFoundException ex,
        HttpServletRequest request
    ) {
        log.error("No Handler Found Exception: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("NOT_FOUND")
            .message("リクエストされたリソースが見つかりません")
            .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 上記いずれにも該当しない予期せぬ例外を処理する。
     *
     * @param ex 任意の例外
     * @param request 現在の HTTP リクエスト
     * @return HTTP 500 と汎用エラーメッセージ
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Unexpected Exception: ", ex);
        
        ErrorResponse response = new ErrorResponse()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("INTERNAL_ERROR")
            .message("システムエラーが発生しました")
            .path(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
