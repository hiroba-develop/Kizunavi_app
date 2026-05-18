package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * SurveySummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveySummary {

  private @Nullable UUID surveyId;

  private JsonNullable<String> surveyName = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate implementationDate;

  private @Nullable BigDecimal totalKizunaScore;

  public SurveySummary surveyId(UUID surveyId) {
    this.surveyId = surveyId;
    return this;
  }

  /**
   * Get surveyId
   * @return surveyId
   */
  @Valid 
  @Schema(name = "surveyId", example = "550e8400-e29b-41d4-a716-446655440004", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveyId")
  public UUID getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(UUID surveyId) {
    this.surveyId = surveyId;
  }

  public SurveySummary surveyName(String surveyName) {
    this.surveyName = JsonNullable.of(surveyName);
    return this;
  }

  /**
   * Get surveyName
   * @return surveyName
   */
  
  @Schema(name = "surveyName", example = "2026年5月度サーベイ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveyName")
  public JsonNullable<String> getSurveyName() {
    return surveyName;
  }

  public void setSurveyName(JsonNullable<String> surveyName) {
    this.surveyName = surveyName;
  }

  public SurveySummary implementationDate(LocalDate implementationDate) {
    this.implementationDate = implementationDate;
    return this;
  }

  /**
   * Get implementationDate
   * @return implementationDate
   */
  @Valid 
  @Schema(name = "implementationDate", example = "2026-05-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implementationDate")
  public LocalDate getImplementationDate() {
    return implementationDate;
  }

  public void setImplementationDate(LocalDate implementationDate) {
    this.implementationDate = implementationDate;
  }

  public SurveySummary totalKizunaScore(BigDecimal totalKizunaScore) {
    this.totalKizunaScore = totalKizunaScore;
    return this;
  }

  /**
   * Get totalKizunaScore
   * @return totalKizunaScore
   */
  @Valid 
  @Schema(name = "totalKizunaScore", example = "72.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalKizunaScore")
  public BigDecimal getTotalKizunaScore() {
    return totalKizunaScore;
  }

  public void setTotalKizunaScore(BigDecimal totalKizunaScore) {
    this.totalKizunaScore = totalKizunaScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveySummary surveySummary = (SurveySummary) o;
    return Objects.equals(this.surveyId, surveySummary.surveyId) &&
        equalsNullable(this.surveyName, surveySummary.surveyName) &&
        Objects.equals(this.implementationDate, surveySummary.implementationDate) &&
        Objects.equals(this.totalKizunaScore, surveySummary.totalKizunaScore);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveyId, hashCodeNullable(surveyName), implementationDate, totalKizunaScore);
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
    sb.append("class SurveySummary {\n");
    sb.append("    surveyId: ").append(toIndentedString(surveyId)).append("\n");
    sb.append("    surveyName: ").append(toIndentedString(surveyName)).append("\n");
    sb.append("    implementationDate: ").append(toIndentedString(implementationDate)).append("\n");
    sb.append("    totalKizunaScore: ").append(toIndentedString(totalKizunaScore)).append("\n");
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

