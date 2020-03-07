package it.infn.cnaf.sd.iam.api.common.error;

public class ValidationError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ValidationError(String message) {
    super(message);
  }

}
