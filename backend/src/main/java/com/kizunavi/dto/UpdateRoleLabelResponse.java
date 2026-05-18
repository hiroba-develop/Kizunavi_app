package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
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
 * UpdateRoleLabelResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateRoleLabelResponse {

  private @Nullable String responseStatus;

  private @Nullable Integer kizunaLevel;

  private @Nullable String displayName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable LocalDateTime updatedAt;

  public UpdateRoleLabelResponse responseStatus(String responseStatus) {
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

  public UpdateRoleLabelResponse kizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
    return this;
  }

  /**
   * Get kizunaLevel
   * @return kizunaLevel
   */
  
  @Schema(name = "kizunaLevel", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaLevel")
  public Integer getKizunaLevel() {
    return kizunaLevel;
  }

  public void setKizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
  }

  public UpdateRoleLabelResponse displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", example = "部長", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public UpdateRoleLabelResponse updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", example = "2026-05-01T09:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateRoleLabelResponse updateRoleLabelResponse = (UpdateRoleLabelResponse) o;
    return Objects.equals(this.responseStatus, updateRoleLabelResponse.responseStatus) &&
        Objects.equals(this.kizunaLevel, updateRoleLabelResponse.kizunaLevel) &&
        Objects.equals(this.displayName, updateRoleLabelResponse.displayName) &&
        Objects.equals(this.updatedAt, updateRoleLabelResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseStatus, kizunaLevel, displayName, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateRoleLabelResponse {\n");
    sb.append("    responseStatus: ").append(toIndentedString(responseStatus)).append("\n");
    sb.append("    kizunaLevel: ").append(toIndentedString(kizunaLevel)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

