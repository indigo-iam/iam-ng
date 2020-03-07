package it.infn.cnaf.sd.iam.api.utils;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithMockUser;

@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@WithMockUser(username = "admin", roles = {"IAM_ADMIN", "IAM_OWNER", "IAM_USER", "USER"})
public @interface WithMockAdminUser {

}
