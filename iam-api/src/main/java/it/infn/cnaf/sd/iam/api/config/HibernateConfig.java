package it.infn.cnaf.sd.iam.api.config;

import static it.infn.cnaf.sd.iam.api.common.PackageConstants.PERSISTENCE_PKG;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.infn.cnaf.sd.iam.api.common.PackageConstants;
import it.infn.cnaf.sd.iam.api.datasource.DataSourceHolder;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = PERSISTENCE_PKG)
@EntityScan(PERSISTENCE_PKG)
public class HibernateConfig extends JpaBaseConfiguration implements PackageConstants {

  public static final Logger LOG = LoggerFactory.getLogger(HibernateConfig.class);

  @Autowired
  MultiTenantConnectionProvider connectionProvider;

  @Autowired
  CurrentTenantIdentifierResolver tenantResolver;

  @Autowired
  DataSourceHolder datasources;

  protected HibernateConfig(DataSource dataSource, JpaProperties properties,
      ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
    super(dataSource, properties, jtaTransactionManager);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      final EntityManagerFactoryBuilder factoryBuilder) {

    return factoryBuilder.dataSource(getDataSource())
      .packages(PackageConstants.PERSISTENCE_PKG)
      .properties(getVendorProperties())
      .build();
  }

  @Override
  protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Override
  protected Map<String, Object> getVendorProperties() {
    Map<String, Object> properties = Maps.newHashMap(getProperties().getProperties());
    properties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
    properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
    properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
    return properties;
  }

  @Bean
  @Profile("no-flyway")
  public FlywayMigrationStrategy noFlywayMigrationStrategy() {
    return f -> {
      LOG.info("Skipping flyway excution since the 'no-flyway' profile is active");
    };
  }

  @Bean
  @Profile("!no-flyway")
  public FlywayMigrationStrategy flywayMigrationStrategy(FlywayProperties properties,
      IamProperties iamProperties) {
    return f -> {
      // Migrate default database
      f.repair();
      f.migrate();
      
      iamProperties.getRealms().forEach((realmName, rc) -> {
        List<String> flywayLocations = Lists.newArrayList();
        flywayLocations.addAll(properties.getLocations());
        if (!Strings.isNullOrEmpty(iamProperties.getTestFlywayLocationBasePath())) {
          flywayLocations
            .add(String.format("%s/%s", iamProperties.getTestFlywayLocationBasePath(), realmName));
        }

        DataSource ds = datasources.getRealmsDataSources().get(realmName); 
        Flyway fw = Flyway.configure()
          .dataSource(ds)
          .locations(flywayLocations.toArray(new String[0]))
          .load();

        fw.repair();
        int appliedMigrations = fw.migrate();
        LOG.info("Applied {} migrations to datasource for realm {}", appliedMigrations, realmName);
      });
    };
  }


}
