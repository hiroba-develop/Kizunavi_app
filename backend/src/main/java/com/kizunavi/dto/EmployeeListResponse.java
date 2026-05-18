package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.Employee;
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
 * EmployeeListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EmployeeListResponse {

  @Valid
  private List<@Valid Employee> employees = new ArrayList<>();

  public EmployeeListResponse employees(List<@Valid Employee> employees) {
    this.employees = employees;
    return this;
  }

  public EmployeeListResponse addEmployeesItem(Employee employeesItem) {
    if (this.employees == null) {
      this.employees = new ArrayList<>();
    }
    this.employees.add(employeesItem);
    return this;
  }

  /**
   * Get employees
   * @return employees
   */
  @Valid 
  @Schema(name = "employees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("employees")
  public List<@Valid Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<@Valid Employee> employees) {
    this.employees = employees;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeListResponse employeeListResponse = (EmployeeListResponse) o;
    return Objects.equals(this.employees, employeeListResponse.employees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmployeeListResponse {\n");
    sb.append("    employees: ").append(toIndentedString(employees)).append("\n");
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

