package it.infn.cnaf.sd.iam.api.kc;

import org.mapstruct.Mapper;

import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;

@Mapper(componentModel = "spring")
public interface KeycloakUserMapper {
  KeycloakUserDTO toUserDto(RegistrationRequestEntity request);
}
