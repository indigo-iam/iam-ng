package it.infn.cnaf.sd.iam.api.apis.registrations.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Retention(RUNTIME)
@Target({FIELD, METHOD})
@Constraint(validatedBy = UsernameAvailableValidator.class)
public @interface UsernameAvailable {

  String message() default "Username is not available";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
