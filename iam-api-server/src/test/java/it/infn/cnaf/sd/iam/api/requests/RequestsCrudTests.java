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
package it.infn.cnaf.sd.iam.api.requests;

import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecision.reject;
import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecisionDTO.approve;
import static it.infn.cnaf.sd.iam.api.apis.requests.dto.RequestDecisionDTO.reject;
import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.done;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.approved;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.rejected;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.registrations.RegistrationRequestMapper;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestCreationResultDTO;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.apis.requests.DefaultRequestsService;
import it.infn.cnaf.sd.iam.api.kc.KeycloakClient;
import it.infn.cnaf.sd.iam.api.kc.KeycloakClientRepository;
import it.infn.cnaf.sd.iam.api.kc.KeycloakError;
import it.infn.cnaf.sd.iam.api.registration.RegistrationRequestTestUtils;
import it.infn.cnaf.sd.iam.api.utils.ClockUtils;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.api.utils.WithMockAdminUser;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@RunWith(SpringRunner.class)
@IamTest
public class RequestsCrudTests extends IntegrationTestSupport
    implements RegistrationRequestTestUtils {

  @Autowired
  RegistrationRequestMapper requestMapper;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  RegistrationRequestRepository requestRepo;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  KeycloakClientRepository repo;

  @Mock
  KeycloakClient client;

  @Captor
  ArgumentCaptor<UserRepresentation> userCaptor;

  Clock clock = ClockUtils.TEST_CLOCK;

  @Before
  public void setup() {
    when(repo.getKeycloakClient()).thenReturn(client);
    when(client.emailAvailable(Mockito.anyString())).thenReturn(true);
    when(client.usernameAvailable(Mockito.anyString())).thenReturn(true);
  }

  @Test
  public void testListPendingRequiresAuthenticatedUsers() throws Exception {
    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  public void testListPendingRequiresPrivilegedUsers() throws Exception {
    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isForbidden());
  }


  @WithMockAdminUser
  @Test
  public void testListPendingWorksFineForAdmins() throws Exception {
    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources").isEmpty());
  }

  @WithMockAdminUser
  @Test
  public void testPendingRequestRepoWorkAsExpected() throws Exception {

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources", hasSize(1)));


    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));
    e.setStatus(RegistrationRequestStatus.confirmed);
    requestRepo.save(e);

    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources", hasSize(1)));

    e.setStatus(RegistrationRequestStatus.done);
    requestRepo.save(e);

    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources", hasSize(0)));
  }

  @WithMockAdminUser
  @Test
  public void testPendingRequestRepoWorkAsExpectedMore() throws Exception {

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    for (int i = 0; i < 10; i++) {
      RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, i);
      RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

      mvc
        .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
          .contentType(APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
    }

    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources", hasSize(10)));

    requestRepo.findAll().forEach(r -> {
      r.setStatus(RegistrationRequestStatus.done);
      requestRepo.save(r);
    });

    mvc.perform(get("/Realms/alice/Requests/registration/pending"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.resources").isArray())
      .andExpect(jsonPath("$.resources", hasSize(0)));
  }


  @Test
  public void testRequestDecisionRequiresAuthenticatedUser() throws Exception {
    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .param("decision", reject.name())).andExpect(status().isUnauthorized());

  }

  @Test
  @WithMockUser
  public void testRequestDecisionRequiresPrivilegedUser() throws Exception {
    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .content(mapper.writeValueAsString(reject()))
      .contentType(APPLICATION_JSON)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockAdminUser
  public void testRequestRejectWorksForAdminUser() throws Exception {
    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .content(mapper.writeValueAsString(reject()))
      .contentType(APPLICATION_JSON)).andExpect(status().isOk());

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));

    assertThat(e.getStatus(), is(done));
    assertThat(e.getOutcome(), is(rejected));
  }

  @Test
  @WithMockAdminUser
  public void testRequestApprovalWorksForAdminUser() throws Exception {

    when(client.createUser(Mockito.any())).then(i -> i.getArgument(0));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .content(mapper.writeValueAsString(approve()))
      .contentType(APPLICATION_JSON)).andExpect(status().isOk());

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));

    assertThat(e.getStatus(), is(done));
    assertThat(e.getOutcome(), is(approved));
    verify(client).createUser(userCaptor.capture());
    UserRepresentation user = userCaptor.getValue();

    assertThat(user.getUsername(), is(req.getRequesterInfo().getUsername()));
    assertThat(user.getFirstName(), is(req.getRequesterInfo().getGivenName()));
    assertThat(user.getLastName(), is(req.getRequesterInfo().getFamilyName()));
    assertThat(user.getEmail(), is(req.getRequesterInfo().getEmail()));

    List<String> requestIdAttr =
        user.getAttributes().get(DefaultRequestsService.REGISTRATION_REQUEST_ID);
    assertThat(requestIdAttr, not(empty()));

    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
        .content(mapper.writeValueAsString(approve()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error", is("bad_request")))
      .andExpect(jsonPath("$.errorDescription", is("Request already handled")));
  }

  @Test
  @WithMockAdminUser
  public void testRequestApprovalWithMessageWorksForAdminUser() throws Exception {
    when(client.createUser(Mockito.any())).then(i -> i.getArgument(0));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .content(mapper.writeValueAsString(approve("approval message")))
      .contentType(APPLICATION_JSON)).andExpect(status().isOk());

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));

    assertThat(e.getStatus(), is(done));
    assertThat(e.getOutcome(), is(approved));
    assertThat(e.getMessages(), hasSize(2));
    assertThat(e.getMessages(), hasItem(hasProperty("message", is("approval message"))));
    assertThat(e.getMessages(), hasItem(hasProperty("sender", is("admin"))));
    verify(client).createUser(userCaptor.capture());
    UserRepresentation user = userCaptor.getValue();

    assertThat(user.getUsername(), is(req.getRequesterInfo().getUsername()));
    assertThat(user.getFirstName(), is(req.getRequesterInfo().getGivenName()));
    assertThat(user.getLastName(), is(req.getRequesterInfo().getFamilyName()));
    assertThat(user.getEmail(), is(req.getRequesterInfo().getEmail()));

    List<String> requestIdAttr =
        user.getAttributes().get(DefaultRequestsService.REGISTRATION_REQUEST_ID);
    assertThat(requestIdAttr, not(empty()));

    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
        .content(mapper.writeValueAsString(approve()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error", is("bad_request")))
      .andExpect(jsonPath("$.errorDescription", is("Request already handled")));
  }

  @Test
  @WithMockAdminUser
  public void testRequestMessageValidation() throws Exception {
    when(client.createUser(Mockito.any())).then(i -> i.getArgument(0));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
        .content(mapper.writeValueAsString(approve("<html>Hello</html>")))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription").value("Invalid request decision"))
      .andExpect(
          jsonPath("$.fieldErrors[?(@.fieldName == 'requestDecisionDTO.message')].fieldError")
            .value(hasItem("invalid message")));
  }


  @Test
  @WithMockAdminUser
  public void testRequestRejectionWithMessageWorksForAdminUser() throws Exception {
    when(client.createUser(Mockito.any())).then(i -> i.getArgument(0));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc.perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
      .content(mapper.writeValueAsString(reject("rejection message")))
      .contentType(APPLICATION_JSON)).andExpect(status().isOk());

    RegistrationRequestEntity e =
        requestRepo.findByRealmNameAndUuid("alice", responseDto.getRequestId())
          .orElseThrow(assertionError("Expected request not found"));

    assertThat(e.getStatus(), is(done));
    assertThat(e.getOutcome(), is(rejected));
    assertThat(e.getMessages(), hasSize(2));
    assertThat(e.getMessages(), hasItem(hasProperty("message", is("rejection message"))));
    assertThat(e.getMessages(), hasItem(hasProperty("sender", is("admin"))));

    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
        .content(mapper.writeValueAsString(approve()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error", is("bad_request")))
      .andExpect(jsonPath("$.errorDescription", is("Request already handled")));
  }

  @Test
  @WithMockAdminUser
  public void testRequestNotFoundHandled() throws Exception {
    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", UUID.randomUUID().toString())
        .content(mapper.writeValueAsString(approve()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithMockAdminUser
  public void testKeycloakErrorHandled() throws Exception {
    when(client.createUser(Mockito.any()))
      .thenThrow(new KeycloakError("Error contacting keycloak!"));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RegistrationRequestEntity req = newTemplateRequest(clock, realm, null, 0);
    RegistrationRequestDTO dto = cleanupDto(requestMapper.entityToDto(req));

    String response = mvc
      .perform(post("/Realms/alice/Registrations").content(mapper.writeValueAsString(dto))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    RegistrationRequestCreationResultDTO responseDto =
        mapper.readValue(response, RegistrationRequestCreationResultDTO.class);

    mvc
      .perform(post("/Realms/alice/Requests/registration/{requestId}", responseDto.getRequestId())
        .content(mapper.writeValueAsString(approve()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.errorDescription").value("Error contacting keycloak!"));
  }

}
