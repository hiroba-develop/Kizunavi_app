package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * EmployeeValue
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EmployeeValue {

  private @Nullable UUID employeeId;

  private @Nullable String displayName;

  private JsonNullable<String> divisionName = JsonNullable.<String>undefined();

  private @Nullable Integer kizunaLevel;

  private @Nullable BigDecimal scoreExploreExploit;

  private @Nullable BigDecimal scoreLongShort;

  private @Nullable BigDecimal scoreAssertListen;

  private @Nullable BigDecimal scoreExpressSuppress;

  private @Nullable BigDecimal kizunaHumanScore;

  private @Nullable BigDecimal kizunaOrgScore;

  private @Nullable BigDecimal phaseScore;

  public EmployeeValue employeeId(UUID employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * Get employeeId
   * @return employeeId
   */
  @Valid 
  @Schema(name = "employeeId", example = "550e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employeeId")
  public UUID getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(UUID employeeId) {
    this.employeeId = employeeId;
  }

  public EmployeeValue displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", example = "山田太郎", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public EmployeeValue divisionName(String divisionName) {
    this.divisionName = JsonNullable.of(divisionName);
    return this;
  }

  /**
   * Get divisionName
   * @return divisionName
   */
  
  @Schema(name = "divisionName", example = "営業部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionName")
  public JsonNullable<String> getDivisionName() {
    return divisionName;
  }

  public void setDivisionName(JsonNullable<String> divisionName) {
    this.divisionName = divisionName;
  }

  public EmployeeValue kizunaLevel(Integer kizunaLevel) {
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

  public EmployeeValue scoreExploreExploit(BigDecimal scoreExploreExploit) {
    this.scoreExploreExploit = scoreExploreExploit;
    return this;
  }

  /**
   * Get scoreExploreExploit
   * @return scoreExploreExploit
   */
  @Valid 
  @Schema(name = "scoreExploreExploit", example = "0.62", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scoreExploreExploit")
  public BigDecimal getScoreExploreExploit() {
    return scoreExploreExploit;
  }

  public void setScoreExploreExploit(BigDecimal scoreExploreExploit) {
    this.scoreExploreExploit = scoreExploreExploit;
  }

  public EmployeeValue scoreLongShort(BigDecimal scoreLongShort) {
    this.scoreLongShort = scoreLongShort;
    return this;
  }

  /**
   * Get scoreLongShort
   * @return scoreLongShort
   */
  @Valid 
  @Schema(name = "scoreLongShort", example = "0.48", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scoreLongShort")
  public BigDecimal getScoreLongShort() {
    return scoreLongShort;
  }

  public void setScoreLongShort(BigDecimal scoreLongShort) {
    this.scoreLongShort = scoreLongShort;
  }

  public EmployeeValue scoreAssertListen(BigDecimal scoreAssertListen) {
    this.scoreAssertListen = scoreAssertListen;
    return this;
  }

  /**
   * Get scoreAssertListen
   * @return scoreAssertListen
   */
  @Valid 
  @Schema(name = "scoreAssertListen", example = "0.55", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scoreAssertListen")
  public BigDecimal getScoreAssertListen() {
    return scoreAssertListen;
  }

  public void setScoreAssertListen(BigDecimal scoreAssertListen) {
    this.scoreAssertListen = scoreAssertListen;
  }

  public EmployeeValue scoreExpressSuppress(BigDecimal scoreExpressSuppress) {
    this.scoreExpressSuppress = scoreExpressSuppress;
    return this;
  }

  /**
   * Get scoreExpressSuppress
   * @return scoreExpressSuppress
   */
  @Valid 
  @Schema(name = "scoreExpressSuppress", example = "0.71", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scoreExpressSuppress")
  public BigDecimal getScoreExpressSuppress() {
    return scoreExpressSuppress;
  }

  public void setScoreExpressSuppress(BigDecimal scoreExpressSuppress) {
    this.scoreExpressSuppress = scoreExpressSuppress;
  }

  public EmployeeValue kizunaHumanScore(BigDecimal kizunaHumanScore) {
    this.kizunaHumanScore = kizunaHumanScore;
    return this;
  }

  /**
   * クリック時に使用
   * @return kizunaHumanScore
   */
  @Valid 
  @Schema(name = "kizunaHumanScore", example = "72.0", description = "クリック時に使用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaHumanScore")
  public BigDecimal getKizunaHumanScore() {
    return kizunaHumanScore;
  }

  public void setKizunaHumanScore(BigDecimal kizunaHumanScore) {
    this.kizunaHumanScore = kizunaHumanScore;
  }

  public EmployeeValue kizunaOrgScore(BigDecimal kizunaOrgScore) {
    this.kizunaOrgScore = kizunaOrgScore;
    return this;
  }

  /**
   * クリック時に使用
   * @return kizunaOrgScore
   */
  @Valid 
  @Schema(name = "kizunaOrgScore", example = "70.0", description = "クリック時に使用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaOrgScore")
  public BigDecimal getKizunaOrgScore() {
    return kizunaOrgScore;
  }

  public void setKizunaOrgScore(BigDecimal kizunaOrgScore) {
    this.kizunaOrgScore = kizunaOrgScore;
  }

  public EmployeeValue phaseScore(BigDecimal phaseScore) {
    this.phaseScore = phaseScore;
    return this;
  }

  /**
   * クリック時に使用
   * @return phaseScore
   */
  @Valid 
  @Schema(name = "phaseScore", example = "3.0", description = "クリック時に使用", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phaseScore")
  public BigDecimal getPhaseScore() {
    return phaseScore;
  }

  public void setPhaseScore(BigDecimal phaseScore) {
    this.phaseScore = phaseScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeValue employeeValue = (EmployeeValue) o;
    return Objects.equals(this.employeeId, employeeValue.employeeId) &&
        Objects.equals(this.displayName, employeeValue.displayName) &&
        equalsNullable(this.divisionName, employeeValue.divisionName) &&
        Objects.equals(this.kizunaLevel, employeeValue.kizunaLevel) &&
        Objects.equals(this.scoreExploreExploit, employeeValue.scoreExploreExploit) &&
        Objects.equals(this.scoreLongShort, employeeValue.scoreLongShort) &&
        Objects.equals(this.scoreAssertListen, employeeValue.scoreAssertListen) &&
        Objects.equals(this.scoreExpressSuppress, employeeValue.scoreExpressSuppress) &&
        Objects.equals(this.kizunaHumanScore, employeeValue.kizunaHumanScore) &&
        Objects.equals(this.kizunaOrgScore, employeeValue.kizunaOrgScore) &&
        Objects.equals(this.phaseScore, employeeValue.phaseScore);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, displayName, hashCodeNullable(divisionName), kizunaLevel, scoreExploreExploit, scoreLongShort, scoreAssertListen, scoreExpressSuppress, kizunaHumanScore, kizunaOrgScore, phaseScore);
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
    sb.append("class EmployeeValue {\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    divisionName: ").append(toIndentedString(divisionName)).append("\n");
    sb.append("    kizunaLevel: ").append(toIndentedString(kizunaLevel)).append("\n");
    sb.append("    scoreExploreExploit: ").append(toIndentedString(scoreExploreExploit)).append("\n");
    sb.append("    scoreLongShort: ").append(toIndentedString(scoreLongShort)).append("\n");
    sb.append("    scoreAssertListen: ").append(toIndentedString(scoreAssertListen)).append("\n");
    sb.append("    scoreExpressSuppress: ").append(toIndentedString(scoreExpressSuppress)).append("\n");
    sb.append("    kizunaHumanScore: ").append(toIndentedString(kizunaHumanScore)).append("\n");
    sb.append("    kizunaOrgScore: ").append(toIndentedString(kizunaOrgScore)).append("\n");
    sb.append("    phaseScore: ").append(toIndentedString(phaseScore)).append("\n");
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

