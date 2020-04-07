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
package it.infn.cnaf.sd.iam.api.apis.registrations;

import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.created;
import static java.util.Objects.isNull;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestAttachmentEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@Service
public class DefaultRegistrationService
    implements RegistrationService, RegistrationSupport, ErrorUtils {

  private final Clock clock;
  private final RegistrationRequestRepository requestRepo;


  public DefaultRegistrationService(RegistrationRequestRepository requestRepo, Clock clock) {
    this.requestRepo = requestRepo;
    this.clock = clock;
  }


  @Override
  public RegistrationRequestEntity createRegistrationRequest(RegistrationRequestEntity request,
      Authentication authentication) {

    Instant creationTime = clock.instant();

    request.setMetadata(MetadataEntity.fromInstant(creationTime));
    request.setUuid(UUID.randomUUID().toString());
    request.setEmailChallenge(UUID.randomUUID().toString());
    request.setRequestChallenge(UUID.randomUUID().toString());
    request.setRealm(RealmContext.getCurrentRealmEntity());
    request.setStatus(created);

    request.getMessages().stream().forEach(m -> m.setCreationTime(Date.from(creationTime)));

    if (!isNull(authentication) && authentication instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;
      RegistrationRequestAttachmentEntity attachment = new RegistrationRequestAttachmentEntity();
      attachment.setMetadata(MetadataEntity.fromInstant(creationTime));
      attachment.setLabel(AUTHENTICATION_ATTACHMENT_LABEL);
      attachment.setContent(authToken.getToken().getTokenValue());

      attachment.setRequest(request);
      request.getAttachments().add(attachment);

    }

    return requestRepo.save(request);
  }


  @Override
  public RegistrationRequestEntity confirmRegistrationRequest(String emailChallenge) {

    RegistrationRequestEntity request = requestRepo
      .findByRealmNameAndEmailChallenge(RealmContext.getCurrentRealmName(), emailChallenge)
      .orElseThrow(requestNotFoundForEmailChallenge(emailChallenge));

    if (!request.getStatus().equals(created)) {
      throw new InvalidRequestError("Request already confirmed");
    }

    if (!emailChallenge.equals(request.getEmailChallenge())) {
      throw new InvalidRequestError("Invalid request confirmation token");
    }

    request.setEmailChallenge(UUID.randomUUID().toString());
    request.setStatus(RegistrationRequestStatus.confirmed);
    request.getMetadata().setLastUpdateTime(Date.from(clock.instant()));
    
    return requestRepo.save(request);
  }



}
