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

import org.springframework.http.HttpStatus;

public class ValidationErrorDTO extends ErrorDTO {

  List<String> globalErrors;
  List<FieldErrorDTO> fieldErrors;

  public ValidationErrorDTO(String errorDescription) {
    super(HttpStatus.BAD_REQUEST.name().toLowerCase(), errorDescription, null);
  }

  public List<String> getGlobalErrors() {
    return globalErrors;
  }

  public void setGlobalErrors(List<String> globalErrors) {
    this.globalErrors = globalErrors;
  }

  public List<FieldErrorDTO> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

}
