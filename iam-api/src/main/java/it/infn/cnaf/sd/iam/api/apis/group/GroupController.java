package it.infn.cnaf.sd.iam.api.apis.group;

import static java.util.stream.Collectors.toList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;
import it.infn.cnaf.sd.iam.api.common.dto.ListResponseDTO;
import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.persistence.entity.GroupEntity;

@RestController
@RequestMapping(value = "/api/{realm}")
public class GroupController {

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

}
