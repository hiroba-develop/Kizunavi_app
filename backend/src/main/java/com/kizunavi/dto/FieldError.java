package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * FieldError
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class FieldError {

  private @Nullable String field;

  private @Nullable String message;

  private JsonNullable<Object> rejectedValue = JsonNullable.<Object>undefined();

  public FieldError field(String field) {
    this.field = field;
    return this;
  }

  /**
   * エラーが発生したフィールド名
   * @return field
   */
  
  @Schema(name = "field", example = "email", description = "エラーが発生したフィールド名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public FieldError message(String message) {
    this.message = message;
    return this;
  }

  /**
   * エラーメッセージ
   * @return message
   */
  
  @Schema(name = "message", example = "メールアドレスの形式が正しくありません", description = "エラーメッセージ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public FieldError rejectedValue(Object rejectedValue) {
    this.rejectedValue = JsonNullable.of(rejectedValue);
    return this;
  }

  /**
   * 拒否された値
   * @return rejectedValue
   */
  
  @Schema(name = "rejectedValue", example = "invalid-email", description = "拒否された値", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rejectedValue")
  public JsonNullable<Object> getRejectedValue() {
    return rejectedValue;
  }

  public void setRejectedValue(JsonNullable<Object> rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FieldError fieldError = (FieldError) o;
    return Objects.equals(this.field, fieldError.field) &&
        Objects.equals(this.message, fieldError.message) &&
        equalsNullable(this.rejectedValue, fieldError.rejectedValue);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, message, hashCodeNullable(rejectedValue));
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
    sb.append("class FieldError {\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    rejectedValue: ").append(toIndentedString(rejectedValue)).append("\n");
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

