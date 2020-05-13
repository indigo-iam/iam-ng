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
        requestRepo.findByRealmNameAndUsername(RealmContext.getCurrentRealmName(), value);

    boolean hasPendingRequest = request.isPresent() && !done.equals(request.get().getStatus());

    return repo.getKeycloakClient().usernameAvailable(value) && !hasPendingRequest;
  }

}
