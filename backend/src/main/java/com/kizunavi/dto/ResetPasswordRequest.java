package com.kizunavi.dto;

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
 * ResetPasswordRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ResetPasswordRequest {

  private String token;

  private String newPassword;

  public ResetPasswordRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResetPasswordRequest(String token, String newPassword) {
    this.token = token;
    this.newPassword = newPassword;
  }

  public ResetPasswordRequest token(String token) {
    this.token = token;
    return this;
  }

  /**
   * リセットトークン（PASSWORD_RESET_TOKENS.tokenHashの元値）
   * @return token
   */
  @NotNull 
  @Schema(name = "token", example = "a1b2c3d4e5f6789012345678901234567890abcd", description = "リセットトークン（PASSWORD_RESET_TOKENS.tokenHashの元値）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public ResetPasswordRequest newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * 新パスワード（BCryptハッシュ化してUSERS.passwordHashへ）
   * @return newPassword
   */
  @NotNull @Size(min = 8, max = 100) 
  @Schema(name = "newPassword", example = "NewP@ssw0rd456", description = "新パスワード（BCryptハッシュ化してUSERS.passwordHashへ）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPassword")
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetPasswordRequest resetPasswordRequest = (ResetPasswordRequest) o;
    return Objects.equals(this.token, resetPasswordRequest.token) &&
        Objects.equals(this.newPassword, resetPasswordRequest.newPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, newPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetPasswordRequest {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
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

