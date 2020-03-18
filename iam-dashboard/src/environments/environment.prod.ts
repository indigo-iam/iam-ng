import { KeycloakConfig } from 'keycloak-angular';

let keycloakConfig: KeycloakConfig = {
  url: 'https://localhost:8443/auth/',
  realm: 'master',
  clientId: 'iam-dashboard'
};

export const environment = {
  production: true,
  keycloak: keycloakConfig
};


