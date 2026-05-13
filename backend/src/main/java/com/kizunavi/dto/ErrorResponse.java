package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.FieldError;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * エラーレスポンス
 */

@Schema(name = "ErrorResponse", description = "エラーレスポンス")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ErrorResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable LocalDateTime timestamp;

  private @Nullable Integer status;

  private @Nullable String error;

  private @Nullable String message;

  private @Nullable String path;

  @Valid
  private List<@Valid FieldError> fieldErrors = new ArrayList<>();

  public ErrorResponse timestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * エラー発生日時
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", description = "エラー発生日時", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ErrorResponse status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * HTTPステータスコード
   * @return status
   */
  
  @Schema(name = "status", example = "400", description = "HTTPステータスコード", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ErrorResponse error(String error) {
    this.error = error;
    return this;
  }

  /**
   * エラーコード
   * @return error
   */
  
  @Schema(name = "error", example = "VALIDATION_ERROR", description = "エラーコード", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public ErrorResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * エラーメッセージ
   * @return message
   */
  
  @Schema(name = "message", example = "入力値が不正です", description = "エラーメッセージ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ErrorResponse path(String path) {
    this.path = path;
    return this;
  }

  /**
   * リクエストパス
   * @return path
   */
  
  @Schema(name = "path", example = "/api/auth/login", description = "リクエストパス", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ErrorResponse fieldErrors(List<@Valid FieldError> fieldErrors) {
    this.fieldErrors = fieldErrors;
    return this;
  }

  public ErrorResponse addFieldErrorsItem(FieldError fieldErrorsItem) {
    if (this.fieldErrors == null) {
      this.fieldErrors = new ArrayList<>();
    }
    this.fieldErrors.add(fieldErrorsItem);
    return this;
  }

  /**
   * フィールドごとのバリデーションエラー（バリデーションエラー時のみ）
   * @return fieldErrors
   */
  @Valid 
  @Schema(name = "fieldErrors", description = "フィールドごとのバリデーションエラー（バリデーションエラー時のみ）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fieldErrors")
  public List<@Valid FieldError> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<@Valid FieldError> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this.timestamp, errorResponse.timestamp) &&
        Objects.equals(this.status, errorResponse.status) &&
        Objects.equals(this.error, errorResponse.error) &&
        Objects.equals(this.message, errorResponse.message) &&
        Objects.equals(this.path, errorResponse.path) &&
        Objects.equals(this.fieldErrors, errorResponse.fieldErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, error, message, path, fieldErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    fieldErrors: ").append(toIndentedString(fieldErrors)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

