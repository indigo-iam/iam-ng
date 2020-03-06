package it.infn.cnaf.sd.iam.api.utils;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Retention(RUNTIME)
@Target(TYPE)
@SpringBootTest
@ActiveProfiles(resolver = TestActiveProfileResolver.class)
public @interface IamTest {

}
