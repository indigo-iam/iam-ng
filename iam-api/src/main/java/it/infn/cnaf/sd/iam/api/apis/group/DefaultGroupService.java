package it.infn.cnaf.sd.iam.api.apis.group;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;
import it.infn.cnaf.sd.iam.persistence.repository.GroupRepository;

@Service
@Transactional
public class DefaultGroupService implements GroupService{

  final GroupRepository repo;
  
  public DefaultGroupService(GroupRepository repo) {
    this.repo = repo;
  }

  @Override
  public Page<GroupEntity> getGroups(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealm(), page);
  }

}
