package it.infn.cnaf.sd.iam.persistence.entity;

import javax.persistence.Column;

public class AddressEntity {
  
  @Column(name = "LABEL", nullable = true, length = 36)
  private String label;

  @Column(name = "IS_PRIMARY", nullable = false)
  private boolean primary;

  public AddressEntity() {
  }

}
