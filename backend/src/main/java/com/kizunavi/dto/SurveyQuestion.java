package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SurveyQuestion
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveyQuestion {

  private @Nullable UUID questionId;

  private @Nullable Integer questionNo;

  private @Nullable String questionText;

  public SurveyQuestion questionId(UUID questionId) {
    this.questionId = questionId;
    return this;
  }

  /**
   * Get questionId
   * @return questionId
   */
  @Valid 
  @Schema(name = "questionId", example = "550e8400-e29b-41d4-a716-446655440005", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questionId")
  public UUID getQuestionId() {
    return questionId;
  }

  public void setQuestionId(UUID questionId) {
    this.questionId = questionId;
  }

  public SurveyQuestion questionNo(Integer questionNo) {
    this.questionNo = questionNo;
    return this;
  }

  /**
   * Get questionNo
   * @return questionNo
   */
  
  @Schema(name = "questionNo", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questionNo")
  public Integer getQuestionNo() {
    return questionNo;
  }

  public void setQuestionNo(Integer questionNo) {
    this.questionNo = questionNo;
  }

  public SurveyQuestion questionText(String questionText) {
    this.questionText = questionText;
    return this;
  }

  /**
   * Get questionText
   * @return questionText
   */
  
  @Schema(name = "questionText", example = "上司から役割への期待を明確に伝えられていると感じますか？", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questionText")
  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyQuestion surveyQuestion = (SurveyQuestion) o;
    return Objects.equals(this.questionId, surveyQuestion.questionId) &&
        Objects.equals(this.questionNo, surveyQuestion.questionNo) &&
        Objects.equals(this.questionText, surveyQuestion.questionText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, questionNo, questionText);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyQuestion {\n");
    sb.append("    questionId: ").append(toIndentedString(questionId)).append("\n");
    sb.append("    questionNo: ").append(toIndentedString(questionNo)).append("\n");
    sb.append("    questionText: ").append(toIndentedString(questionText)).append("\n");
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

