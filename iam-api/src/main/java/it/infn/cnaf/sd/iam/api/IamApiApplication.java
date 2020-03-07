package it.infn.cnaf.sd.iam.api;

import static it.infn.cnaf.sd.iam.api.common.PackageConstants.API_PKG;
import static it.infn.cnaf.sd.iam.api.common.PackageConstants.PERSISTENCE_PKG;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = PERSISTENCE_PKG)
@EntityScan(PERSISTENCE_PKG)
@ComponentScan(basePackages = {API_PKG, PERSISTENCE_PKG},
    excludeFilters = {@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
public class IamApiApplication {
  public static void main(final String[] args) {
    SpringApplication.run(IamApiApplication.class, args);
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
