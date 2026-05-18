package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.SurveyQuestion;
import java.time.LocalDate;
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
 * SurveyAnswerFormResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveyAnswerFormResponse {

  private @Nullable UUID surveyId;

  private JsonNullable<String> surveyName = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate answerDeadline;

  private @Nullable Boolean alreadyAnswered;

  @Valid
  private List<@Valid SurveyQuestion> questions = new ArrayList<>();

  public SurveyAnswerFormResponse surveyId(UUID surveyId) {
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

  public SurveyAnswerFormResponse surveyName(String surveyName) {
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

  public SurveyAnswerFormResponse answerDeadline(LocalDate answerDeadline) {
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

  public SurveyAnswerFormResponse alreadyAnswered(Boolean alreadyAnswered) {
    this.alreadyAnswered = alreadyAnswered;
    return this;
  }

  /**
   * true=回答済み（フロントが回答済み画面を表示）
   * @return alreadyAnswered
   */
  
  @Schema(name = "alreadyAnswered", example = "false", description = "true=回答済み（フロントが回答済み画面を表示）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alreadyAnswered")
  public Boolean getAlreadyAnswered() {
    return alreadyAnswered;
  }

  public void setAlreadyAnswered(Boolean alreadyAnswered) {
    this.alreadyAnswered = alreadyAnswered;
  }

  public SurveyAnswerFormResponse questions(List<@Valid SurveyQuestion> questions) {
    this.questions = questions;
    return this;
  }

  public SurveyAnswerFormResponse addQuestionsItem(SurveyQuestion questionsItem) {
    if (this.questions == null) {
      this.questions = new ArrayList<>();
    }
    this.questions.add(questionsItem);
    return this;
  }

  /**
   * Get questions
   * @return questions
   */
  @Valid 
  @Schema(name = "questions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questions")
  public List<@Valid SurveyQuestion> getQuestions() {
    return questions;
  }

  public void setQuestions(List<@Valid SurveyQuestion> questions) {
    this.questions = questions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyAnswerFormResponse surveyAnswerFormResponse = (SurveyAnswerFormResponse) o;
    return Objects.equals(this.surveyId, surveyAnswerFormResponse.surveyId) &&
        equalsNullable(this.surveyName, surveyAnswerFormResponse.surveyName) &&
        Objects.equals(this.answerDeadline, surveyAnswerFormResponse.answerDeadline) &&
        Objects.equals(this.alreadyAnswered, surveyAnswerFormResponse.alreadyAnswered) &&
        Objects.equals(this.questions, surveyAnswerFormResponse.questions);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveyId, hashCodeNullable(surveyName), answerDeadline, alreadyAnswered, questions);
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
    sb.append("class SurveyAnswerFormResponse {\n");
    sb.append("    surveyId: ").append(toIndentedString(surveyId)).append("\n");
    sb.append("    surveyName: ").append(toIndentedString(surveyName)).append("\n");
    sb.append("    answerDeadline: ").append(toIndentedString(answerDeadline)).append("\n");
    sb.append("    alreadyAnswered: ").append(toIndentedString(alreadyAnswered)).append("\n");
    sb.append("    questions: ").append(toIndentedString(questions)).append("\n");
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

