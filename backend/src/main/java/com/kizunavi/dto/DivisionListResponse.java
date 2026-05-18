package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.Division;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DivisionListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DivisionListResponse {

  @Valid
  private List<@Valid Division> divisions = new ArrayList<>();

  public DivisionListResponse divisions(List<@Valid Division> divisions) {
    this.divisions = divisions;
    return this;
  }

  public DivisionListResponse addDivisionsItem(Division divisionsItem) {
    if (this.divisions == null) {
      this.divisions = new ArrayList<>();
    }
    this.divisions.add(divisionsItem);
    return this;
  }

  /**
   * Get divisions
   * @return divisions
   */
  @Valid 
  @Schema(name = "divisions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisions")
  public List<@Valid Division> getDivisions() {
    return divisions;
  }

  public void setDivisions(List<@Valid Division> divisions) {
    this.divisions = divisions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DivisionListResponse divisionListResponse = (DivisionListResponse) o;
    return Objects.equals(this.divisions, divisionListResponse.divisions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(divisions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DivisionListResponse {\n");
    sb.append("    divisions: ").append(toIndentedString(divisions)).append("\n");
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

