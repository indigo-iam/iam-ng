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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus;

public interface RegistrationRequestRepository
    extends PagingAndSortingRepository<RegistrationRequestEntity, Long> {

  Page<RegistrationRequestEntity> findByRealmName(String realmName, Pageable page);

  @Query("select r from RegistrationRequestEntity r join r.labels l where r.realm.name = :realmName and "
      + "l.prefix is null and l.name = :labelKey and l.value = :labelValue")
  Page<RegistrationRequestEntity> findByRealmNameAndLabelValue(String realmName, String labelKey,
      String labelValue, Pageable page);

  @Query("select r from RegistrationRequestEntity r where r.realm.name = :realmName and "
      + "r.status in ('created', 'confirmed')")
  Page<RegistrationRequestEntity> findByRealmNamePending(String realmName, Pageable page);
  
  Page<RegistrationRequestEntity> findByRealmNameAndStatus(String realmName,
      RegistrationRequestStatus status, Pageable page);

  Optional<RegistrationRequestEntity> findByRealmNameAndUuid(String realmName, String uuid);

  Optional<RegistrationRequestEntity> findByRealmNameAndEmailChallenge(String realmName,
      String emailChallenge);

  @Query("select r from RegistrationRequestEntity r where r.realm.name = :realmName and "
      + "r.requesterInfo.email = :email and r.status in ('created', 'confirmed')")
  Optional<RegistrationRequestEntity> findPendingByRealmNameAndEmail(String realmName,
      String email);

  @Query("select r from RegistrationRequestEntity r where r.realm.name = :realmName and "
      + "r.requesterInfo.username = :username and r.status in ('created', 'confirmed')")
  Optional<RegistrationRequestEntity> findPendingByRealmNameAndUsername(String realmName,
      String username);

}
