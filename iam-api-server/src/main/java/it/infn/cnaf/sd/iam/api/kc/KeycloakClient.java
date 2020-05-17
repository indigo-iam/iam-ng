package it.infn.cnaf.sd.iam.api.kc;

import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakClient {

  void sendPasswordResetEmail(String username);
  
  UserRepresentation createUser(UserRepresentation user);
  
  boolean emailAvailable(String email);
  
  boolean usernameAvailable(String username);
}
