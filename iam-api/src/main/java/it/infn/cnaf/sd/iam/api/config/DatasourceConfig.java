package it.infn.cnaf.sd.iam.api.config;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import it.infn.cnaf.sd.iam.api.datasource.DataSourceHolder;
import it.infn.cnaf.sd.iam.api.datasource.DefaultDataSourceHolder;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Configuration
public class DatasourceConfig {

  public static final String H2_TEST_PROFILE = "h2-test";
  public static final String MYSQL_TEST_PROFILE = "mysql-test";

  public static final Logger LOG = LoggerFactory.getLogger(DatasourceConfig.class);


  private DataSource initializeDataSource(String realmName, DataSourceProperties dsp,
      IamProperties props, Map<String, DataSource> datasources) {

    LOG.info("Configuring datasource {} for realm {}", dsp.getUrl(), realmName);

    DataSource ds = DataSourceBuilder.create()
      .url(dsp.getUrl())
      .username(dsp.getUsername())
      .password(dsp.getPassword())
      .build();

    datasources.put(realmName, ds);
    return ds;
  }


  @Bean
  public DataSourceHolder realmDataSources(IamProperties props) {

    final Map<String, DataSource> configuredDataSources = Maps.newHashMap();
    props.getRealms()
      .forEach(
          (name, r) -> initializeDataSource(name, r.getDatasource(), props, configuredDataSources));
    return new DefaultDataSourceHolder(configuredDataSources);
  }
}
