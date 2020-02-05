package it.infn.cnaf.sd.iam.api.datasource;

import java.util.Map;

import javax.sql.DataSource;

public class DefaultDataSourceHolder implements DataSourceHolder {

  private final Map<String, DataSource> datasources;

  public DefaultDataSourceHolder(Map<String, DataSource> datasources) {
    this.datasources = datasources;
  }

  @Override
  public Map<String, DataSource> getRealmsDataSources() {
    return datasources;
  }

}
