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
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
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
  private LocalDateTime timestamp;

  private Integer status;

  private String error;

  private String message;

  private String path;

  @Valid
  private JsonNullable<List<@Valid FieldError>> fieldErrors = JsonNullable.<List<@Valid FieldError>>undefined();

  public ErrorResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ErrorResponse(LocalDateTime timestamp, Integer status, String error, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  public ErrorResponse timestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * エラー発生日時
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", example = "2026-05-01T09:00Z", description = "エラー発生日時", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "status", example = "400", description = "HTTPステータスコード", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "error", example = "VALIDATION_ERROR", description = "エラーコード", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "message", example = "入力値が不正です", description = "エラーメッセージ", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull 
  @Schema(name = "path", example = "/api/auth/login", description = "リクエストパス", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ErrorResponse fieldErrors(List<@Valid FieldError> fieldErrors) {
    this.fieldErrors = JsonNullable.of(fieldErrors);
    return this;
  }

  public ErrorResponse addFieldErrorsItem(FieldError fieldErrorsItem) {
    if (this.fieldErrors == null || !this.fieldErrors.isPresent()) {
      this.fieldErrors = JsonNullable.of(new ArrayList<>());
    }
    this.fieldErrors.get().add(fieldErrorsItem);
    return this;
  }

  /**
   * フィールドごとのバリデーションエラー（バリデーションエラー時のみ）
   * @return fieldErrors
   */
  @Valid 
  @Schema(name = "fieldErrors", description = "フィールドごとのバリデーションエラー（バリデーションエラー時のみ）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fieldErrors")
  public JsonNullable<List<@Valid FieldError>> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(JsonNullable<List<@Valid FieldError>> fieldErrors) {
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
        equalsNullable(this.fieldErrors, errorResponse.fieldErrors);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, error, message, path, hashCodeNullable(fieldErrors));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
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

