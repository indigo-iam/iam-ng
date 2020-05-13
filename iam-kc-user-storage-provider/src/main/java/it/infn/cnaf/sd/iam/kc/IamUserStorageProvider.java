/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.kc;


import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.UserStorageProviderModel;
import org.keycloak.storage.user.ImportSynchronization;
import org.keycloak.storage.user.SynchronizationResult;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;

@Stateful
@Local(IamUserStorageProvider.class)
public class IamUserStorageProvider implements UserStorageProvider, UserLookupProvider,
    UserQueryProvider, CredentialInputValidator, OnUserCache, CredentialInputUpdater, ImportSynchronization {

  public static final Set<String> DISABLEABLE_CREDENTIAL_TYPES =
      Collections.singleton(PasswordCredentialModel.TYPE);

  public static final String PASSWORD_CACHE_KEY = IamUserAdapter.class.getName() + ".password";

  private static final Logger LOG = Logger.getLogger(IamUserStorageProvider.class);

  @PersistenceContext
  protected EntityManager em;

  protected ComponentModel model;
  protected KeycloakSession session;

  public IamUserStorageProvider() {
    LOG.info("Inside constructor!");
  }

  @Remove
  @Override
  public void close() {

  }

  private IamUserAdapter userFromEntity(UserEntity e, RealmModel realm) {
    return new IamUserAdapter(session, realm, model, e);
  }

  @Override
  public UserModel getUserById(String id, RealmModel realm) {

    return null;
  }

  @Override
  public UserModel getUserByUsername(String username, RealmModel realm) {
    return findUserByUsername(username, realm).map(e -> userFromEntity(e, realm)).orElse(null);
  }

  @Override
  public UserModel getUserByEmail(String email, RealmModel realm) {
    return findUserByEmail(email, realm).map(e -> userFromEntity(e, realm)).orElse(null);
  }

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return PasswordCredentialModel.TYPE.equals(credentialType);
  }

  Optional<UserEntity> findUserByEmail(String email, RealmModel realm) {

    TypedQuery<UserEntity> query =
        em.createNamedQuery(UserEntity.QUERY_GET_USER_BY_EMAIL, UserEntity.class);

    query.setParameter("email", email);
    query.setParameter("realm", realm.getName());
    List<UserEntity> result = query.getResultList();
    if (result.isEmpty()) {
      LOG.info("Could not find user by email: " + email);
      return Optional.empty();
    }
    if (result.size() > 1) {
      LOG.warn("Found more than one result for a findUserByEmail query: " + email);
    }
    return Optional.of(result.get(0));
  }

  Optional<UserEntity> findUserByUserId(String userId, RealmModel realm) {

    TypedQuery<UserEntity> query =
        em.createNamedQuery(UserEntity.QUERY_GET_USER_BY_UUID, UserEntity.class);
    query.setParameter("uuid", userId);
    query.setParameter("realm", realm.getName());

    List<UserEntity> result = query.getResultList();
    if (result.isEmpty()) {
      LOG.info("Could not find uuid: " + userId);
      return Optional.empty();
    }

    if (result.size() > 1) {
      LOG.warn("Found more than one result for a findUserByUuid query: " + userId);
    }
    return Optional.of(result.get(0));
  }

  Optional<UserEntity> findUserByUsername(String username, RealmModel realm) {
    TypedQuery<UserEntity> query =
        em.createNamedQuery(UserEntity.QUERY_GET_USER_BY_USERNAME, UserEntity.class);
    query.setParameter("username", username);
    query.setParameter("realm", realm.getName());
    List<UserEntity> result = query.getResultList();
    if (result.isEmpty()) {
      LOG.info("Could not find username: " + username);
      return Optional.empty();
    }

    if (result.size() > 1) {
      LOG.warn("Found more than one result for a findUserByUsername query: " + username);
    }
    return Optional.of(result.get(0));
  }


  @Override
  public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {

    Optional<UserEntity> e = findUserByUsername(user.getUsername(), realm);
    if (e.isPresent()) {
      return !isNull(e.get().getPassword()) && PasswordCredentialModel.TYPE.equals(credentialType);
    }

    return false;
  }

  public String getPassword(UserModel user) {
    String password = null;
    if (user instanceof CachedUserModel) {
      password = (String) ((CachedUserModel) user).getCachedWith().get(PASSWORD_CACHE_KEY);
    } else if (user instanceof IamUserAdapter) {
      password = ((IamUserAdapter) user).getPassword();
    }
    return password;
  }

  @Override
  public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {

    if (!supportsCredentialType(credentialInput.getType())) {
      return false;
    }
    String password = getPassword(user);
    if (isNull(password))
      return false;

    return password.equals(credentialInput.getChallengeResponse());

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


  @SuppressWarnings("unchecked")
  @Override
  public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
    String password = ((IamUserAdapter) delegate).getPassword();
    if (!isNull(password)) {
      user.getCachedWith().put(PASSWORD_CACHE_KEY, password);
    }

  }

  @Override
  public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
    if (!input.getType().equals(PasswordCredentialModel.TYPE)) {
      return false;
    }

    Optional<UserEntity> userEntity = findUserByUsername(user.getUsername(), realm);
    if (!userEntity.isPresent()) {
      return false;
    }

    userEntity.get().setPassword(input.getChallengeResponse());
    em.persist(userEntity);
    return true;
  }

  @Override
  public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
    // do nothing
  }

  @Override
  public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
    return DISABLEABLE_CREDENTIAL_TYPES;
  }

  @Override
  public int getUsersCount(RealmModel realm) {
    TypedQuery<Long> query = em.createNamedQuery(UserEntity.QUERY_COUNT_REALM_USERS, Long.class);

    query.setParameter("realm", realm.getName());

    return query.getSingleResult() == null ? 0 : query.getSingleResult().intValue();
  }

  @Override
  public List<UserModel> getUsers(RealmModel realm) {
    return getUsers(realm, 0, Integer.MAX_VALUE - 1);
  }

  @Override
  public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
    TypedQuery<UserEntity> query =
        em.createNamedQuery(UserEntity.QUERY_GET_REALM_USERS, UserEntity.class);
    query.setFirstResult(firstResult);
    query.setMaxResults(maxResults);

    query.setParameter("realm", realm.getName());
    return query.getResultList()
      .stream()
      .map(e -> userFromEntity(e, realm))
      .collect(toList());
  }

  @Override
  public List<UserModel> searchForUser(String search, RealmModel realm) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult,
      int maxResults) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm,
      int firstResult, int maxResults) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult,
      int maxResults) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue,
      RealmModel realm) {
    return Collections.EMPTY_LIST;
  }

  @Override
  public SynchronizationResult sync(KeycloakSessionFactory sessionFactory, String realmId,
      UserStorageProviderModel model) {
    
    return null;
  }

  @Override
  public SynchronizationResult syncSince(Date lastSync, KeycloakSessionFactory sessionFactory,
      String realmId, UserStorageProviderModel model) {
    // TODO Auto-generated method stub
    return null;
  }

}
