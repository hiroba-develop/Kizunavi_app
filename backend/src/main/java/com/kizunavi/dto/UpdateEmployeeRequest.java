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
 * UpdateEmployeeRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateEmployeeRequest {

  private JsonNullable<@Size(max = 100) String> displayName = JsonNullable.<String>undefined();

  private JsonNullable<@jakarta.validation.constraints.Email String> email = JsonNullable.<String>undefined();

  private @Nullable Role role;

  private JsonNullable<@Min(1) @Max(5) Integer> kizunaLevel = JsonNullable.<Integer>undefined();

  private JsonNullable<UUID> divisionId = JsonNullable.<UUID>undefined();

  private JsonNullable<UUID> sectionId = JsonNullable.<UUID>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private JsonNullable<LocalDate> hireDate = JsonNullable.<LocalDate>undefined();

  public UpdateEmployeeRequest displayName(String displayName) {
    this.displayName = JsonNullable.of(displayName);
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  @Size(max = 100) 
  @Schema(name = "displayName", example = "山田太郎", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public JsonNullable<@Size(max = 100) String> getDisplayName() {
    return displayName;
  }

  public void setDisplayName(JsonNullable<String> displayName) {
    this.displayName = displayName;
  }

  public UpdateEmployeeRequest email(String email) {
    this.email = JsonNullable.of(email);
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "yamada.taro@example.co.jp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public JsonNullable<@jakarta.validation.constraints.Email String> getEmail() {
    return email;
  }

  public void setEmail(JsonNullable<String> email) {
    this.email = email;
  }

  public UpdateEmployeeRequest role(Role role) {
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

  public UpdateEmployeeRequest kizunaLevel(Integer kizunaLevel) {
    this.kizunaLevel = JsonNullable.of(kizunaLevel);
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
  public JsonNullable<@Min(1) @Max(5) Integer> getKizunaLevel() {
    return kizunaLevel;
  }

  public void setKizunaLevel(JsonNullable<Integer> kizunaLevel) {
    this.kizunaLevel = kizunaLevel;
  }

  public UpdateEmployeeRequest divisionId(UUID divisionId) {
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

  public UpdateEmployeeRequest sectionId(UUID sectionId) {
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

  public UpdateEmployeeRequest hireDate(LocalDate hireDate) {
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
    UpdateEmployeeRequest updateEmployeeRequest = (UpdateEmployeeRequest) o;
    return equalsNullable(this.displayName, updateEmployeeRequest.displayName) &&
        equalsNullable(this.email, updateEmployeeRequest.email) &&
        Objects.equals(this.role, updateEmployeeRequest.role) &&
        equalsNullable(this.kizunaLevel, updateEmployeeRequest.kizunaLevel) &&
        equalsNullable(this.divisionId, updateEmployeeRequest.divisionId) &&
        equalsNullable(this.sectionId, updateEmployeeRequest.sectionId) &&
        equalsNullable(this.hireDate, updateEmployeeRequest.hireDate);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(displayName), hashCodeNullable(email), role, hashCodeNullable(kizunaLevel), hashCodeNullable(divisionId), hashCodeNullable(sectionId), hashCodeNullable(hireDate));
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
    sb.append("class UpdateEmployeeRequest {\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    kizunaLevel: ").append(toIndentedString(kizunaLevel)).append("\n");
    sb.append("    divisionId: ").append(toIndentedString(divisionId)).append("\n");
    sb.append("    sectionId: ").append(toIndentedString(sectionId)).append("\n");
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

