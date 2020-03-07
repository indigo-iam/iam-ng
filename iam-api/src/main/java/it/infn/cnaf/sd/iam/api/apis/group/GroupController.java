package it.infn.cnaf.sd.iam.api.apis.group;

import static it.infn.cnaf.sd.iam.api.common.utils.ValidationHelper.handleValidationError;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

@RestController
@RequestMapping(value = "/api/{realm}")
public class GroupController {

  public static final String INVALID_GROUP_REPRESENTATION = "Invalid group representation";

  private final GroupService service;
  private final GroupMapper mapper;

  public GroupController(GroupService service, GroupMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PreAuthorize("hasRole('IAM_ADMIN')")
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

  @PreAuthorize("hasRole('IAM_ADMIN')")
  @PostMapping("/Groups")
  @ResponseStatus(CREATED)
  public GroupDTO createGroup(@RequestBody @Validated final GroupDTO group,
      final BindingResult validationResult) {
    handleValidationError(INVALID_GROUP_REPRESENTATION, validationResult);
    GroupEntity groupEntity = service.createGroup(mapper.groupDtoToEntity(group));
    return mapper.groupEntityToDto(groupEntity);
  }

}
