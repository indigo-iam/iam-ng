package it.infn.cnaf.sd.iam.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
  Page<UserEntity> findByRealmName(String realmName, Pageable page);
}
