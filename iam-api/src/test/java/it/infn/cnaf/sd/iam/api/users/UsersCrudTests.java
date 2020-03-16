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
package it.infn.cnaf.sd.iam.api.users;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.apis.users.dto.PasswordDTO;
import it.infn.cnaf.sd.iam.api.apis.users.dto.UserDTO;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.api.utils.WithMockAdminUser;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;
import it.infn.cnaf.sd.iam.persistence.repository.UserRepository;

@RunWith(SpringRunner.class)
@IamTest
public class UsersCrudTests extends IntegrationTestSupport implements UserTestSupport {
  public static String USERS_ENDPOINT = "/api/alice/Users";
  public static String USER_ENDPOINT = "/api/alice/Users/{id}";
  public static String USER_PASSWORD_ENDPOINT = "/api/alice/Users/{id}/password";

  @Autowired
  UserRepository repo;

  @WithAnonymousUser
  @Test
  public void testUserAccessRequiresAuthentication() throws Exception {
    mvc.perform(get(USERS_ENDPOINT)).andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  public void testUserAccessRequiresOwnerPrivileges() throws Exception {
    mvc.perform(get(USERS_ENDPOINT)).andExpect(status().isForbidden());
  }

  @WithMockAdminUser
  @Test
  public void testUserAccessReturnsCountWithDefaultTestUsers() throws Exception {
    mvc.perform(get(USERS_ENDPOINT))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalResults").value(1));
  }

  @WithAnonymousUser
  @Test
  public void testUserCreationRequiresAuthentication() throws Exception {
    mvc
      .perform(post(USERS_ENDPOINT).content(mapper.writeValueAsString(lennonUser()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  public void testUserCreationRequiresOwnerPrivileges() throws Exception {
    mvc
      .perform(post(USERS_ENDPOINT).content(mapper.writeValueAsString(lennonUser()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @WithMockAdminUser
  @Test
  public void testUserCreationWorks() throws Exception {
    mvc
      .perform(post(USERS_ENDPOINT).content(mapper.writeValueAsString(lennonUser()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.givenName").value(LENNON_GIVEN_NAME))
      .andExpect(jsonPath("$.familyName").value(LENNON_FAMILY_NAME))
      .andExpect(jsonPath("$.username").value(LENNON_USERNAME))
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"));
  }

  @WithMockAdminUser
  @Test
  public void testUserDeletionWorks() throws Exception {

    UserDTO createdUser = mapper.readValue(mvc
      .perform(post(USERS_ENDPOINT).content(mapper.writeValueAsString(lennonUser()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.givenName").value(LENNON_GIVEN_NAME))
      .andExpect(jsonPath("$.familyName").value(LENNON_FAMILY_NAME))
      .andExpect(jsonPath("$.username").value(LENNON_USERNAME))
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"))
      .andReturn()
      .getResponse()
      .getContentAsString(), UserDTO.class);

    mvc.perform(get(USER_ENDPOINT, createdUser.getUuid()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.givenName").value(LENNON_GIVEN_NAME))
      .andExpect(jsonPath("$.familyName").value(LENNON_FAMILY_NAME))
      .andExpect(jsonPath("$.username").value(LENNON_USERNAME))
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"));

    mvc.perform(delete(USER_ENDPOINT, createdUser.getUuid())).andExpect(status().isNoContent());

    mvc.perform(get(USER_ENDPOINT, createdUser.getUuid()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.errorDescription", containsString("User not found")));

  }

  @WithMockAdminUser
  @Test
  public void testUserChangePasswordWorks() throws Exception {

    UserDTO createdUser = mapper.readValue(mvc
      .perform(post(USERS_ENDPOINT).content(mapper.writeValueAsString(lennonUser()))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.givenName").value(LENNON_GIVEN_NAME))
      .andExpect(jsonPath("$.familyName").value(LENNON_FAMILY_NAME))
      .andExpect(jsonPath("$.username").value(LENNON_USERNAME))
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"))
      .andReturn()
      .getResponse()
      .getContentAsString(), UserDTO.class);


    mvc.perform(post(USER_PASSWORD_ENDPOINT, createdUser.getUuid())
      .content(mapper.writeValueAsString(PasswordDTO.newInstance("password")))
      .contentType(APPLICATION_JSON)).andExpect(status().isCreated());

    UserEntity updatedUser = repo.findByUuidAndRealmName(createdUser.getUuid(), ALICE_REALM)
      .orElseThrow(assertionError(EXPECTED_USER_NOT_FOUND));

    assertThat(updatedUser.getPassword(), is("password"));
  }

}
