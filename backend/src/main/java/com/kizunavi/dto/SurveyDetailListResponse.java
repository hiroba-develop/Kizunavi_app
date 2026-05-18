package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.SurveyDetail;
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
 * SurveyDetailListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SurveyDetailListResponse {

  @Valid
  private List<@Valid SurveyDetail> surveys = new ArrayList<>();

  public SurveyDetailListResponse surveys(List<@Valid SurveyDetail> surveys) {
    this.surveys = surveys;
    return this;
  }

  public SurveyDetailListResponse addSurveysItem(SurveyDetail surveysItem) {
    if (this.surveys == null) {
      this.surveys = new ArrayList<>();
    }
    this.surveys.add(surveysItem);
    return this;
  }

  /**
   * Get surveys
   * @return surveys
   */
  @Valid 
  @Schema(name = "surveys", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveys")
  public List<@Valid SurveyDetail> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<@Valid SurveyDetail> surveys) {
    this.surveys = surveys;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurveyDetailListResponse surveyDetailListResponse = (SurveyDetailListResponse) o;
    return Objects.equals(this.surveys, surveyDetailListResponse.surveys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveys);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurveyDetailListResponse {\n");
    sb.append("    surveys: ").append(toIndentedString(surveys)).append("\n");
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

