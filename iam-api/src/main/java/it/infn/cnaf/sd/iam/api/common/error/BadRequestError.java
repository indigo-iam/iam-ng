package it.infn.cnaf.sd.iam.api.common.error;

public class BadRequestError extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  public BadRequestError(String message) {
    super(message);
  }
}
