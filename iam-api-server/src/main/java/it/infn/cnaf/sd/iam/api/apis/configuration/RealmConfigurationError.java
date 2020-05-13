package it.infn.cnaf.sd.iam.api.apis.configuration;

public class RealmConfigurationError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RealmConfigurationError(String message, Throwable cause) {
    super(message, cause);
  }

}
