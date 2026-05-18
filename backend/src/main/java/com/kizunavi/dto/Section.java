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
 * Section
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Section {

  private @Nullable UUID sectionId;

  private @Nullable UUID divisionId;

  private @Nullable String divisionName;

  private @Nullable String displayName;

  private @Nullable Integer displayOrder;

  public Section sectionId(UUID sectionId) {
    this.sectionId = sectionId;
    return this;
  }

  /**
   * Get sectionId
   * @return sectionId
   */
  @Valid 
  @Schema(name = "sectionId", example = "550e8400-e29b-41d4-a716-446655440003", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sectionId")
  public UUID getSectionId() {
    return sectionId;
  }

  public void setSectionId(UUID sectionId) {
    this.sectionId = sectionId;
  }

  public Section divisionId(UUID divisionId) {
    this.divisionId = divisionId;
    return this;
  }

  /**
   * Get divisionId
   * @return divisionId
   */
  @Valid 
  @Schema(name = "divisionId", example = "550e8400-e29b-41d4-a716-446655440002", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionId")
  public UUID getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(UUID divisionId) {
    this.divisionId = divisionId;
  }

  public Section divisionName(String divisionName) {
    this.divisionName = divisionName;
    return this;
  }

  /**
   * Get divisionName
   * @return divisionName
   */
  
  @Schema(name = "divisionName", example = "営業部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionName")
  public String getDivisionName() {
    return divisionName;
  }

  public void setDivisionName(String divisionName) {
    this.divisionName = divisionName;
  }

  public Section displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", example = "第一営業課", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Section displayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
    return this;
  }

  /**
   * Get displayOrder
   * @return displayOrder
   */
  
  @Schema(name = "displayOrder", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayOrder")
  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Section section = (Section) o;
    return Objects.equals(this.sectionId, section.sectionId) &&
        Objects.equals(this.divisionId, section.divisionId) &&
        Objects.equals(this.divisionName, section.divisionName) &&
        Objects.equals(this.displayName, section.displayName) &&
        Objects.equals(this.displayOrder, section.displayOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sectionId, divisionId, divisionName, displayName, displayOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Section {\n");
    sb.append("    sectionId: ").append(toIndentedString(sectionId)).append("\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
    sb.append("    divisionName: ").append(toIndentedString(divisionName)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
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

