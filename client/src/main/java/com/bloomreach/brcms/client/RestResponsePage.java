package com.bloomreach.brcms.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponsePage<T> extends PageImpl<T> {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestResponsePage(
      @JsonProperty("content") final List<T> content,
      @JsonProperty("number") final int number,
      @JsonProperty("size") final int size,
      @JsonProperty("totalElements") final Long totalElements,
      @JsonProperty("pageable") final JsonNode pageable,
      @JsonProperty("last") final boolean last,
      @JsonProperty("totalPages") final int totalPages,
      @JsonProperty("sort") final JsonNode sort,
      @JsonProperty("first") final boolean first,
      @JsonProperty("numberOfElements") final int numberOfElements) {

    super(content, PageRequest.of(number, size), totalElements);
  }

  public RestResponsePage(final List<T> content, final Pageable pageable, final long total) {
    super(content, pageable, total);
  }

  public RestResponsePage(final List<T> content) {
    super(content);
  }

  public RestResponsePage() {
    super(new ArrayList<>());
  }
}
