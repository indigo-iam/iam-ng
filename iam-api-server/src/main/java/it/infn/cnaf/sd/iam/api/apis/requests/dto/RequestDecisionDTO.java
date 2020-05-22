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
package it.infn.cnaf.sd.iam.api.apis.requests.dto;

import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision.approve;
import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision.reject;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RequestDecisionDTO {

  public static final String MESSAGE_REGEXP = "(\\w?\\s?[.,:'\";!?]?)*$";
  
  RequestDecision decision;
  
  @Pattern(regexp = MESSAGE_REGEXP, message="invalid message")
  @Size(max = 512, message = "message too long")
  String message;

  public RequestDecision getDecision() {
    return decision;
  }

  public void setDecision(RequestDecision decision) {
    this.decision = decision;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static RequestDecisionDTO approve(String message) {
    RequestDecisionDTO dto  = new RequestDecisionDTO();
    dto.setDecision(approve);
    dto.setMessage(message);
    return dto;
  }
  
  public static RequestDecisionDTO reject(String message) {
    RequestDecisionDTO dto  = new RequestDecisionDTO();
    dto.setDecision(reject);
    dto.setMessage(message);
    return dto;
  }
  
  public static RequestDecisionDTO reject() {
    return reject(null);
  }
  
  public static RequestDecisionDTO approve() {
    return approve(null);
  }
}
