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
package it.infn.cnaf.sd.iam.api.apis.groups;

import static it.infn.cnaf.sd.iam.api.common.utils.ValidationHelper.handleValidationError;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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
import it.infn.cnaf.sd.iam.api.apis.groups.dto.GroupDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

@RestController
@RequestMapping(value = "/api/{realm}")
@Transactional
@PreAuthorize("hasRole('IAM_OWNER')")
public class GroupController implements GroupSupport, ErrorUtils {

  public static final String INVALID_GROUP_REPRESENTATION = "Invalid group representation";

  private final GroupService service;
  private final GroupMapper mapper;

  public GroupController(GroupService service, GroupMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  
  @GetMapping("/Groups")
  public ListResponseDTO<GroupDTO> listGroups(@RequestParam(required = false) final Integer count,
      @RequestParam(required = false) final Integer startIndex) {

    PageRequest pageRequest =
        PageUtils.buildPageRequest(count, startIndex, 50, Sort.by("name").ascending());

    Page<GroupEntity> pagedResults = service.getGroups(pageRequest);
    ListResponseDTO.Builder<GroupDTO> result = ListResponseDTO.builder();
    result.fromPage(pagedResults);

    result.resources(pagedResults.get().map(mapper::groupEntityToDto).collect(toList()));

    return result.build();
  }

  @GetMapping("/Groups/{groupId}")
  public GroupDTO getGroup(@PathVariable String groupId) {

    GroupEntity group =
        service.findByUuid(groupId).orElseThrow(notFoundError(groupNotFoundMessage(groupId)));

    return mapper.groupEntityToDto(group);
  }

  @PostMapping("/Groups")
  @ResponseStatus(CREATED)
  public GroupDTO createGroup(@RequestBody @Validated final GroupDTO group,
      final BindingResult validationResult) {
    handleValidationError(INVALID_GROUP_REPRESENTATION, validationResult);
    GroupEntity groupEntity = service.createGroup(mapper.groupDtoToEntity(group));
    return mapper.groupEntityToDto(groupEntity);
  }

  @DeleteMapping("/Groups/{groupId}")
  @ResponseStatus(NO_CONTENT)
  public void deleteGroup(@PathVariable String groupId) {
    service.deleteGroupByUuid(groupId);
  }

}
