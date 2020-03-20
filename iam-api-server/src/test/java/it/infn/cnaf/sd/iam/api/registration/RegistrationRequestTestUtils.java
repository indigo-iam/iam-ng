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
package it.infn.cnaf.sd.iam.api.registration;

import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.created;
import static java.lang.String.format;

import java.time.Clock;
import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.domain.Sort;

import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RequestMessageEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RequesterInfoEntity;

public interface RegistrationRequestTestUtils {

  public static final String REALM_ALICE = "alice";
  public static final String REALM_CMS = "cms";
  public static final Sort SORT_BY_CREATION_TIME = Sort.by("metadata.creationTime").ascending();

  public static final String ERROR_REALM_NOT_FOUND_TEMPLATE = 
      "Inconsistent db: expected realm not found: %s";
  
  public static final String ERROR_EXPECTED_REQUEST_NOT_FOUND =
      "Inconsistent db: expected request not found";

  public static final String U0_GIVEN_NAME = "Name0";
  public static final String U0_FAMILY_NAME = "FamilyName0";
  public static final String U0_USERNAME = "user0";
  public static final String U0_EMAIL = "user0@test.example.com";
  public static final String U0_MESSAGE = "Yo from user 0!";

  public static final String GIVEN_NAME_TEMPLATE = "Name%d";
  public static final String FAMILY_NAME_TEMPLATE = "FamilyName%d";
  public static final String USERNAME_TEMPLATE = "user%d";
  public static final String EMAIL_TEMPLATE = "user%d@test.example.com";
  public static final String MESSAGE_TEMPLATE = "Yo from user %d!";
  
  default RegistrationRequestEntity newTemplateRequest(Clock clock, RealmEntity realm, String uuid, int id) {
    RegistrationRequestEntity r = new RegistrationRequestEntity();
    r.setUuid(uuid);
    r.setMetadata(MetadataEntity.fromCurrentInstant(clock));
    r.setEmailChallenge(UUID.randomUUID().toString());
    r.setRequestChallenge(UUID.randomUUID().toString());

    RequesterInfoEntity requester = new RequesterInfoEntity();

    requester.setEmail(format(EMAIL_TEMPLATE, id));
    requester.setEmailVerified(false);
    requester.setGivenName(format(GIVEN_NAME_TEMPLATE,id));
    requester.setFamilyName(format(FAMILY_NAME_TEMPLATE, id));
    requester.setUsername(format(USERNAME_TEMPLATE, id));
    
    RequestMessageEntity message = new RequestMessageEntity();
    message.setCreationTime(Date.from(clock.instant()));
    message.setMessage(format(MESSAGE_TEMPLATE, id));
    
    r.getMessages().add(message);

    r.setRequesterInfo(requester);
    r.setRealm(realm);
    r.setStatus(created);

    return r;
  }
  
  default RegistrationRequestDTO cleanupDto(RegistrationRequestDTO dto) {
    dto.setId(null);
    dto.setUuid(null);
    dto.setRealm(null);
    dto.setMetadata(null);
    
    dto.getMessages().forEach(m -> {
      m.setSender(null);
      m.setCreationTime(null);
    });
    
    return dto;
  }
  
  default Supplier<AssertionError> realmNotFoundError(String realmName){
    return () -> new AssertionError(format(ERROR_REALM_NOT_FOUND_TEMPLATE, realmName));
  }
  
  

  
}
