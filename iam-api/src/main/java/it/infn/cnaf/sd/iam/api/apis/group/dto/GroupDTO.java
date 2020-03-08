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
package it.infn.cnaf.sd.iam.api.apis.group.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.infn.cnaf.sd.iam.api.apis.group.validator.CompositeGroupNameSize;
import it.infn.cnaf.sd.iam.api.common.dto.MetadataDTO;
import it.infn.cnaf.sd.iam.api.common.dto.RealmDTO;

@CompositeGroupNameSize
public class GroupDTO {

  public static final String GROUP_NAME_REGEXP = "^[a-zA-Z][a-zA-Z0-9\\-_.]*$";
  public static final String GROUP_DESCRIPTION_REGEXP = "[a-zA-Z][a-zA-Z0-9\\-_.;\\s/]*$";

  private String id;

  private String uuid;

  @NotBlank(message = "the group name cannot be blank")
  @Size(max = 512, message = "the group name cannot be longer than 512 chars")
  @Pattern(regexp = GROUP_NAME_REGEXP,
      message = "invalid group name (does not match with regexp: '" + GROUP_NAME_REGEXP + "')")
  private String name;

  @Size(max = 512, message = "the group description cannot be longer than 512 chars")
  @Pattern(regexp = GROUP_DESCRIPTION_REGEXP,
    message = "invalid group description (does not match with regexp: '" + GROUP_DESCRIPTION_REGEXP + "')")
  private String description;

  private MetadataDTO metadata;

  @Valid
  private GroupRefDTO parentGroup;

  private RealmDTO realm;

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

  public RealmDTO getRealm() {
    return realm;
  }

  public void setRealm(RealmDTO realm) {
    this.realm = realm;
  }

}
