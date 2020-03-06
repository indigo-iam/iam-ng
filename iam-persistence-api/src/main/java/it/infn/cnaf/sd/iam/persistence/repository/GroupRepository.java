package it.infn.cnaf.sd.iam.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

public interface GroupRepository extends PagingAndSortingRepository<GroupEntity, Long> {
  Page<GroupEntity> findByRealmName(String realmName, Pageable page);
}
