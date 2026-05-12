package com.product.template.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateUserRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateUserRequest {

  private @Nullable String name;

  private @Nullable String password;

  private @Nullable String currentPassword;

  public UpdateUserRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * ユーザー名（100文字以下）
   * @return name
   */
  @Size(max = 100) 
  @Schema(name = "name", example = "山田花子", description = "ユーザー名（100文字以下）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UpdateUserRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * 新しいパスワード（8文字以上100文字以下）
   * @return password
   */
  @Size(min = 8, max = 100) 
  @Schema(name = "password", example = "newSecurePass456", description = "新しいパスワード（8文字以上100文字以下）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UpdateUserRequest currentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
    return this;
  }

  /**
   * 現在のパスワード（パスワード変更時に必要）
   * @return currentPassword
   */
  
  @Schema(name = "currentPassword", example = "oldPassword123", description = "現在のパスワード（パスワード変更時に必要）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentPassword")
  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateUserRequest updateUserRequest = (UpdateUserRequest) o;
    return Objects.equals(this.name, updateUserRequest.name) &&
        Objects.equals(this.password, updateUserRequest.password) &&
        Objects.equals(this.currentPassword, updateUserRequest.currentPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, password, currentPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateUserRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    currentPassword: ").append(toIndentedString(currentPassword)).append("\n");
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

