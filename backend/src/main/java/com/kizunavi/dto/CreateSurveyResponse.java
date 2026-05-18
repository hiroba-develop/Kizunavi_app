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
 * CreateSurveyResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSurveyResponse {

  private @Nullable String responseStatus;

  private @Nullable UUID surveyId;

  public CreateSurveyResponse responseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
    return this;
  }

  /**
   * Get responseStatus
   * @return responseStatus
   */
  
  @Schema(name = "responseStatus", example = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responseStatus")
  public String getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(String responseStatus) {
    this.responseStatus = responseStatus;
  }

  public CreateSurveyResponse surveyId(UUID surveyId) {
    this.surveyId = surveyId;
    return this;
  }

  /**
   * 作成されたサーベイID
   * @return surveyId
   */
  @Valid 
  @Schema(name = "surveyId", example = "550e8400-e29b-41d4-a716-446655440004", description = "作成されたサーベイID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveyId")
  public UUID getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(UUID surveyId) {
    this.surveyId = surveyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSurveyResponse createSurveyResponse = (CreateSurveyResponse) o;
    return Objects.equals(this.responseStatus, createSurveyResponse.responseStatus) &&
        Objects.equals(this.surveyId, createSurveyResponse.surveyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseStatus, surveyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSurveyResponse {\n");
    sb.append("    responseStatus: ").append(toIndentedString(responseStatus)).append("\n");
    sb.append("    surveyId: ").append(toIndentedString(surveyId)).append("\n");
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

