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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.apis.registrations.RegistrationRequestMapper;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestCreationResultDTO;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.utils.ClockUtils;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@RunWith(SpringRunner.class)
@IamTest
public class RegistrationCrudTests extends IntegrationTestSupport
    implements RegistrationRequestTestUtils {

  @Autowired
  RegistrationRequestMapper requestMapper;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  RegistrationRequestRepository requestRepo;

  @Autowired
  ObjectMapper mapper;

  Clock clock = ClockUtils.TEST_CLOCK;

  @Test
  public void testInvalidRequestAccess() throws Exception {
    mvc.perform(post("/Realms/alice/Registrations"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", is(ErrorUtils.INVALID_HTTP_MESSAGE)));
  }

  @Test
  public void testSimpleRequestCreationSuccess() throws Exception {
    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated());
  }


  @Test
  public void testSimpleRequestCreationSuccessAuthenticated() throws Exception {
    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));


    String response = mvc
      .perform(post("/Realms/alice/Registrations").with(jwt())
        .content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));
    
    assertThat(e.getAttachments(), hasSize(1));
    assertThat(e.getStatus(), is(created));

  }
  
  @Test
  public void testGetConfiguration() throws Exception {
    mvc.perform(get("/Realms/alice/Registrations/config"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.registrationEnabled").value(true))
      .andExpect(jsonPath("$.privacyPolicyUrl").value("https://alice.example.com/privacy"))
      .andExpect(jsonPath("$.aupUrl").value("https://alice.example.com/aup"));
    
    mvc.perform(get("/Realms/iam/Registrations/config"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.registrationEnabled").value(false))
    .andExpect(jsonPath("$.privacyPolicyUrl").value("https://iam.example.com/privacy"))
    .andExpect(jsonPath("$.aupUrl").doesNotExist());
  }

}
