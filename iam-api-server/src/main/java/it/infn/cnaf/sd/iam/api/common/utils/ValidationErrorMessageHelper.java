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
package it.infn.cnaf.sd.iam.api.common.utils;

import static it.infn.cnaf.sd.iam.api.common.dto.FieldErrorDTO.newFieldError;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import it.infn.cnaf.sd.iam.api.common.dto.ValidationErrorDTO;
import it.infn.cnaf.sd.iam.api.common.error.ValidationError;

public class ValidationErrorMessageHelper {

  private ValidationErrorMessageHelper() {}

  public static ValidationErrorDTO buildValidationErrorDto(ValidationError error) {
    ValidationErrorDTO errorDto = new ValidationErrorDTO(error.getMessage());

    errorDto.setGlobalErrors(error.getValidationResult()
      .getGlobalErrors()
      .stream()
      .map(ObjectError::getDefaultMessage)
      .collect(toList()));

    errorDto.setFieldErrors(error.getValidationResult()
      .getFieldErrors()
      .stream()
      .map(e -> newFieldError(format("%s.%s", e.getObjectName(), e.getField()), e.getDefaultMessage()))
      .collect(toList()));

    return errorDto;
  }

  public static String buildValidationErrorMessage(String errorMessage,
      BindingResult validationResult) {

    StringBuilder validationError = new StringBuilder();
    validationError.append(errorMessage + ": ");

    boolean first = true;

    for (ObjectError error : validationResult.getAllErrors()) {

      if (!first) {
        validationError.append(",");
      }

      if (error instanceof FieldError) {
        FieldError fieldError = (FieldError) error;

        validationError.append(String.format("[%s.%s : %s]", fieldError.getObjectName(),
            fieldError.getField(), fieldError.getDefaultMessage()));

      } else {

        validationError
          .append(String.format("[%s : %s]", error.getObjectName(), error.getDefaultMessage()));
      }

      first = false;
    }

    return validationError.toString();
  }

}
