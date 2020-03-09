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
package it.infn.cnaf.sd.iam.api.apis.users;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.infn.cnaf.sd.iam.api.apis.common.BaseService;
import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;
import it.infn.cnaf.sd.iam.persistence.repository.UserRepository;


@Service
@Transactional
public class DefaultUserService extends BaseService<UserRepository>
    implements UserService, UserSupport, ErrorUtils {

  @Autowired
  public DefaultUserService(UserRepository repo, Clock clock) {
    super(repo, clock);
  }

  @Override
  public Page<UserEntity> getUsers(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealmName(), page);
  }

  @Override
  public Optional<UserEntity> findUserByUuid(String uuid) {
    return repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealmName());
  }

  @Override
  public Optional<UserEntity> findUserByUsername(String username) {
    return repo.findByUsernameAndRealmName(username, RealmContext.getCurrentRealmName());
  }

  @Override
  public UserEntity createUser(UserEntity user) {
    user.setMetadata(MetadataEntity.fromCurrentInstant(clock));
    user.setRealm(RealmContext.getCurrentRealmEntity());
    user.setUuid(UUID.randomUUID().toString());
    return repo.save(user);
  }

  @Override
  public void deleteUserByUUid(String uuid) {
    UserEntity user = repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealmName())
      .orElseThrow(notFoundError(userNotFoundMessage(uuid)));

    repo.delete(user);
  }

  @Override
  public void changeUserPassword(String uuid, String password) {
    UserEntity user = repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealmName())
      .orElseThrow(notFoundError(userNotFoundMessage(uuid)));
    user.setPassword(password);
    repo.save(user);

  }



}
