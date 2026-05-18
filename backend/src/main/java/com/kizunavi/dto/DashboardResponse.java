package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.DashboardSummary;
import com.kizunavi.dto.EmployeeValue;
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
 * DashboardResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DashboardResponse {

  @Valid
  private List<@Valid SurveySummary> surveys = new ArrayList<>();

  private SurveySummary survey;

  private DashboardSummary summary;

  @Valid
  private List<@Valid EmployeeValue> values = new ArrayList<>();

  public DashboardResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DashboardResponse(List<@Valid SurveySummary> surveys, SurveySummary survey, DashboardSummary summary, List<@Valid EmployeeValue> values) {
    this.surveys = surveys;
    this.survey = survey;
    this.summary = summary;
    this.values = values;
  }

  public DashboardResponse surveys(List<@Valid SurveySummary> surveys) {
    this.surveys = surveys;
    return this;
  }

  public DashboardResponse addSurveysItem(SurveySummary surveysItem) {
    if (this.surveys == null) {
      this.surveys = new ArrayList<>();
    }
    this.surveys.add(surveysItem);
    return this;
  }

  /**
   * 全サーベイ一覧（サイドバー・ドロップダウン用）
   * @return surveys
   */
  @NotNull @Valid 
  @Schema(name = "surveys", description = "全サーベイ一覧（サイドバー・ドロップダウン用）", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("surveys")
  public List<@Valid SurveySummary> getSurveys() {
    return surveys;
  }

  public void setSurveys(List<@Valid SurveySummary> surveys) {
    this.surveys = surveys;
  }

  public DashboardResponse survey(SurveySummary survey) {
    this.survey = survey;
    return this;
  }

  /**
   * Get survey
   * @return survey
   */
  @NotNull @Valid 
  @Schema(name = "survey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("survey")
  public SurveySummary getSurvey() {
    return survey;
  }

  public void setSurvey(SurveySummary survey) {
    this.survey = survey;
  }

  public DashboardResponse summary(DashboardSummary summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @NotNull @Valid 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("summary")
  public DashboardSummary getSummary() {
    return summary;
  }

  public void setSummary(DashboardSummary summary) {
    this.summary = summary;
  }

  public DashboardResponse values(List<@Valid EmployeeValue> values) {
    this.values = values;
    return this;
  }

  public DashboardResponse addValuesItem(EmployeeValue valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<>();
    }
    this.values.add(valuesItem);
    return this;
  }

  /**
   * 従業員バリュー分布
   * @return values
   */
  @NotNull @Valid 
  @Schema(name = "values", description = "従業員バリュー分布", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("values")
  public List<@Valid EmployeeValue> getValues() {
    return values;
  }

  public void setValues(List<@Valid EmployeeValue> values) {
    this.values = values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DashboardResponse dashboardResponse = (DashboardResponse) o;
    return Objects.equals(this.surveys, dashboardResponse.surveys) &&
        Objects.equals(this.survey, dashboardResponse.survey) &&
        Objects.equals(this.summary, dashboardResponse.summary) &&
        Objects.equals(this.values, dashboardResponse.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveys, survey, summary, values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DashboardResponse {\n");
    sb.append("    surveys: ").append(toIndentedString(surveys)).append("\n");
    sb.append("    survey: ").append(toIndentedString(survey)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

