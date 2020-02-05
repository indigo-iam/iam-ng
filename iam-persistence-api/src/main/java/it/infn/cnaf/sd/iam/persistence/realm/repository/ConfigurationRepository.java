package it.infn.cnaf.sd.iam.persistence.realm.repository;

import org.springframework.data.repository.CrudRepository;

import it.infn.cnaf.sd.iam.persistence.realm.entity.ConfigurationEntity;

public interface ConfigurationRepository extends CrudRepository<ConfigurationEntity, Long>{

}
