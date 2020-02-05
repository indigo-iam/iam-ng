package it.infn.cnaf.sd.iam.api.realm;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.infn.cnaf.sd.iam.persistence.realm.entity.ConfigurationEntity;
import it.infn.cnaf.sd.iam.persistence.realm.repository.ConfigurationRepository;

@Component
@Transactional
public class DefaultRealmService implements RealmService {

  ConfigurationRepository configRepo;

  public DefaultRealmService(ConfigurationRepository repo) {
    this.configRepo = repo;
  }

  @Override
  public ConfigurationEntity getRealmConfiguration() {

    Iterable<ConfigurationEntity> result = configRepo.findAll();
    return result.iterator().next();
  }

}
