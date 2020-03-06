package it.infn.cnaf.sd.iam.api.apis.group;

import org.mapstruct.Mapper;

import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

@Mapper(componentModel = "spring")
public interface GroupMapper {
  GroupDTO groupEntityToDto(GroupEntity entity);
  GroupEntity groupDtoToEntity(GroupDTO dto);
}
