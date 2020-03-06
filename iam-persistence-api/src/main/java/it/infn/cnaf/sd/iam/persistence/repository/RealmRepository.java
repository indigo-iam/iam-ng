package it.infn.cnaf.sd.iam.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;

public interface RealmRepository extends PagingAndSortingRepository<RealmEntity, Long> {

  Optional<RealmEntity> findByName(String name);
  
}
