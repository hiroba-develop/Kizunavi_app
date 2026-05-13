package com.kizunavi.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.kizunavi.dto.Pageable;
import com.kizunavi.dto.Sort;
import com.kizunavi.dto.UserResponse;
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
 * ページネーション付きユーザー一覧レスポンス（Spring Data Page形式）
 */

@Schema(name = "PageUserResponse", description = "ページネーション付きユーザー一覧レスポンス（Spring Data Page形式）")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PageUserResponse {

  @Valid
  private List<@Valid UserResponse> content = new ArrayList<>();

  private @Nullable Pageable pageable;

  private @Nullable Long totalElements;

  private @Nullable Integer totalPages;

  private @Nullable Integer size;

  private @Nullable Integer number;

  private @Nullable Sort sort;

  private @Nullable Boolean first;

  private @Nullable Boolean last;

  private @Nullable Integer numberOfElements;

  private @Nullable Boolean empty;

  public PageUserResponse content(List<@Valid UserResponse> content) {
    this.content = content;
    return this;
  }

  public PageUserResponse addContentItem(UserResponse contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

  /**
   * ユーザー一覧
   * @return content
   */
  @Valid 
  @Schema(name = "content", description = "ユーザー一覧", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public List<@Valid UserResponse> getContent() {
    return content;
  }

  public void setContent(List<@Valid UserResponse> content) {
    this.content = content;
  }

  public PageUserResponse pageable(Pageable pageable) {
    this.pageable = pageable;
    return this;
  }

  /**
   * Get pageable
   * @return pageable
   */
  @Valid 
  @Schema(name = "pageable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pageable")
  public Pageable getPageable() {
    return pageable;
  }

  public void setPageable(Pageable pageable) {
    this.pageable = pageable;
  }

  public PageUserResponse totalElements(Long totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * 全件数
   * @return totalElements
   */
  
  @Schema(name = "totalElements", example = "100", description = "全件数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalElements")
  public Long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  public PageUserResponse totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * 総ページ数
   * @return totalPages
   */
  
  @Schema(name = "totalPages", example = "5", description = "総ページ数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public PageUserResponse size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * 1ページあたりの件数
   * @return size
   */
  
  @Schema(name = "size", example = "20", description = "1ページあたりの件数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public PageUserResponse number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * 現在のページ番号（0始まり）
   * @return number
   */
  
  @Schema(name = "number", example = "0", description = "現在のページ番号（0始まり）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("number")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PageUserResponse sort(Sort sort) {
    this.sort = sort;
    return this;
  }

  /**
   * Get sort
   * @return sort
   */
  @Valid 
  @Schema(name = "sort", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sort")
  public Sort getSort() {
    return sort;
  }

  public void setSort(Sort sort) {
    this.sort = sort;
  }

  public PageUserResponse first(Boolean first) {
    this.first = first;
    return this;
  }

  /**
   * 最初のページかどうか
   * @return first
   */
  
  @Schema(name = "first", example = "true", description = "最初のページかどうか", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first")
  public Boolean getFirst() {
    return first;
  }

  public void setFirst(Boolean first) {
    this.first = first;
  }

  public PageUserResponse last(Boolean last) {
    this.last = last;
    return this;
  }

  /**
   * 最後のページかどうか
   * @return last
   */
  
  @Schema(name = "last", example = "false", description = "最後のページかどうか", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last")
  public Boolean getLast() {
    return last;
  }

  public void setLast(Boolean last) {
    this.last = last;
  }

  public PageUserResponse numberOfElements(Integer numberOfElements) {
    this.numberOfElements = numberOfElements;
    return this;
  }

  /**
   * 現在のページの要素数
   * @return numberOfElements
   */
  
  @Schema(name = "numberOfElements", example = "20", description = "現在のページの要素数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("numberOfElements")
  public Integer getNumberOfElements() {
    return numberOfElements;
  }

  public void setNumberOfElements(Integer numberOfElements) {
    this.numberOfElements = numberOfElements;
  }

  public PageUserResponse empty(Boolean empty) {
    this.empty = empty;
    return this;
  }

  /**
   * 空かどうか
   * @return empty
   */
  
  @Schema(name = "empty", example = "false", description = "空かどうか", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("empty")
  public Boolean getEmpty() {
    return empty;
  }

  public void setEmpty(Boolean empty) {
    this.empty = empty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageUserResponse pageUserResponse = (PageUserResponse) o;
    return Objects.equals(this.content, pageUserResponse.content) &&
        Objects.equals(this.pageable, pageUserResponse.pageable) &&
        Objects.equals(this.totalElements, pageUserResponse.totalElements) &&
        Objects.equals(this.totalPages, pageUserResponse.totalPages) &&
        Objects.equals(this.size, pageUserResponse.size) &&
        Objects.equals(this.number, pageUserResponse.number) &&
        Objects.equals(this.sort, pageUserResponse.sort) &&
        Objects.equals(this.first, pageUserResponse.first) &&
        Objects.equals(this.last, pageUserResponse.last) &&
        Objects.equals(this.numberOfElements, pageUserResponse.numberOfElements) &&
        Objects.equals(this.empty, pageUserResponse.empty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, pageable, totalElements, totalPages, size, number, sort, first, last, numberOfElements, empty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageUserResponse {\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    pageable: ").append(toIndentedString(pageable)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
    sb.append("    first: ").append(toIndentedString(first)).append("\n");
    sb.append("    last: ").append(toIndentedString(last)).append("\n");
    sb.append("    numberOfElements: ").append(toIndentedString(numberOfElements)).append("\n");
    sb.append("    empty: ").append(toIndentedString(empty)).append("\n");
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

