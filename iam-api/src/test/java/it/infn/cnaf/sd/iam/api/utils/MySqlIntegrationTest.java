package it.infn.cnaf.sd.iam.api.utils;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import it.infn.cnaf.sd.iam.api.utils.MySqlIntegrationTestSupport.Initializer;

@Retention(RUNTIME)
@Target(TYPE)
@ContextConfiguration(initializers = {Initializer.class})
@SpringBootTest
@ActiveProfiles("mysql-test")
public @interface MySqlIntegrationTest {

}
