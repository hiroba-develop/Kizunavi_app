package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
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
 * CreateDivisionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateDivisionRequest {

  private String displayName;

  private JsonNullable<Integer> displayOrder = JsonNullable.<Integer>undefined();

  public CreateDivisionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateDivisionRequest(String displayName) {
    this.displayName = displayName;
  }

  public CreateDivisionRequest displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * 部名称
   * @return displayName
   */
  @NotNull @Size(max = 100) 
  @Schema(name = "displayName", example = "営業部", description = "部名称", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public CreateDivisionRequest displayOrder(Integer displayOrder) {
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
    CreateDivisionRequest createDivisionRequest = (CreateDivisionRequest) o;
    return Objects.equals(this.displayName, createDivisionRequest.displayName) &&
        equalsNullable(this.displayOrder, createDivisionRequest.displayOrder);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayName, hashCodeNullable(displayOrder));
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
    sb.append("class CreateDivisionRequest {\n");
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

