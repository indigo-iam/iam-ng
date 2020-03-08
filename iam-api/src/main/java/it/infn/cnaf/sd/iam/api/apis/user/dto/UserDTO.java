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
package it.infn.cnaf.sd.iam.api.apis.user.dto;

import java.util.List;

import it.infn.cnaf.sd.iam.api.common.dto.MetadataDTO;

public class UserDTO {

  MetadataDTO metadata;

  Long id;

  String uuid;

  String username;

  String givenName;

  String familyName;

  boolean active;

  boolean provisioned;

  List<EmailDTO> emails;

  public UserDTO() {}

  public MetadataDTO getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataDTO metadata) {
    this.metadata = metadata;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isProvisioned() {
    return provisioned;
  }

  public void setProvisioned(boolean provisioned) {
    this.provisioned = provisioned;
  }

  public List<EmailDTO> getEmails() {
    return emails;
  }

  public void setEmails(List<EmailDTO> emails) {
    this.emails = emails;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

}
