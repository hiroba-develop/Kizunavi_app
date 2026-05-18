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
 * CreateDivisionResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateDivisionResponse {

  private @Nullable String responseStatus;

  private @Nullable UUID divisionId;

  public CreateDivisionResponse responseStatus(String responseStatus) {
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

  public CreateDivisionResponse divisionId(UUID divisionId) {
    this.divisionId = divisionId;
    return this;
  }

  /**
   * 新規発行UUID
   * @return divisionId
   */
  @Valid 
  @Schema(name = "divisionId", example = "550e8400-e29b-41d4-a716-446655440002", description = "新規発行UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionId")
  public UUID getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(UUID divisionId) {
    this.divisionId = divisionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateDivisionResponse createDivisionResponse = (CreateDivisionResponse) o;
    return Objects.equals(this.responseStatus, createDivisionResponse.responseStatus) &&
        Objects.equals(this.divisionId, createDivisionResponse.divisionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseStatus, divisionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateDivisionResponse {\n");
    sb.append("    responseStatus: ").append(toIndentedString(responseStatus)).append("\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
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

