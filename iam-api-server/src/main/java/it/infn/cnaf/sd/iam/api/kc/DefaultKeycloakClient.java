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
package it.infn.cnaf.sd.iam.api.kc;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;

public class DefaultKeycloakClient implements KeycloakClient {

  public static final Logger LOG = LoggerFactory.getLogger(DefaultKeycloakClient.class);

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_TEMPLATE = "Bearer %s";
  public static final String EXECUTE_ACTIONS_RESOURCE = "%s/users/%s/execute-actions-email";
  public static final String UPDATE_PASSWORD_ACTION = "[\"UPDATE_PASSWORD\"]";

  final Keycloak kc;
  final WebClient webClient;
  final RealmUtils realmUtils;

  @Autowired
  public DefaultKeycloakClient(Keycloak kc, WebClient webClient, RealmUtils realmUtils) {
    this.kc = kc;
    this.webClient = webClient;
    this.realmUtils = realmUtils;
  }

  @Override
  public UserRepresentation createUser(UserRepresentation user) {
    final String realm = RealmContext.getCurrentRealmName();

    Response response = kc.realm(realm).users().create(user);

    if (response.getStatus() != HttpStatus.CREATED.value()) {
      throw new KeycloakError(response.getStatusInfo().getReasonPhrase());
    }

    UserRepresentation createdUser = kc.realm(realm).users().search(user.getUsername()).get(0);
    sendPasswordResetEmail(createdUser.getId());

    return createdUser;
  }

  @Override
  public boolean emailAvailable(String email) {
    final String realm = RealmContext.getCurrentRealmName();
    return kc.realm(realm).users().count(null, null, email, null) == 0;
  }

  @Override
  public boolean usernameAvailable(String username) {
    final String realm = RealmContext.getCurrentRealmName();
    return kc.realm(realm).users().count(null, null, null, username) == 0;
  }

  private String buildAuthorizationHeaderContents() {
    return String.format(BEARER_TEMPLATE, kc.tokenManager().getAccessTokenString());
  }

  @Override
  public void sendPasswordResetEmail(String userId) {
    // Unfortunately the KC client does not cover the full keycloak API :(

    webClient.put()
      .uri(format(EXECUTE_ACTIONS_RESOURCE, realmUtils.realmAdminBaseContext(), userId))
      .contentType(APPLICATION_JSON)
      .header(AUTHORIZATION_HEADER, buildAuthorizationHeaderContents())
      .bodyValue(UPDATE_PASSWORD_ACTION)
      .retrieve()
      .toBodilessEntity()
      .block();

  }

}
