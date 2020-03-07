package it.infn.cnaf.sd.iam.api.apis.group;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.common.error.ValidationError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.repository.GroupRepository;

@Service
@Transactional
public class DefaultGroupService implements GroupService {

  public static final String GROUP_NOT_FOUND_ERROR_TEMPLATE = "Group not found for name: %s";

  final GroupRepository repo;
  final Clock clock;

  @Autowired
  public DefaultGroupService(GroupRepository repo, Clock clock) {
    this.repo = repo;
    this.clock = clock;
  }

  @Override
  public Page<GroupEntity> getGroups(Pageable page) {
    return repo.findByRealmName(RealmContext.getCurrentRealm(), page);
  }

  private Supplier<NotFoundError> groupNotFoundError(String message) {
    return () -> new NotFoundError(message);
  }

  @Override
  public GroupEntity createGroup(GroupEntity group) {

    group.setMetadata(MetadataEntity.fromCurrentInstant(clock));
    group.setUuid(UUID.randomUUID().toString());

    if (!isNull(group.getParentGroup())) {
      GroupEntity parentGroup =
          findByName(group.getParentGroup().getName()).orElseThrow(groupNotFoundError(
              format(GROUP_NOT_FOUND_ERROR_TEMPLATE, group.getParentGroup().getName())));
      group.setParentGroup(parentGroup);
      parentGroup.getChildrenGroups().add(group);

      String groupName = String.format("%s/%s", parentGroup.getName(), group.getName());
      if (groupName.length() > 512) {
        throw new ValidationError("Group name longer than 512 characters");
      }
      group.setName(groupName);
    }

    return repo.save(group);
  }

  @Override
  public Optional<GroupEntity> findByName(String name) {
    return repo.findByNameAndRealmName(name, RealmContext.getCurrentRealm());
  }

  @Override
  public Optional<GroupEntity> findByUuid(String uuid) {
    return repo.findByUuidAndRealmName(uuid, RealmContext.getCurrentRealm());
  }

}
