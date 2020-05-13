package it.infn.cnaf.sd.iam.api.common.dto;

public class FieldErrorDTO {

  String fieldName;
  String fieldError;

  private FieldErrorDTO(String name, String error) {
    this.fieldName = name;
    this.fieldError = error;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldError() {
    return fieldError;
  }

  public void setFieldError(String fieldError) {
    this.fieldError = fieldError;
  }

  public static FieldErrorDTO newFieldError(String fieldName, String fieldError) {
    return new FieldErrorDTO(fieldName, fieldError);
  }

}
