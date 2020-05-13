package it.infn.cnaf.sd.iam.api.apis.configuration;

import it.infn.cnaf.sd.iam.api.apis.configuration.dto.RealmConfigurationDTO;

public interface RealmConfigurationService {

  RealmConfigurationDTO getRealmConfiguration(String realmName);
  
  RealmConfigurationDTO getRealmConfiguration();

  void saveRealmConfiguration(RealmConfigurationDTO realmConfig);
}
