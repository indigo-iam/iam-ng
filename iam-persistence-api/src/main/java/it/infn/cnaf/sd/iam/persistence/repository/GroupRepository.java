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
package it.infn.cnaf.sd.iam.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;


public interface GroupRepository extends PagingAndSortingRepository<GroupEntity, Long> {
  Page<GroupEntity> findByRealmName(String realmName, Pageable page);
  Optional<GroupEntity> findByNameAndRealmName(String name, String realmName);
  Optional<GroupEntity> findByUuidAndRealmName(String uuid, String realmName);
}
