package it.infn.cnaf.sd.iam.api.kc;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import it.infn.cnaf.sd.iam.api.apis.configuration.RealmConfigurationService;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Service
public class DefaultKeycloakClientRepository implements KeycloakClientRepository {

  private final Map<String, KeycloakClient> clients = Maps.newConcurrentMap();

  private final IamProperties iamProperties;
  private final RealmConfigurationService configService;

  @Autowired
  public DefaultKeycloakClientRepository(IamProperties properties,
      RealmConfigurationService configService) {
    this.iamProperties = properties;
    this.configService = configService;
  }

  protected KeycloakClient buildClient(String realmName) {

    Keycloak kc = KeycloakBuilder.builder()
      .grantType(CLIENT_CREDENTIALS)
      .realm(realmName)
      .serverUrl(iamProperties.getKeycloakAdminBaseUrl())
      .clientId(configService.getRealmConfiguration(realmName).getKc().getClientId())
      .clientSecret(configService.getRealmConfiguration(realmName).getKc().getClientSecret())
      .build();

    return new DefaultKeycloakClient(kc);
  }

  @Override
  public KeycloakClient getKeycloakClient() {
    final String realm = RealmContext.getCurrentRealmName();
    return clients.computeIfAbsent(realm, this::buildClient);
  }

}
