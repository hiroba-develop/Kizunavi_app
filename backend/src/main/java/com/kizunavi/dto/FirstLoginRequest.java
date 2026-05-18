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
 * FirstLoginRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class FirstLoginRequest {

  private String email;

  private String tempPassword;

  private String newPassword;

  public FirstLoginRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FirstLoginRequest(String email, String tempPassword, String newPassword) {
    this.email = email;
    this.tempPassword = tempPassword;
    this.newPassword = newPassword;
  }

  public FirstLoginRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * メールアドレス
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "yamada.taro@example.co.jp", description = "メールアドレス", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public FirstLoginRequest tempPassword(String tempPassword) {
    this.tempPassword = tempPassword;
    return this;
  }

  /**
   * 仮パスワード（BCrypt検証）
   * @return tempPassword
   */
  @NotNull 
  @Schema(name = "tempPassword", example = "TempPass001", description = "仮パスワード（BCrypt検証）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tempPassword")
  public String getTempPassword() {
    return tempPassword;
  }

  public void setTempPassword(String tempPassword) {
    this.tempPassword = tempPassword;
  }

  public FirstLoginRequest newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * 新パスワード（BCryptハッシュ化）
   * @return newPassword
   */
  @NotNull @Size(min = 8, max = 100) 
  @Schema(name = "newPassword", example = "NewP@ssw0rd456", description = "新パスワード（BCryptハッシュ化）", requiredMode = Schema.RequiredMode.REQUIRED)
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
    FirstLoginRequest firstLoginRequest = (FirstLoginRequest) o;
    return Objects.equals(this.email, firstLoginRequest.email) &&
        Objects.equals(this.tempPassword, firstLoginRequest.tempPassword) &&
        Objects.equals(this.newPassword, firstLoginRequest.newPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, tempPassword, newPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FirstLoginRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    tempPassword: ").append(toIndentedString(tempPassword)).append("\n");
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

