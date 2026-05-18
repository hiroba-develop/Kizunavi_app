package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.AnswerItem;
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
 * SubmitAnswerRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SubmitAnswerRequest {

  @Valid
  private List<@Valid AnswerItem> answers = new ArrayList<>();

  public SubmitAnswerRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubmitAnswerRequest(List<@Valid AnswerItem> answers) {
    this.answers = answers;
  }

  public SubmitAnswerRequest answers(List<@Valid AnswerItem> answers) {
    this.answers = answers;
    return this;
  }

  public SubmitAnswerRequest addAnswersItem(AnswerItem answersItem) {
    if (this.answers == null) {
      this.answers = new ArrayList<>();
    }
    this.answers.add(answersItem);
    return this;
  }

  /**
   * Get answers
   * @return answers
   */
  @NotNull @Valid 
  @Schema(name = "answers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("answers")
  public List<@Valid AnswerItem> getAnswers() {
    return answers;
  }

  public void setAnswers(List<@Valid AnswerItem> answers) {
    this.answers = answers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmitAnswerRequest submitAnswerRequest = (SubmitAnswerRequest) o;
    return Objects.equals(this.answers, submitAnswerRequest.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmitAnswerRequest {\n");
    sb.append("    answers: ").append(toIndentedString(answers)).append("\n");
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

