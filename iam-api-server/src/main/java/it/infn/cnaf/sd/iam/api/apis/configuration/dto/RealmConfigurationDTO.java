package it.infn.cnaf.sd.iam.api.apis.configuration.dto;

import javax.validation.Valid;


public class RealmConfigurationDTO {

  @Valid
  private KcClient kc;

  public RealmConfigurationDTO() {
  }

  public KcClient getKc() {
    return kc;
  }

  public void setKc(KcClient kc) {
    this.kc = kc;
  }

}
