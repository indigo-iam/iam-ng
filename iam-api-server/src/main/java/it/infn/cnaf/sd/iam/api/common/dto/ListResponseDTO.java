/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.api.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public class ListResponseDTO<T> {

  private final String kind;

  private final long totalResults;
  private final int itemsPerPage;
  private final int startIndex;

  private final List<T> resources;

  private ListResponseDTO(Builder<T> builder) {
    this.kind = builder.kind;
    this.totalResults = builder.totalResults;
    this.startIndex = builder.startIndex;
    this.itemsPerPage = builder.itemsPerPage;
    this.resources = builder.resources;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public List<T> getResources() {
    return resources;
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public String getKind() {
    return kind;
  }

  public static class Builder<T> {
    private String kind;
    private long totalResults;
    private int itemsPerPage;
    private int startIndex;
    private List<T> resources;

    public <S> Builder<T> fromPage(Page<S> page) {
      this.totalResults = page.getTotalElements();
      this.itemsPerPage = page.getSize();
      this.startIndex = page.getNumber();
      return this;
    }

    public Builder<T> totalResults(long totalResults) {
      this.totalResults = totalResults;
      return this;
    }

    public Builder<T> itemsPerPage(int itemsPerPage) {
      this.itemsPerPage = itemsPerPage;
      return this;
    }

    public Builder<T> startIndex(int startIndex) {
      this.startIndex = startIndex;
      return this;
    }

    public Builder<T> resources(List<T> resources) {
      this.resources = resources;
      return this;
    }

    public Builder<T> kind(String kind) {
      this.kind = kind;
      return this;
    }

    public ListResponseDTO<T> build() {
      return new ListResponseDTO<>(this);
    }
  }
}
