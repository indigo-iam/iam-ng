package it.infn.cnaf.sd.iam.api.common.dto;

public class BaseDTO {

  final String kind;

  public BaseDTO(String kind) {
    this.kind = kind;
  }

  public String getKind() {
    return kind;
  }


}
