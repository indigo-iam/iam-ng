package it.infn.cnaf.sd.iam.api.datasource;

import java.util.Map;

import javax.sql.DataSource;

public interface DataSourceHolder {

  Map<String, DataSource> getRealmsDataSources();

}
