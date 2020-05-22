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
package it.infn.cnaf.sd.iam.api.apis.registrations.validator;

import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.done;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.kc.KeycloakClientRepository;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@Component
@Scope("prototype")
public class UsernameAvailableValidator implements ConstraintValidator<UsernameAvailable, String> {

  KeycloakClientRepository repo;
  RegistrationRequestRepository requestRepo;

  public UsernameAvailableValidator(KeycloakClientRepository repo,
      RegistrationRequestRepository requestRepo) {
    this.repo = repo;
    this.requestRepo = requestRepo;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    Optional<RegistrationRequestEntity> request =
        requestRepo.findPendingByRealmNameAndUsername(RealmContext.getCurrentRealmName(), value);

    boolean hasPendingRequest = request.isPresent() && !done.equals(request.get().getStatus());

    return repo.getKeycloakClient().usernameAvailable(value) && !hasPendingRequest;
  }

}
