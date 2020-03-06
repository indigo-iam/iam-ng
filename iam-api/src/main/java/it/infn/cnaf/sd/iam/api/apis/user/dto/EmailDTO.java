package it.infn.cnaf.sd.iam.api.apis.user.dto;

public class EmailDTO {

  private boolean primary;

  private String label;

  private String email;

  private boolean verified;

  public EmailDTO() {}

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

}
