package it.infn.cnaf.sd.iam.persistence.realm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmailEntity {

  @Column(name = "LABEL", nullable = true, length = 36)
  private String label;

  @Column(name = "IS_PRIMARY", nullable = false)
  private boolean primary;

  @Column(name = "EMAIL", nullable = false, length = 128)
  private String email;

  @Column(name = "EMAIL_VERIFIED", nullable = false)
  private boolean verified;

  public EmailEntity() {}

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EmailEntity other = (EmailEntity) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    return true;
  }
}
