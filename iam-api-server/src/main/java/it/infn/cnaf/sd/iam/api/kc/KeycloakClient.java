package it.infn.cnaf.sd.iam.api.kc;

import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;

public interface KeycloakClient {

  void createUser(RegistrationRequestEntity request);
  
}
