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
 * AnswerItem
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class AnswerItem {

  private UUID questionId;

  private Integer answerValue;

  public AnswerItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AnswerItem(UUID questionId, Integer answerValue) {
    this.questionId = questionId;
    this.answerValue = answerValue;
  }

  public AnswerItem questionId(UUID questionId) {
    this.questionId = questionId;
    return this;
  }

  /**
   * 設問ID
   * @return questionId
   */
  @NotNull @Valid 
  @Schema(name = "questionId", example = "550e8400-e29b-41d4-a716-446655440005", description = "設問ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("questionId")
  public UUID getQuestionId() {
    return questionId;
  }

  public void setQuestionId(UUID questionId) {
    this.questionId = questionId;
  }

  public AnswerItem answerValue(Integer answerValue) {
    this.answerValue = answerValue;
    return this;
  }

  /**
   * 回答値（1〜7）
   * minimum: 1
   * maximum: 7
   * @return answerValue
   */
  @NotNull @Min(1) @Max(7) 
  @Schema(name = "answerValue", example = "5", description = "回答値（1〜7）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("answerValue")
  public Integer getAnswerValue() {
    return answerValue;
  }

  public void setAnswerValue(Integer answerValue) {
    this.answerValue = answerValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnswerItem answerItem = (AnswerItem) o;
    return Objects.equals(this.questionId, answerItem.questionId) &&
        Objects.equals(this.answerValue, answerItem.answerValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, answerValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnswerItem {\n");
    sb.append("    questionId: ").append(toIndentedString(questionId)).append("\n");
    sb.append("    answerValue: ").append(toIndentedString(answerValue)).append("\n");
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

