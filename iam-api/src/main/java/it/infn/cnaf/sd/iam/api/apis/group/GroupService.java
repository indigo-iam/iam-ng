package it.infn.cnaf.sd.iam.api.apis.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

public interface GroupService {

  Page<GroupEntity> getGroups(Pageable page);
}
