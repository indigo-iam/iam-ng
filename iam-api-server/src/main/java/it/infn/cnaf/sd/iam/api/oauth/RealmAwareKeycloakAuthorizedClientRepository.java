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
