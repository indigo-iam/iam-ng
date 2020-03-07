package it.infn.cnaf.sd.iam.api.common.dto;

import org.springframework.http.HttpStatus;

public class ErrorDTO {
  
  final String error;
  final String errorDescription;

  private ErrorDTO(String error, String errorDescription) {
    this.error = error;
    this.errorDescription = errorDescription;
  }
  
  private ErrorDTO(String error) {
    this(error,null);
  }

  public String getError() {
    return error;
  }
  
  public String getErrorDescription() {
    return errorDescription;
  }
  
  public static ErrorDTO newError(HttpStatus s, String errorDescription) {
    return new ErrorDTO(s.name().toLowerCase(), errorDescription);
  }
 
}
