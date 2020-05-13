/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.api.apis.error;



import static it.infn.cnaf.sd.iam.api.common.dto.ErrorDTO.newError;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import it.infn.cnaf.sd.iam.api.apis.configuration.RealmConfigurationError;
import it.infn.cnaf.sd.iam.api.common.dto.ErrorDTO;
import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.common.error.ValidationError;

@RestControllerAdvice
public class ApiExceptionHandler implements ErrorUtils {

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationError.class)
  public ErrorDTO handleValidationError(ValidationError e) {
    return newError(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidRequestError.class)
  public ErrorDTO handleBadRequestError(InvalidRequestError e) {
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

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorDTO handleMessageNotReadableError(HttpMessageNotReadableException e) {
    return newError(HttpStatus.BAD_REQUEST, INVALID_HTTP_MESSAGE, e.getMessage());
  }
  
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RealmConfigurationError.class)
  public ErrorDTO handleInvalidConfiguration(RealmConfigurationError e) {
    return newError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

}
