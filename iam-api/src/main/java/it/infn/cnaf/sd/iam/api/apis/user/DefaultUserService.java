package it.infn.cnaf.sd.iam.api.apis.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;
import it.infn.cnaf.sd.iam.persistence.repository.UserRepository;


@Service
public class DefaultUserService implements UserService {

  private final UserRepository repo;

  public DefaultUserService(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public Page<UserEntity> getUsers(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealmName(), page);
  }

  @Override
  public Optional<UserEntity> findUserById(Long id) {
    return repo.findById(id);
  }

}
