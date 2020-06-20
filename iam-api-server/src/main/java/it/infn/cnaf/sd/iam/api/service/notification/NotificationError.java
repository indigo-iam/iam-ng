package it.infn.cnaf.sd.iam.api.service.notification;

public class NotificationError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotificationError(String message, Throwable cause) {
    super(message, cause);
  }
}
