package it.infn.cnaf.sd.iam.api.kc;

public class KeycloakError extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public KeycloakError(String message) {
    super(message);
  }

}
