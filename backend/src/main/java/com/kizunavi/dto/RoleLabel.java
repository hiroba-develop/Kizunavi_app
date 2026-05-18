package com.kizunavi.dto;

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
 * RoleLabel
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RoleLabel {

  private @Nullable Integer kizunaLevel;

  private @Nullable String displayName;

  public RoleLabel kizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
    return this;
  }

  /**
   * 役職レベル（1〜5）
   * minimum: 1
   * maximum: 5
   * @return kizunaLevel
   */
  @Min(1) @Max(5) 
  @Schema(name = "kizunaLevel", example = "3", description = "役職レベル（1〜5）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaLevel")
  public Integer getKizunaLevel() {
    return kizunaLevel;
  }

  public void setKizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
  }

  public RoleLabel displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * 役職表示名
   * @return displayName
   */
  
  @Schema(name = "displayName", example = "部長", description = "役職表示名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
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
    RoleLabel roleLabel = (RoleLabel) o;
    return Objects.equals(this.kizunaLevel, roleLabel.kizunaLevel) &&
        Objects.equals(this.displayName, roleLabel.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kizunaLevel, displayName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleLabel {\n");
    sb.append("    kizunaLevel: ").append(toIndentedString(kizunaLevel)).append("\n");
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

