package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kizunavi.dto.Role;
import java.time.LocalDate;
import java.util.Arrays;
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
 * Employee
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Employee {

  private @Nullable UUID employeeId;

  private @Nullable String displayName;

  private @Nullable String email;

  private @Nullable Role role;

  private JsonNullable<UUID> divisionId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> divisionName = JsonNullable.<String>undefined();

  private JsonNullable<UUID> sectionId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> sectionName = JsonNullable.<String>undefined();

  private @Nullable Integer kizunaLevel;

  private @Nullable String roleLabel;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<LocalDate> hireDate = JsonNullable.<LocalDate>undefined();

  public Employee employeeId(UUID employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * Get employeeId
   * @return employeeId
   */
  @Valid 
  @Schema(name = "employeeId", example = "550e8400-e29b-41d4-a716-446655440001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employeeId")
  public UUID getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(UUID employeeId) {
    this.employeeId = employeeId;
  }

  public Employee displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", example = "山田太郎", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Employee email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "yamada.taro@example.co.jp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Employee role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @Valid 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Employee divisionId(UUID divisionId) {
    this.divisionId = JsonNullable.of(divisionId);
    return this;
  }

  /**
   * Get divisionId
   * @return divisionId
   */
  @Valid 
  @Schema(name = "divisionId", example = "550e8400-e29b-41d4-a716-446655440002", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionId")
  public JsonNullable<UUID> getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(JsonNullable<UUID> divisionId) {
    this.divisionId = divisionId;
  }

  public Employee divisionName(String divisionName) {
    this.divisionName = JsonNullable.of(divisionName);
    return this;
  }

  /**
   * Get divisionName
   * @return divisionName
   */
  
  @Schema(name = "divisionName", example = "営業部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisionName")
  public JsonNullable<String> getDivisionName() {
    return divisionName;
  }

  public void setDivisionName(JsonNullable<String> divisionName) {
    this.divisionName = divisionName;
  }

  public Employee sectionId(UUID sectionId) {
    this.sectionId = JsonNullable.of(sectionId);
    return this;
  }

  /**
   * Get sectionId
   * @return sectionId
   */
  @Valid 
  @Schema(name = "sectionId", example = "550e8400-e29b-41d4-a716-446655440003", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sectionId")
  public JsonNullable<UUID> getSectionId() {
    return sectionId;
  }

  public void setSectionId(JsonNullable<UUID> sectionId) {
    this.sectionId = sectionId;
  }

  public Employee sectionName(String sectionName) {
    this.sectionName = JsonNullable.of(sectionName);
    return this;
  }

  /**
   * Get sectionName
   * @return sectionName
   */
  
  @Schema(name = "sectionName", example = "第一営業課", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sectionName")
  public JsonNullable<String> getSectionName() {
    return sectionName;
  }

  public void setSectionName(JsonNullable<String> sectionName) {
    this.sectionName = sectionName;
  }

  public Employee kizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
    return this;
  }

  /**
   * Get kizunaLevel
   * minimum: 1
   * maximum: 5
   * @return kizunaLevel
   */
  @Min(1) @Max(5) 
  @Schema(name = "kizunaLevel", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kizunaLevel")
  public Integer getKizunaLevel() {
    return kizunaLevel;
  }

  public void setKizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
  }

  public Employee roleLabel(String roleLabel) {
    this.roleLabel = roleLabel;
    return this;
  }

  /**
   * Get roleLabel
   * @return roleLabel
   */
  
  @Schema(name = "roleLabel", example = "部長", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleLabel")
  public String getRoleLabel() {
    return roleLabel;
  }

  public void setRoleLabel(String roleLabel) {
    this.roleLabel = roleLabel;
  }

  public Employee hireDate(LocalDate hireDate) {
    this.hireDate = JsonNullable.of(hireDate);
    return this;
  }

  /**
   * Get hireDate
   * @return hireDate
   */
  @Valid 
  @Schema(name = "hireDate", example = "2020-04-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hireDate")
  public JsonNullable<LocalDate> getHireDate() {
    return hireDate;
  }

  public void setHireDate(JsonNullable<LocalDate> hireDate) {
    this.hireDate = hireDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee employee = (Employee) o;
    return Objects.equals(this.employeeId, employee.employeeId) &&
        Objects.equals(this.displayName, employee.displayName) &&
        Objects.equals(this.email, employee.email) &&
        Objects.equals(this.role, employee.role) &&
        equalsNullable(this.divisionId, employee.divisionId) &&
        equalsNullable(this.divisionName, employee.divisionName) &&
        equalsNullable(this.sectionId, employee.sectionId) &&
        equalsNullable(this.sectionName, employee.sectionName) &&
        Objects.equals(this.kizunaLevel, employee.kizunaLevel) &&
        Objects.equals(this.roleLabel, employee.roleLabel) &&
        equalsNullable(this.hireDate, employee.hireDate);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, displayName, email, role, hashCodeNullable(divisionId), hashCodeNullable(divisionName), hashCodeNullable(sectionId), hashCodeNullable(sectionName), kizunaLevel, roleLabel, hashCodeNullable(hireDate));
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
    sb.append("class Employee {\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
    sb.append("    divisionName: ").append(toIndentedString(divisionName)).append("\n");
    sb.append("    sectionId: ").append(toIndentedString(sectionId)).append("\n");
    sb.append("    sectionName: ").append(toIndentedString(sectionName)).append("\n");
    sb.append("    kizunaLevel: ").append(toIndentedString(kizunaLevel)).append("\n");
    sb.append("    roleLabel: ").append(toIndentedString(roleLabel)).append("\n");
    sb.append("    hireDate: ").append(toIndentedString(hireDate)).append("\n");
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

