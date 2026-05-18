package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.RoleLabel;
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
 * RoleLabelListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RoleLabelListResponse {

  @Valid
  private List<@Valid RoleLabel> roleLabels = new ArrayList<>();

  public RoleLabelListResponse roleLabels(List<@Valid RoleLabel> roleLabels) {
    this.roleLabels = roleLabels;
    return this;
  }

  public RoleLabelListResponse addRoleLabelsItem(RoleLabel roleLabelsItem) {
    if (this.roleLabels == null) {
      this.roleLabels = new ArrayList<>();
    }
    this.roleLabels.add(roleLabelsItem);
    return this;
  }

  /**
   * Get roleLabels
   * @return roleLabels
   */
  @Valid 
  @Schema(name = "roleLabels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleLabels")
  public List<@Valid RoleLabel> getRoleLabels() {
    return roleLabels;
  }

  public void setRoleLabels(List<@Valid RoleLabel> roleLabels) {
    this.roleLabels = roleLabels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleLabelListResponse roleLabelListResponse = (RoleLabelListResponse) o;
    return Objects.equals(this.roleLabels, roleLabelListResponse.roleLabels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleLabels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleLabelListResponse {\n");
    sb.append("    roleLabels: ").append(toIndentedString(roleLabels)).append("\n");
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

