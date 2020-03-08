package it.infn.cnaf.sd.iam.api.common.error;

public class InvalidRequestError extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  public InvalidRequestError(String message) {
    super(message);
  }
}
