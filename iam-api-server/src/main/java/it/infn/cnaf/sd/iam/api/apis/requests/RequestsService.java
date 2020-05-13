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
package it.infn.cnaf.sd.iam.api.apis.requests;

import java.util.function.Supplier;

import org.springframework.data.domain.Pageable;

import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestOutcomeDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;

public interface RequestsService {

  String NOT_FOUND_TEMPLATE = "Request not found for id: %s";
  default Supplier<NotFoundError> requestNotFound(String requestId) {
    return () -> new NotFoundError(String.format(NOT_FOUND_TEMPLATE, requestId));
  }

  ListResponseDTO<RegistrationRequestDTO> getPendingRequests(Pageable pageable);
  
  RequestOutcomeDTO setRequestDecision(String requestId, RequestDecision decision);

  
}
