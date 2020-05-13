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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.registrations.RegistrationRequestMapper;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestOutcomeDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;

@RestController
@RequestMapping(value = "/Realms/{realm}")
@Transactional
@PreAuthorize("hasRole('IAM_OWNER')")
public class RequestsController {

  public static final int PAGE_SIZE = 20;

  final RequestsService service;
  final RegistrationRequestMapper mapper;

  @Autowired
  public RequestsController(RequestsService service, RegistrationRequestMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping("/Requests/registration/pending")
  ListResponseDTO<RegistrationRequestDTO> getPendingRegistrationRequests(
      @RequestParam(required = false) final Integer count,
      @RequestParam(required = false) final Integer startIndex) {

    PageRequest pageRequest = PageUtils.buildPageRequest(count, startIndex, PAGE_SIZE,
        Sort.by("metadata.creationTime").descending());

    return service.getPendingRequests(pageRequest);
  }

  @PostMapping("/Requests/registration/{requestId}")
  RequestOutcomeDTO approveRequest(@PathVariable final String requestId,
      @RequestParam final RequestDecision decision) {
    return service.setRequestDecision(requestId, decision);
  }
  
}
