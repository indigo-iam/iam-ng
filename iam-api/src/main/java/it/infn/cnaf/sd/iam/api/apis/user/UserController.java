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
package it.infn.cnaf.sd.iam.api.apis.user;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.user.dto.UserDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.persistence.entity.UserEntity;



@RestController
@RequestMapping(value = "/api/{realm}")
public class UserController {

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

    PageRequest pageRequest = PageUtils.buildPageRequest(count, startIndex, 50,
        Sort.by("familyName").ascending());

    Page<UserEntity> pagedResults = service.getUsers(pageRequest);

    ListResponseDTO.Builder<UserDTO> result = ListResponseDTO.builder();
    result.fromPage(pagedResults);

    result.resources(pagedResults.get().map(mapper::userEntityToDto).collect(toList()));

    return result.build();
  }
}
