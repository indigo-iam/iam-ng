package it.infn.cnaf.sd.iam.api.common.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ValidationErrorDTO extends ErrorDTO {

  List<String> globalErrors;
  List<FieldErrorDTO> fieldErrors;

  public ValidationErrorDTO(String errorDescription) {
    super(HttpStatus.BAD_REQUEST.name().toLowerCase(), errorDescription, null);
  }

  public List<String> getGlobalErrors() {
    return globalErrors;
  }

  public void setGlobalErrors(List<String> globalErrors) {
    this.globalErrors = globalErrors;
  }

  public List<FieldErrorDTO> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

}
