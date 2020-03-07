package it.infn.cnaf.sd.iam.api.apis.group.dto;

import javax.validation.constraints.NotBlank;

import it.infn.cnaf.sd.iam.api.apis.group.validator.GroupName;

public class GroupRefDTO {

  @NotBlank(message = "Group ref name cannot be blank")
  @GroupName
  String name;

  String location;

  String uuid;

  public GroupRefDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
