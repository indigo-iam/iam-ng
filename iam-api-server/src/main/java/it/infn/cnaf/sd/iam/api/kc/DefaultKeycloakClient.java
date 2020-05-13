package it.infn.cnaf.sd.iam.api.kc;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;

public class DefaultKeycloakClient implements KeycloakClient {

  public static final Logger LOG = LoggerFactory.getLogger(DefaultKeycloakClient.class);

  final Keycloak kc;

  @Autowired
  public DefaultKeycloakClient(Keycloak kc) {
    this.kc = kc;
  }

  @Override
  public UserRepresentation createUser(UserRepresentation user) {
    final String realm = RealmContext.getCurrentRealmName();

    Response response = kc.realm(realm).users().create(user);
    
    if (response.getStatus() != HttpStatus.OK.value()) {
      throw new KeycloakError(response.getStatusInfo().getReasonPhrase());
    }

    return kc.realm(realm).users().search(user.getUsername()).get(0);
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

}
