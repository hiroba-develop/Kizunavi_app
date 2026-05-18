package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * DashboardSummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DashboardSummary {

  private @Nullable BigDecimal kizunaScore;

  private @Nullable BigDecimal kizunaChangeScore;

  private @Nullable BigDecimal roleExpectationScore;

  private @Nullable BigDecimal engagementScore;

  private @Nullable BigDecimal climateScore;

  private @Nullable BigDecimal accuracyScore;

  private @Nullable Integer condition;

  @Valid
  private List<Object> alertTop10 = new ArrayList<>();

  public DashboardSummary kizunaScore(BigDecimal kizunaScore) {
    this.kizunaScore = kizunaScore;
    return this;
  }

  /**
   * Get kizunaScore
   * @return kizunaScore
   */
  @Valid 
  @Schema(name = "kizunaScore", example = "64.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaScore")
  public BigDecimal getKizunaScore() {
    return kizunaScore;
  }

  public void setKizunaScore(BigDecimal kizunaScore) {
    this.kizunaScore = kizunaScore;
  }

  public DashboardSummary kizunaChangeScore(BigDecimal kizunaChangeScore) {
    this.kizunaChangeScore = kizunaChangeScore;
    return this;
  }

  /**
   * Get kizunaChangeScore
   * @return kizunaChangeScore
   */
  @Valid 
  @Schema(name = "kizunaChangeScore", example = "43.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaChangeScore")
  public BigDecimal getKizunaChangeScore() {
    return kizunaChangeScore;
  }

  public void setKizunaChangeScore(BigDecimal kizunaChangeScore) {
    this.kizunaChangeScore = kizunaChangeScore;
  }

  public DashboardSummary roleExpectationScore(BigDecimal roleExpectationScore) {
    this.roleExpectationScore = roleExpectationScore;
    return this;
  }

  /**
   * Get roleExpectationScore
   * @return roleExpectationScore
   */
  @Valid 
  @Schema(name = "roleExpectationScore", example = "66.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleExpectationScore")
  public BigDecimal getRoleExpectationScore() {
    return roleExpectationScore;
  }

  public void setRoleExpectationScore(BigDecimal roleExpectationScore) {
    this.roleExpectationScore = roleExpectationScore;
  }

  public DashboardSummary engagementScore(BigDecimal engagementScore) {
    this.engagementScore = engagementScore;
    return this;
  }

  /**
   * Get engagementScore
   * @return engagementScore
   */
  @Valid 
  @Schema(name = "engagementScore", example = "74.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("engagementScore")
  public BigDecimal getEngagementScore() {
    return engagementScore;
  }

  public void setEngagementScore(BigDecimal engagementScore) {
    this.engagementScore = engagementScore;
  }

  public DashboardSummary climateScore(BigDecimal climateScore) {
    this.climateScore = climateScore;
    return this;
  }

  /**
   * Get climateScore
   * @return climateScore
   */
  @Valid 
  @Schema(name = "climateScore", example = "80.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("climateScore")
  public BigDecimal getClimateScore() {
    return climateScore;
  }

  public void setClimateScore(BigDecimal climateScore) {
    this.climateScore = climateScore;
  }

  public DashboardSummary accuracyScore(BigDecimal accuracyScore) {
    this.accuracyScore = accuracyScore;
    return this;
  }

  /**
   * Get accuracyScore
   * @return accuracyScore
   */
  @Valid 
  @Schema(name = "accuracyScore", example = "73.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accuracyScore")
  public BigDecimal getAccuracyScore() {
    return accuracyScore;
  }

  public void setAccuracyScore(BigDecimal accuracyScore) {
    this.accuracyScore = accuracyScore;
  }

  public DashboardSummary condition(Integer condition) {
    this.condition = condition;
    return this;
  }

  /**
   * 1=良好 2=普通 3=要注意
   * @return condition
   */
  
  @Schema(name = "condition", example = "2", description = "1=良好 2=普通 3=要注意", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition")
  public Integer getCondition() {
    return condition;
  }

  public void setCondition(Integer condition) {
    this.condition = condition;
  }

  public DashboardSummary alertTop10(List<Object> alertTop10) {
    this.alertTop10 = alertTop10;
    return this;
  }

  public DashboardSummary addAlertTop10Item(Object alertTop10Item) {
    if (this.alertTop10 == null) {
      this.alertTop10 = new ArrayList<>();
    }
    this.alertTop10.add(alertTop10Item);
    return this;
  }

  /**
   * アラート上位10件（DASHBOARD_SUMMARIES.alertTop10）。 ※要素の shape は別途設計予定。現時点では未定義のため any 扱い。 フロント・バックともに実装前に型定義の合意を取ること。 
   * @return alertTop10
   */
  
  @Schema(name = "alertTop10", description = "アラート上位10件（DASHBOARD_SUMMARIES.alertTop10）。 ※要素の shape は別途設計予定。現時点では未定義のため any 扱い。 フロント・バックともに実装前に型定義の合意を取ること。 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alertTop10")
  public List<Object> getAlertTop10() {
    return alertTop10;
  }

  public void setAlertTop10(List<Object> alertTop10) {
    this.alertTop10 = alertTop10;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DashboardSummary dashboardSummary = (DashboardSummary) o;
    return Objects.equals(this.kizunaScore, dashboardSummary.kizunaScore) &&
        Objects.equals(this.kizunaChangeScore, dashboardSummary.kizunaChangeScore) &&
        Objects.equals(this.roleExpectationScore, dashboardSummary.roleExpectationScore) &&
        Objects.equals(this.engagementScore, dashboardSummary.engagementScore) &&
        Objects.equals(this.climateScore, dashboardSummary.climateScore) &&
        Objects.equals(this.accuracyScore, dashboardSummary.accuracyScore) &&
        Objects.equals(this.condition, dashboardSummary.condition) &&
        Objects.equals(this.alertTop10, dashboardSummary.alertTop10);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kizunaScore, kizunaChangeScore, roleExpectationScore, engagementScore, climateScore, accuracyScore, condition, alertTop10);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DashboardSummary {\n");
    sb.append("    kizunaScore: ").append(toIndentedString(kizunaScore)).append("\n");
    sb.append("    kizunaChangeScore: ").append(toIndentedString(kizunaChangeScore)).append("\n");
    sb.append("    roleExpectationScore: ").append(toIndentedString(roleExpectationScore)).append("\n");
    sb.append("    engagementScore: ").append(toIndentedString(engagementScore)).append("\n");
    sb.append("    climateScore: ").append(toIndentedString(climateScore)).append("\n");
    sb.append("    accuracyScore: ").append(toIndentedString(accuracyScore)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    alertTop10: ").append(toIndentedString(alertTop10)).append("\n");
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

