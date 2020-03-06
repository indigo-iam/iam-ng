package it.infn.cnaf.sd.iam.kc;

import static it.infn.cnaf.sd.iam.kc.persistence.UserRepository.QUERY_GET_USER_BY_USERNAME;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;

@Stateful
@Local(IamUserStorageProvider.class)
public class IamUserStorageProvider
    implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

  private static final Logger LOG = Logger.getLogger(IamUserStorageProvider.class);

  @PersistenceContext
  protected EntityManager em;

  protected ComponentModel model;
  protected KeycloakSession session;

  public IamUserStorageProvider() {}

  @Override
  public void close() {

  }

  @Override
  public UserModel getUserById(String id, RealmModel realm) {
    return null;
  }

  @Override
  public UserModel getUserByUsername(String username, RealmModel realm) {
    
    TypedQuery<UserEntity> query = em.createNamedQuery(QUERY_GET_USER_BY_USERNAME, UserEntity.class);
    query.setParameter("username", username);
    query.setParameter("realm", realm.getName());
    List<UserEntity> result = query.getResultList();
    if (result.isEmpty()) {
      LOG.info("Could not find username: "+username);
      return null;
    }
    return null;
  }

  @Override
  public UserModel getUserByEmail(String email, RealmModel realm) {
    return null;
  }

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return PasswordCredentialModel.TYPE.equals(credentialType);
  }

  @Override
  public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
    return false;
  }

  @Override
  public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
    return false;
  }

  public ComponentModel getModel() {
    return model;
  }

  public void setModel(ComponentModel model) {
    this.model = model;
  }

  public KeycloakSession getSession() {
    return session;
  }

  public void setSession(KeycloakSession session) {
    this.session = session;
  }
}
