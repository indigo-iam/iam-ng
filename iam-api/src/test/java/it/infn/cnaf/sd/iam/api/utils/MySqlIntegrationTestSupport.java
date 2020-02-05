package it.infn.cnaf.sd.iam.api.utils;

import org.junit.ClassRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class MySqlIntegrationTestSupport {

  @ClassRule
  public static IamMySqlContainer db = new IamMySqlContainer();

  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues
        .of("spring.datasource.url=" + db.getJdbcUrl("iam"), "spring.datasource.username=iam",
            "spring.datasource.password=pwd",
            "iam.realms[one].datasource.url=" + db.getJdbcUrl("one"),
            "iam.realms[one].datasource.username=one", "iam.realms[one].datasource.password=pwd",
            "iam.realms[two].datasource.url=" + db.getJdbcUrl("two"),
            "iam.realms[two].datasource.username=two", "iam.realms[two].datasource.password=pwd")
        .applyTo(applicationContext.getEnvironment());
    }
  }
}
