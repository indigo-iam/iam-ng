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

import static com.google.common.collect.Lists.newArrayList;
import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision.approve;
import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestOutcomeDTO.newOutcomeDto;
import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.done;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import java.security.Principal;
import java.time.Clock;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import it.infn.cnaf.sd.iam.api.apis.registrations.RegistrationRequestMapper;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecisionDTO;
import it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestOutcomeDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.kc.KeycloakClient;
import it.infn.cnaf.sd.iam.api.kc.KeycloakClientRepository;
import it.infn.cnaf.sd.iam.api.service.notification.NotificationFactory;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@Service
@Transactional
public class DefaultRequestsService implements RequestsService, KeycloakAttributes {

  public static final String APPROVED_TEMPLATE = "Request %s approved (User %s has been created)";
  public static final String REJECTED_TEMPLATE = "Request %s rejected";

  private final Clock clock;
  private final RegistrationRequestRepository repo;
  private final RegistrationRequestMapper mapper;
  private final KeycloakClientRepository kcRepo;
  private final NotificationFactory notificationFactory;


  @Autowired
  public DefaultRequestsService(Clock clock, RegistrationRequestRepository repo,
      RegistrationRequestMapper mapper, KeycloakClientRepository kcRepo, NotificationFactory nf) {
    this.clock = clock;
    this.repo = repo;
    this.mapper = mapper;
    this.kcRepo = kcRepo;
    this.notificationFactory = nf;
  }

  @Override
  public ListResponseDTO<RegistrationRequestDTO> getPendingRequests(Pageable pageable) {

    ListResponseDTO.Builder<RegistrationRequestDTO> result = ListResponseDTO.builder();

    Page<RegistrationRequestEntity> pendingRequests =
        repo.findByRealmNamePending(RealmContext.getCurrentRealmName(), pageable);

    result.fromPage(pendingRequests);

    result.resources(pendingRequests.get().map(mapper::entityToDto).collect(toList()));
    return result.build();
  }

  protected void requestStatusSanityChecks(RegistrationRequestEntity request) {
    if (done.equals(request.getStatus())) {
      throw new InvalidRequestError("Request already handled");
    }
  }


  protected UserRepresentation userFromRequest(RegistrationRequestEntity request) {

    UserRepresentation user = new UserRepresentation();

    user.setUsername(request.getRequesterInfo().getUsername());
    user.setEmail(request.getRequesterInfo().getEmail());
    user.setEmailVerified(true);
    user.setFirstName(request.getRequesterInfo().getGivenName());
    user.setLastName(request.getRequesterInfo().getFamilyName());
    user.setAttributes(Maps.newHashMap());
    user.getAttributes().put(REGISTRATION_REQUEST_ID, newArrayList(request.getUuid()));
    user.setEnabled(true);

    return user;
  }

  private RegistrationRequestEntity findRequest(String requestId) {
    return repo.findByRealmNameAndUuid(RealmContext.getCurrentRealmName(), requestId)
      .orElseThrow(requestNotFound(requestId));
  }

  @Override
  public RequestOutcomeDTO setRequestDecision(Principal principal, String requestId,
      RequestDecisionDTO decision) {

    RegistrationRequestEntity request = findRequest(requestId);
    requestStatusSanityChecks(request);

    if (isNotBlank(decision.getMessage())) {
      request.addMessage(clock, decision.getMessage(), principal.getName());
    }

    if (approve.equals(decision.getDecision())) {
      request.approve(clock);
      KeycloakClient kcClient = kcRepo.getKeycloakClient();
      UserRepresentation user = kcClient.createUser(userFromRequest(request));
      notificationFactory.createRegistrationApprovedMessage(request);
      return newOutcomeDto(format(APPROVED_TEMPLATE, request.getUuid(), user.getUsername()),
          mapper.entityToDto(request));
    } else {
      request.reject(clock);;
      notificationFactory.createRegistrationRejectedMessage(request);
      return newOutcomeDto(format(REJECTED_TEMPLATE, request.getUuid()),
          mapper.entityToDto(request));
    }
  }

}
