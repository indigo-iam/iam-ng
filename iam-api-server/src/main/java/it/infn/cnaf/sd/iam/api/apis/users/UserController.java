/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.api.apis.users;

import static it.infn.cnaf.sd.iam.api.common.utils.ValidationHelper.handleValidationError;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.apis.users.dto.PasswordDTO;
import it.infn.cnaf.sd.iam.api.apis.users.dto.UserDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;



@RestController
@RequestMapping(value = "/Realms/{realm}")
@PreAuthorize("hasRole('IAM_OWNER')")
@Transactional
public class UserController implements UserSupport, ErrorUtils {

  private final UserService service;
  private final UserMapper mapper;

  @Autowired
  public UserController(UserService service, UserMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping("/Users")
  public ListResponseDTO<UserDTO> listUsers(@RequestParam(required = false) final Integer count,
      @RequestParam(required = false) final Integer startIndex) {

    PageRequest pageRequest =
        PageUtils.buildPageRequest(count, startIndex, 50, Sort.by("familyName").ascending());

    Page<UserEntity> pagedResults = service.getUsers(pageRequest);

    ListResponseDTO.Builder<UserDTO> result = ListResponseDTO.builder();
    result.fromPage(pagedResults);

    result.resources(pagedResults.get().map(mapper::userEntityToDto).collect(toList()));

    return result.build();
  }

  @PostMapping("/Users")
  @ResponseStatus(CREATED)
  public UserDTO createUser(@RequestBody @Validated final UserDTO user,
      final BindingResult validationResult) {
    handleValidationError(INVALID_USER_REPRESENTATION, validationResult);
    UserEntity entity = service.createUser(mapper.userDtoToEntity(user));
    return mapper.userEntityToDto(entity);
  }


  @GetMapping("/Users/{userId}")
  public UserDTO getUser(@PathVariable String userId) {
    return service.findUserByUuid(userId)
      .map(mapper::userEntityToDto)
      .orElseThrow(notFoundError(userNotFoundMessage(userId)));
  }

  @PostMapping("/Users/{userId}/password")
  @ResponseStatus(CREATED)
  public void changeUserPassword(@PathVariable String userId,
      @RequestBody @Valid PasswordDTO password) {
    service.changeUserPassword(userId, password.getPassword());
  }

  @DeleteMapping("/Users/{userId}")
  @ResponseStatus(NO_CONTENT)
  public void deleteUser(@PathVariable String userId) {
    service.deleteUserByUUid(userId);
  }

}
