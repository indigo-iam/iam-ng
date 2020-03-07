package it.infn.cnaf.sd.iam.api.apis.error;



import static it.infn.cnaf.sd.iam.api.common.dto.ErrorDTO.newError;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.infn.cnaf.sd.iam.api.common.dto.ErrorDTO;
import it.infn.cnaf.sd.iam.api.common.error.BadRequestError;
import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.common.error.ValidationError;

@RestControllerAdvice
public class ApiExceptionHandler {
  
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationError.class)
  public ErrorDTO handleValidationError(ValidationError e) {
    return newError(HttpStatus.BAD_REQUEST, e.getMessage());
  }
  
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestError.class)
  public ErrorDTO handleBadRequestError(BadRequestError e) {
    return newError(HttpStatus.BAD_REQUEST, e.getMessage());
  }
  
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundError.class)
  public ErrorDTO handleNotFoundError(NotFoundError e) {
    return newError(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ResponseStatus(code = HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ErrorDTO handleAccessDeniedError(AccessDeniedException e) {
    return newError(HttpStatus.FORBIDDEN, e.getMessage());
  }

}
