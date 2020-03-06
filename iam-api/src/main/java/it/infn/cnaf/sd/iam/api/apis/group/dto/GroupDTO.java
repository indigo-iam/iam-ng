package it.infn.cnaf.sd.iam.api.apis.group.dto;

import it.infn.cnaf.sd.iam.api.common.dto.MetadataDTO;

public class GroupDTO {

  private String id;
  private String uuid;
  private String name;
  private String description;

  private MetadataDTO metadata;

  private GroupDTO parentGroup;

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

  public GroupDTO getParentGroup() {
    return parentGroup;
  }

  public void setParentGroup(GroupDTO parentGroup) {
    this.parentGroup = parentGroup;
  }

}
