package it.infn.cnaf.sd.iam.api.apis.group.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.infn.cnaf.sd.iam.api.common.dto.MetadataDTO;

public class GroupDTO {

  public static final String GROUP_NAME_REGEXP = "^[a-zA-Z][a-zA-Z0-9\\-_.]*$";

  private String id;

  private String uuid;

  @NotBlank(message = "the group name cannot be blank")
  @Size(max = 512, message = "the group name cannot be longer than 512 chars")
  @Pattern(regexp = GROUP_NAME_REGEXP,
      message = "invalid group name (does not match with regexp: '" + GROUP_NAME_REGEXP + "')")
  private String name;

  @Size(max = 512, message = "the group name cannot be longer than 512 chars")
  private String description;

  private MetadataDTO metadata;

  @Valid
  private GroupRefDTO parentGroup;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MetadataDTO getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataDTO metadata) {
    this.metadata = metadata;
  }
  
  public GroupRefDTO getParentGroup() {
    return parentGroup;
  }
  
  public void setParentGroup(GroupRefDTO parentGroup) {
    this.parentGroup = parentGroup;
  }

}
