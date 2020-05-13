package it.infn.cnaf.sd.iam.api.apis.configuration.dto;

import javax.validation.Valid;


public class RealmConfigurationDTO {

  @Valid
  private KcClient kc;

  public RealmConfigurationDTO() {
    // TODO Auto-generated constructor stub
  }

  public KcClient getKc() {
    return kc;
  }

  public void setKc(KcClient kc) {
    this.kc = kc;
  }

}
