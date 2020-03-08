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
package it.infn.cnaf.sd.iam.api.apis.group;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.repository.GroupRepository;

@Service
@Transactional
public class DefaultGroupService implements GroupService, GroupSupport, ErrorUtils {
  
  final GroupRepository repo;
  final Clock clock;

  @Autowired
  public DefaultGroupService(GroupRepository repo, Clock clock) {
    this.repo = repo;
    this.clock = clock;
  }

  @Override
  public Page<GroupEntity> getGroups(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealmName(), page);
  }

  @Override
  public GroupEntity createGroup(GroupEntity group) {
    group.setMetadata(MetadataEntity.fromCurrentInstant(clock));
    group.setUuid(UUID.randomUUID().toString());

    if (!isNull(group.getParentGroup())) {
      GroupEntity parentGroup = findByName(group.getParentGroup().getName())
        .orElseThrow(notFoundError(groupNotFoundMessage(group.getParentGroup().getName())));

      String groupName = format("%s/%s", parentGroup.getName(), group.getName());
      group.setParentGroup(parentGroup);
      parentGroup.getChildrenGroups().add(group);
      group.setName(groupName);
    }

    group.setRealm(RealmContext.getCurrentRealmEntity());
    return repo.save(group);
  }

  @Override
  public Optional<GroupEntity> findByName(String name) {
    return repo.findByNameAndRealmName(name, RealmContext.getCurrentRealmName());
  }

  @Override
  public Optional<GroupEntity> findByUuid(String uuid) {
    return repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealmName());
  }

  @Override
  public void deleteGroupByUuid(String uuid) {
    GroupEntity group = repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealmName())
      .orElseThrow(notFoundError(groupNotFoundMessage(uuid)));
    if (!group.getChildrenGroups().isEmpty()) {
      throw new InvalidRequestError(GROUP_HAS_CHILDREN);
    }
    repo.delete(group);
  }

}
