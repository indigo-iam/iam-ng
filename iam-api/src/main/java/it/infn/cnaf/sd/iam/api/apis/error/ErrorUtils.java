package it.infn.cnaf.sd.iam.api.apis.error;

import java.util.function.Supplier;

import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;

public interface ErrorUtils {

  public default Supplier<NotFoundError> notFoundError(String message) {
    return () -> new NotFoundError(message);
  }
}
