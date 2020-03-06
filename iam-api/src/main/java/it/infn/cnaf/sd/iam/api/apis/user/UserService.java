package it.infn.cnaf.sd.iam.api.apis.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;

public interface UserService {

  Page<UserEntity> getUsers(Pageable page);
  
  Optional<UserEntity> findUserById(Long id);
  
}
