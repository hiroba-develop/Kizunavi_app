package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
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
 * SurveyRespondent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveyRespondent {

  private @Nullable UUID employeeId;

  private @Nullable String displayName;

  private JsonNullable<String> divisionName = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<LocalDateTime> answeredAt = JsonNullable.<LocalDateTime>undefined();

  public SurveyRespondent employeeId(UUID employeeId) {
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

  public SurveyRespondent displayName(String displayName) {
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

  public SurveyRespondent divisionName(String divisionName) {
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

  public SurveyRespondent answeredAt(LocalDateTime answeredAt) {
    this.answeredAt = JsonNullable.of(answeredAt);
    return this;
  }

  /**
   * NULL=未回答 / 値あり=回答済み
   * @return answeredAt
   */
  @Valid 
  @Schema(name = "answeredAt", example = "2026-05-10T14:30Z", description = "NULL=未回答 / 値あり=回答済み", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("answeredAt")
  public JsonNullable<LocalDateTime> getAnsweredAt() {
    return answeredAt;
  }

  public void setAnsweredAt(JsonNullable<LocalDateTime> answeredAt) {
    this.answeredAt = answeredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyRespondent surveyRespondent = (SurveyRespondent) o;
    return Objects.equals(this.employeeId, surveyRespondent.employeeId) &&
        Objects.equals(this.displayName, surveyRespondent.displayName) &&
        equalsNullable(this.divisionName, surveyRespondent.divisionName) &&
        equalsNullable(this.answeredAt, surveyRespondent.answeredAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, displayName, hashCodeNullable(divisionName), hashCodeNullable(answeredAt));
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
    sb.append("class SurveyRespondent {\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    divisionName: ").append(toIndentedString(divisionName)).append("\n");
    sb.append("    answeredAt: ").append(toIndentedString(answeredAt)).append("\n");
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

