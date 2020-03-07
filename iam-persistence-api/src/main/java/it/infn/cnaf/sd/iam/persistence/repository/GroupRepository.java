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
