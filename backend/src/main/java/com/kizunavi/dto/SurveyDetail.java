package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.SurveyRespondent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * SurveyDetail
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveyDetail {

  private @Nullable UUID surveyId;

  private JsonNullable<String> surveyName = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate answerDeadline;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable LocalDateTime createdAt;

  private @Nullable Integer totalEmployees;

  private @Nullable Integer responseCount;

  private @Nullable Integer noResponseCount;

  @Valid
  private List<@Valid SurveyRespondent> respondents = new ArrayList<>();

  public SurveyDetail surveyId(UUID surveyId) {
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

  public SurveyDetail surveyName(String surveyName) {
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

  public SurveyDetail answerDeadline(LocalDate answerDeadline) {
    this.answerDeadline = answerDeadline;
    return this;
  }

  /**
   * Get answerDeadline
   * @return answerDeadline
   */
  @Valid 
  @Schema(name = "answerDeadline", example = "2026-05-31", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("answerDeadline")
  public LocalDate getAnswerDeadline() {
    return answerDeadline;
  }

  public void setAnswerDeadline(LocalDate answerDeadline) {
    this.answerDeadline = answerDeadline;
  }

  public SurveyDetail createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", example = "2026-05-01T09:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public SurveyDetail totalEmployees(Integer totalEmployees) {
    this.totalEmployees = totalEmployees;
    return this;
  }

  /**
   * Get totalEmployees
   * @return totalEmployees
   */
  
  @Schema(name = "totalEmployees", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalEmployees")
  public Integer getTotalEmployees() {
    return totalEmployees;
  }

  public void setTotalEmployees(Integer totalEmployees) {
    this.totalEmployees = totalEmployees;
  }

  public SurveyDetail responseCount(Integer responseCount) {
    this.responseCount = responseCount;
    return this;
  }

  /**
   * Get responseCount
   * @return responseCount
   */
  
  @Schema(name = "responseCount", example = "43", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responseCount")
  public Integer getResponseCount() {
    return responseCount;
  }

  public void setResponseCount(Integer responseCount) {
    this.responseCount = responseCount;
  }

  public SurveyDetail noResponseCount(Integer noResponseCount) {
    this.noResponseCount = noResponseCount;
    return this;
  }

  /**
   * Get noResponseCount
   * @return noResponseCount
   */
  
  @Schema(name = "noResponseCount", example = "7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("noResponseCount")
  public Integer getNoResponseCount() {
    return noResponseCount;
  }

  public void setNoResponseCount(Integer noResponseCount) {
    this.noResponseCount = noResponseCount;
  }

  public SurveyDetail respondents(List<@Valid SurveyRespondent> respondents) {
    this.respondents = respondents;
    return this;
  }

  public SurveyDetail addRespondentsItem(SurveyRespondent respondentsItem) {
    if (this.respondents == null) {
      this.respondents = new ArrayList<>();
    }
    this.respondents.add(respondentsItem);
    return this;
  }

  /**
   * Get respondents
   * @return respondents
   */
  @Valid 
  @Schema(name = "respondents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("respondents")
  public List<@Valid SurveyRespondent> getRespondents() {
    return respondents;
  }

  public void setRespondents(List<@Valid SurveyRespondent> respondents) {
    this.respondents = respondents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyDetail surveyDetail = (SurveyDetail) o;
    return Objects.equals(this.surveyId, surveyDetail.surveyId) &&
        equalsNullable(this.surveyName, surveyDetail.surveyName) &&
        Objects.equals(this.answerDeadline, surveyDetail.answerDeadline) &&
        Objects.equals(this.createdAt, surveyDetail.createdAt) &&
        Objects.equals(this.totalEmployees, surveyDetail.totalEmployees) &&
        Objects.equals(this.responseCount, surveyDetail.responseCount) &&
        Objects.equals(this.noResponseCount, surveyDetail.noResponseCount) &&
        Objects.equals(this.respondents, surveyDetail.respondents);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveyId, hashCodeNullable(surveyName), answerDeadline, createdAt, totalEmployees, responseCount, noResponseCount, respondents);
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
    sb.append("class SurveyDetail {\n");
    sb.append("    surveyId: ").append(toIndentedString(surveyId)).append("\n");
    sb.append("    surveyName: ").append(toIndentedString(surveyName)).append("\n");
    sb.append("    answerDeadline: ").append(toIndentedString(answerDeadline)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    totalEmployees: ").append(toIndentedString(totalEmployees)).append("\n");
    sb.append("    responseCount: ").append(toIndentedString(responseCount)).append("\n");
    sb.append("    noResponseCount: ").append(toIndentedString(noResponseCount)).append("\n");
    sb.append("    respondents: ").append(toIndentedString(respondents)).append("\n");
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

