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
 * CreateSectionResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSectionResponse {

  private @Nullable String responseStatus;

  private @Nullable UUID sectionId;

  public CreateSectionResponse responseStatus(String responseStatus) {
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

  public CreateSectionResponse sectionId(UUID sectionId) {
    this.sectionId = sectionId;
    return this;
  }

  /**
   * 新規発行UUID
   * @return sectionId
   */
  @Valid 
  @Schema(name = "sectionId", example = "550e8400-e29b-41d4-a716-446655440003", description = "新規発行UUID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sectionId")
  public UUID getSectionId() {
    return sectionId;
  }

  public void setSectionId(UUID sectionId) {
    this.sectionId = sectionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSectionResponse createSectionResponse = (CreateSectionResponse) o;
    return Objects.equals(this.responseStatus, createSectionResponse.responseStatus) &&
        Objects.equals(this.sectionId, createSectionResponse.sectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseStatus, sectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSectionResponse {\n");
    sb.append("    responseStatus: ").append(toIndentedString(responseStatus)).append("\n");
    sb.append("    sectionId: ").append(toIndentedString(sectionId)).append("\n");
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

