package it.infn.cnaf.sd.iam.api.oauth;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import it.infn.cnaf.sd.iam.api.apis.configuration.RealmConfigurationService;
import it.infn.cnaf.sd.iam.api.apis.configuration.dto.RealmConfigurationDTO;
import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;

@Service
public class KeycloakClientRegistrationRepository implements ClientRegistrationRepository {

  public static final String TOKEN_ENDPOINT_CFG_KEY = "token_endpoint";

  private final Map<String, ClientRegistration> registeredClients = Maps.newConcurrentMap();

  private final RealmUtils realmUtils;
  private final OidcConfigurationFetcher fetcher;
  private final RealmConfigurationService realmConfigService;


  public KeycloakClientRegistrationRepository(RealmUtils realmUtils,
      OidcConfigurationFetcher configFetcher, RealmConfigurationService realmConfigService) {
    this.realmUtils = realmUtils;
    this.fetcher = configFetcher;
    this.realmConfigService = realmConfigService;
  }

  protected ClientRegistration resolveClient(String realmName) {
    RealmConfigurationDTO realmConfig = realmConfigService.getRealmConfiguration(realmName);

    Map<String, Object> openidConfig =
        fetcher.loadConfigurationForIssuer(realmUtils.realmTrustedTokenIssuerAsString(realmName));

    ClientRegistration.Builder clientBuilder = ClientRegistration.withRegistrationId(realmName)
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .clientId(realmConfig.getKc().getClientId())
      .clientSecret(realmConfig.getKc().getClientSecret())
      .providerConfigurationMetadata(openidConfig)
      .tokenUri((String) openidConfig.get(TOKEN_ENDPOINT_CFG_KEY));

    return clientBuilder.build();
  }

  @Override
  public ClientRegistration findByRegistrationId(String registrationId) {

    return registeredClients.computeIfAbsent(registrationId, this::resolveClient);
  }

}
