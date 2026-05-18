package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.Arrays;
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
 * CreateSurveyRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSurveyRequest {

  private JsonNullable<@Size(max = 100) String> surveyName = JsonNullable.<String>undefined();

  private JsonNullable<@Size(max = 500) String> description = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate answerDeadline;

  public CreateSurveyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateSurveyRequest(LocalDate answerDeadline) {
    this.answerDeadline = answerDeadline;
  }

  public CreateSurveyRequest surveyName(String surveyName) {
    this.surveyName = JsonNullable.of(surveyName);
    return this;
  }

  /**
   * サーベイ名
   * @return surveyName
   */
  @Size(max = 100) 
  @Schema(name = "surveyName", example = "2026年5月度サーベイ", description = "サーベイ名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveyName")
  public JsonNullable<@Size(max = 100) String> getSurveyName() {
    return surveyName;
  }

  public void setSurveyName(JsonNullable<String> surveyName) {
    this.surveyName = surveyName;
  }

  public CreateSurveyRequest description(String description) {
    this.description = JsonNullable.of(description);
    return this;
  }

  /**
   * 説明文
   * @return description
   */
  @Size(max = 500) 
  @Schema(name = "description", example = "今月のサーベイです。正直にご回答ください。", description = "説明文", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public JsonNullable<@Size(max = 500) String> getDescription() {
    return description;
  }

  public void setDescription(JsonNullable<String> description) {
    this.description = description;
  }

  public CreateSurveyRequest answerDeadline(LocalDate answerDeadline) {
    this.answerDeadline = answerDeadline;
    return this;
  }

  /**
   * 回答締め切り（YYYY-MM-DD）
   * @return answerDeadline
   */
  @NotNull @Valid 
  @Schema(name = "answerDeadline", example = "2026-05-31", description = "回答締め切り（YYYY-MM-DD）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("answerDeadline")
  public LocalDate getAnswerDeadline() {
    return answerDeadline;
  }

  public void setAnswerDeadline(LocalDate answerDeadline) {
    this.answerDeadline = answerDeadline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSurveyRequest createSurveyRequest = (CreateSurveyRequest) o;
    return equalsNullable(this.surveyName, createSurveyRequest.surveyName) &&
        equalsNullable(this.description, createSurveyRequest.description) &&
        Objects.equals(this.answerDeadline, createSurveyRequest.answerDeadline);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(surveyName), hashCodeNullable(description), answerDeadline);
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
    sb.append("class CreateSurveyRequest {\n");
    sb.append("    surveyName: ").append(toIndentedString(surveyName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    answerDeadline: ").append(toIndentedString(answerDeadline)).append("\n");
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

