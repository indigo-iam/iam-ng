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
package it.infn.cnaf.sd.iam.api.oauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

@Service
public class RealmAwareKeycloakAuthorizedClientRepository
    implements OAuth2AuthorizedClientRepository {

  private final Map<String, OAuth2AuthorizedClient> realmToClients = Maps.newConcurrentMap();
  
  public RealmAwareKeycloakAuthorizedClientRepository() {
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
      Authentication principal, HttpServletRequest request) { 
    return (T) realmToClients.get(clientRegistrationId);
  }

  @Override
  public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient,
      Authentication principal, HttpServletRequest request, HttpServletResponse response) {
    realmToClients.put(authorizedClient.getClientRegistration().getRegistrationId(), authorizedClient);
  }

  @Override
  public void removeAuthorizedClient(String clientRegistrationId, Authentication principal,
      HttpServletRequest request, HttpServletResponse response) {
    realmToClients.remove(clientRegistrationId);
  }

}
