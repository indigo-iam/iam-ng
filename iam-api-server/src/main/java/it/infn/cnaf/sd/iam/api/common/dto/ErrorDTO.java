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

import org.springframework.http.HttpStatus;

public class ErrorDTO {
  
  final String error;
  final String errorDescription;
  final String errorDetail;

  private ErrorDTO(String error, String errorDescription, String detail) {
    this.error = error;
    this.errorDescription = errorDescription;
    this.errorDetail = detail;
  }
  
  private ErrorDTO(String error) {
    this(error,null, null);
  }

  public String getError() {
    return error;
  }
  
  public String getErrorDescription() {
    return errorDescription;
  }
  
  public String getErrorDetail() {
    return errorDetail;
  }
  
  public static ErrorDTO newError(HttpStatus s, String errorDescription) {
    return new ErrorDTO(s.name().toLowerCase(), errorDescription, null);
  }
  
  public static ErrorDTO newError(HttpStatus s, String errorDescription, String detail) {
    return new ErrorDTO(s.name().toLowerCase(), errorDescription, detail);
  }
  
 
}
