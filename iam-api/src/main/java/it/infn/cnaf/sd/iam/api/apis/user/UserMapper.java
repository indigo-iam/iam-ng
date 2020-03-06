package it.infn.cnaf.sd.iam.api.apis.user;

import org.mapstruct.Mapper;

import it.infn.cnaf.sd.iam.api.apis.user.dto.UserDTO;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDTO userEntityToDto(UserEntity entity);
  UserEntity userDtoToEntity(UserDTO dto);
}
