package it.infn.cnaf.sd.iam.api.apis.group;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

public interface GroupService {
  
  Optional<GroupEntity> findByName(String name);
  
  Optional<GroupEntity> findByUuid(String uuid);
  
  Page<GroupEntity> getGroups(Pageable page);
  
  GroupEntity createGroup(GroupEntity group);
  
  void deleteGroupByUuid(String uuid);
  
}
