package it.infn.cnaf.sd.iam.api.kc;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;

@Component
public class DefaultKeycloakClient implements KeycloakClient {

  public static final Logger LOG = LoggerFactory.getLogger(DefaultKeycloakClient.class);

  final WebClient client;
  final IamProperties properties;
  final KeycloakUserMapper mapper;

  @Autowired
  public DefaultKeycloakClient(WebClient client, IamProperties properties,
      KeycloakUserMapper mapper) {
    this.client = client;
    this.properties = properties;
    this.mapper = mapper;
  }

  private String createUserUri(String realm) {
    return String.format("%s/%s/users", properties.getKeycloakBaseUrl(), realm);
  }

  @Override
  public void createUser(RegistrationRequestEntity request) {
    final String realm = RealmContext.getCurrentRealmName();

    client.post()
      .uri(createUserUri(realm))
      .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(realm))
      .contentType(APPLICATION_JSON)
      .bodyValue(mapper.toUserDto(request))
      .retrieve();
  }

}
