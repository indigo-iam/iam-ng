package it.infn.cnaf.sd.iam.api.realm;

import it.infn.cnaf.sd.iam.persistence.realm.entity.ConfigurationEntity;

public interface RealmService {

  ConfigurationEntity getRealmConfiguration();
  
}
