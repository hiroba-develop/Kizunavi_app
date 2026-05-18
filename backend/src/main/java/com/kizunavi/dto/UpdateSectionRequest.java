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
 * UpdateSectionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateSectionRequest {

  private JsonNullable<UUID> divisionId = JsonNullable.<UUID>undefined();

  private JsonNullable<@Size(max = 100) String> displayName = JsonNullable.<String>undefined();

  public UpdateSectionRequest divisionId(UUID divisionId) {
    this.divisionId = JsonNullable.of(divisionId);
    return this;
  }

  /**
   * Get divisionId
   * @return divisionId
   */
  @Valid 
  @Schema(name = "divisionId", example = "550e8400-e29b-41d4-a716-446655440002", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionId")
  public JsonNullable<UUID> getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(JsonNullable<UUID> divisionId) {
    this.divisionId = divisionId;
  }

  public UpdateSectionRequest displayName(String displayName) {
    this.displayName = JsonNullable.of(displayName);
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  @Size(max = 100) 
  @Schema(name = "displayName", example = "第二営業課", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public JsonNullable<@Size(max = 100) String> getDisplayName() {
    return displayName;
  }

  public void setDisplayName(JsonNullable<String> displayName) {
    this.displayName = displayName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateSectionRequest updateSectionRequest = (UpdateSectionRequest) o;
    return equalsNullable(this.divisionId, updateSectionRequest.divisionId) &&
        equalsNullable(this.displayName, updateSectionRequest.displayName);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(divisionId), hashCodeNullable(displayName));
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
    sb.append("class UpdateSectionRequest {\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
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

