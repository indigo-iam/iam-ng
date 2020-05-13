package it.infn.cnaf.sd.iam.api.apis.requests;

import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.confirmed;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.approved;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.rejected;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import it.infn.cnaf.sd.iam.api.apis.users.UserService;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.EmailEntity;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@Service
public class DefaultRequestsService implements RequestsService {

  final Clock clock;
  final RegistrationRequestRepository repo;
  final UserService userService;

  @Autowired
  public DefaultRequestsService(Clock clock, RegistrationRequestRepository repo,
      UserService userService) {
    this.clock = clock;
    this.repo = repo;
    this.userService = userService;
  }

  @Override
  public Page<RegistrationRequestEntity> getPendingRequests(Pageable pageable) {
    return repo.findByRealmNameAndStatus(RealmContext.getCurrentRealmName(),
        RegistrationRequestStatus.confirmed, pageable);
  }

  protected void requestStatusSanityChecks(RegistrationRequestEntity request) {
    if (!request.getStatus().equals(confirmed)) {
      throw new IllegalArgumentException("Invalid request status: " + request.getStatus());
    }
  }


  protected UserEntity userFromRequest(RegistrationRequestEntity request) {

    UserEntity user = new UserEntity();

    user.setUuid(UUID.randomUUID().toString());
    user.setMetadata(MetadataEntity.fromCurrentInstant(clock));

    user.setGivenName(request.getRequesterInfo().getGivenName());
    user.setFamilyName(request.getRequesterInfo().getFamilyName());
    user.setUsername(request.getRequesterInfo().getUsername());
    user.setEmails(Sets.newHashSet());

    EmailEntity e = new EmailEntity();
    e.setVerified(true);
    e.setPrimary(true);
    e.setEmail(request.getRequesterInfo().getEmail());
    user.getEmails().add(e);
    user.setActive(true);

    return user;
  }

  @Override
  public RegistrationRequestEntity approveRequest(RegistrationRequestEntity request) {

    requestStatusSanityChecks(request);

    final Instant now = clock.instant();
    request.getMetadata().setLastUpdateTime(Date.from(now));
    request.setOutcome(approved);

    UserEntity user = userFromRequest(request);
    userService.createUser(user);
    repo.save(request);

    return request;
  }


  @Override
  public RegistrationRequestEntity rejectRequest(RegistrationRequestEntity request) {
    
    requestStatusSanityChecks(request);
    
    final Instant now = clock.instant();
    request.getMetadata().setLastUpdateTime(Date.from(now));
    request.setOutcome(rejected);
    
    repo.save(request);
    return request;
  }

}
