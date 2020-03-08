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
package it.infn.cnaf.sd.iam.api.apis.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;
import it.infn.cnaf.sd.iam.persistence.repository.UserRepository;


@Service
public class DefaultUserService implements UserService {

  private final UserRepository repo;

  public DefaultUserService(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public Page<UserEntity> getUsers(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealmName(), page);
  }

  @Override
  public Optional<UserEntity> findUserById(Long id) {
    return repo.findById(id);
  }

}
