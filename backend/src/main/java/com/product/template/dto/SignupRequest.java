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
 * SignupRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SignupRequest {

  private String email;

  private String password;

  private String name;

  public SignupRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SignupRequest(String email, String password, String name) {
    this.email = email;
    this.password = password;
    this.name = name;
  }

  public SignupRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * メールアドレス
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "newuser@example.com", description = "メールアドレス", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SignupRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * パスワード（8文字以上100文字以下）
   * @return password
   */
  @NotNull @Size(min = 8, max = 100) 
  @Schema(name = "password", example = "securePass123", description = "パスワード（8文字以上100文字以下）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public SignupRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * ユーザー名（100文字以下）
   * @return name
   */
  @NotNull @Size(max = 100) 
  @Schema(name = "name", example = "田中太郎", description = "ユーザー名（100文字以下）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignupRequest signupRequest = (SignupRequest) o;
    return Objects.equals(this.email, signupRequest.email) &&
        Objects.equals(this.password, signupRequest.password) &&
        Objects.equals(this.name, signupRequest.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignupRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

