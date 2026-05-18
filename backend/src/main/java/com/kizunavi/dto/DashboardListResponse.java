package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.SurveySummary;
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
 * DashboardListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DashboardListResponse {

  @Valid
  private List<@Valid SurveySummary> surveys = new ArrayList<>();

  public DashboardListResponse surveys(List<@Valid SurveySummary> surveys) {
    this.surveys = surveys;
    return this;
  }

  public DashboardListResponse addSurveysItem(SurveySummary surveysItem) {
    if (this.surveys == null) {
      this.surveys = new ArrayList<>();
    }
    this.surveys.add(surveysItem);
    return this;
  }

  /**
   * 全サーベイ一覧
   * @return surveys
   */
  @Valid 
  @Schema(name = "surveys", description = "全サーベイ一覧", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("surveys")
  public List<@Valid SurveySummary> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<@Valid SurveySummary> surveys) {
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
    DashboardListResponse dashboardListResponse = (DashboardListResponse) o;
    return Objects.equals(this.surveys, dashboardListResponse.surveys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveys);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DashboardListResponse {\n");
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

