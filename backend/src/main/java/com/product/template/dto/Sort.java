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
 * Sort
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Sort {

  private @Nullable Boolean empty;

  private @Nullable Boolean sorted;

  private @Nullable Boolean unsorted;

  public Sort empty(Boolean empty) {
    this.empty = empty;
    return this;
  }

  /**
   * Get empty
   * @return empty
   */
  
  @Schema(name = "empty", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("empty")
  public Boolean getEmpty() {
    return empty;
  }

  public void setEmpty(Boolean empty) {
    this.empty = empty;
  }

  public Sort sorted(Boolean sorted) {
    this.sorted = sorted;
    return this;
  }

  /**
   * Get sorted
   * @return sorted
   */
  
  @Schema(name = "sorted", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sorted")
  public Boolean getSorted() {
    return sorted;
  }

  public void setSorted(Boolean sorted) {
    this.sorted = sorted;
  }

  public Sort unsorted(Boolean unsorted) {
    this.unsorted = unsorted;
    return this;
  }

  /**
   * Get unsorted
   * @return unsorted
   */
  
  @Schema(name = "unsorted", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unsorted")
  public Boolean getUnsorted() {
    return unsorted;
  }

  public void setUnsorted(Boolean unsorted) {
    this.unsorted = unsorted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sort sort = (Sort) o;
    return Objects.equals(this.empty, sort.empty) &&
        Objects.equals(this.sorted, sort.sorted) &&
        Objects.equals(this.unsorted, sort.unsorted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(empty, sorted, unsorted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sort {\n");
    sb.append("    empty: ").append(toIndentedString(empty)).append("\n");
    sb.append("    sorted: ").append(toIndentedString(sorted)).append("\n");
    sb.append("    unsorted: ").append(toIndentedString(unsorted)).append("\n");
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

