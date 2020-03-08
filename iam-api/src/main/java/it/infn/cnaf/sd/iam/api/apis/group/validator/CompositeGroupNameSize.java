package it.infn.cnaf.sd.iam.api.apis.group.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = CompositeGroupNameSizeValidator.class)
public @interface CompositeGroupNameSize {

  String message() default "composite group name exceeds 512 chars";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
