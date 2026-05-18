package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.UUID;
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
 * CreateSectionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSectionRequest {

  private UUID divisionId;

  private String displayName;

  private JsonNullable<Integer> displayOrder = JsonNullable.<Integer>undefined();

  public CreateSectionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateSectionRequest(UUID divisionId, String displayName) {
    this.divisionId = divisionId;
    this.displayName = displayName;
  }

  public CreateSectionRequest divisionId(UUID divisionId) {
    this.divisionId = divisionId;
    return this;
  }

  /**
   * 部ID
   * @return divisionId
   */
  @NotNull @Valid 
  @Schema(name = "divisionId", example = "550e8400-e29b-41d4-a716-446655440002", description = "部ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("divisionId")
  public UUID getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(UUID divisionId) {
    this.divisionId = divisionId;
  }

  public CreateSectionRequest displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * 課名称
   * @return displayName
   */
  @NotNull @Size(max = 100) 
  @Schema(name = "displayName", example = "第一営業課", description = "課名称", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public CreateSectionRequest displayOrder(Integer displayOrder) {
    this.displayOrder = JsonNullable.of(displayOrder);
    return this;
  }

  /**
   * 表示順（省略時は末尾）
   * @return displayOrder
   */
  
  @Schema(name = "displayOrder", example = "1", description = "表示順（省略時は末尾）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayOrder")
  public JsonNullable<Integer> getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(JsonNullable<Integer> displayOrder) {
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
    CreateSectionRequest createSectionRequest = (CreateSectionRequest) o;
    return Objects.equals(this.divisionId, createSectionRequest.divisionId) &&
        Objects.equals(this.displayName, createSectionRequest.displayName) &&
        equalsNullable(this.displayOrder, createSectionRequest.displayOrder);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(divisionId, displayName, hashCodeNullable(displayOrder));
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
    sb.append("class CreateSectionRequest {\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
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

