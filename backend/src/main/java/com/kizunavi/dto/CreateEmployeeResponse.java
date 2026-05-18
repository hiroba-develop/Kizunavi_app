package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateEmployeeResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateEmployeeResponse {

  private @Nullable String responseStatus;

  private @Nullable UUID employeeId;

  private @Nullable Long userId;

  public CreateEmployeeResponse responseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
    return this;
  }

  /**
   * Get responseStatus
   * @return responseStatus
   */
  
  @Schema(name = "responseStatus", example = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responseStatus")
  public String getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
  }

  public CreateEmployeeResponse employeeId(UUID employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * 新規発行UUID
   * @return employeeId
   */
  @Valid 
  @Schema(name = "employeeId", example = "550e8400-e29b-41d4-a716-446655440001", description = "新規発行UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employeeId")
  public UUID getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(UUID employeeId) {
    this.employeeId = employeeId;
  }

  public CreateEmployeeResponse userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * USERS.userId（IDENTITY）
   * @return userId
   */
  
  @Schema(name = "userId", example = "42", description = "USERS.userId（IDENTITY）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateEmployeeResponse createEmployeeResponse = (CreateEmployeeResponse) o;
    return Objects.equals(this.responseStatus, createEmployeeResponse.responseStatus) &&
        Objects.equals(this.employeeId, createEmployeeResponse.employeeId) &&
        Objects.equals(this.userId, createEmployeeResponse.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseStatus, employeeId, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateEmployeeResponse {\n");
    sb.append("    responseStatus: ").append(toIndentedString(responseStatus)).append("\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

