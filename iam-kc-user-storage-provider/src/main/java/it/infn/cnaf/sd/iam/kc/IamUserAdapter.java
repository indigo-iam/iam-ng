package it.infn.cnaf.sd.iam.kc;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;



public class IamUserAdapter extends AbstractUserAdapter {

  final UserEntity entity;

  public IamUserAdapter(KeycloakSession session, RealmModel realm,
      ComponentModel storageProviderModel, UserEntity entity) {
    super(session, realm, storageProviderModel);
    this.entity = entity;
  }

  @Override
  public String getUsername() {
    return entity.getUsername();
  }

  @Override
  public String getFirstName() {
    return entity.getGivenName();
  }

  @Override
  public String getLastName() {
    return entity.getFamilyName();
  }
  
  @Override
  public boolean isEnabled() {
    return entity.isActive();
  }
}
