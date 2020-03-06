package it.infn.cnaf.sd.iam.kc.persistence;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@NamedQueries({@NamedQuery(name = UserRepository.QUERY_GET_USER_BY_USERNAME,
    query = "select u from UserEntity u where u.username = :username and u.realm.name = :realm"),})
public interface UserRepository {
  public static final String QUERY_GET_USER_BY_USERNAME = "getUserByUsername";
}
