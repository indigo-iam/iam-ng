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
