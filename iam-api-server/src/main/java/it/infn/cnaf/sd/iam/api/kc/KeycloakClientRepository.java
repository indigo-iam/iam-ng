package it.infn.cnaf.sd.iam.api.kc;

@FunctionalInterface
public interface KeycloakClientRepository {
  KeycloakClient getKeycloakClient();
}
