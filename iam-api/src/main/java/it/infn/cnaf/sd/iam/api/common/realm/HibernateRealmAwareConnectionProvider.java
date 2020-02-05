package it.infn.cnaf.sd.iam.api.common.realm;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.datasource.DataSourceHolder;

@Component
public class HibernateRealmAwareConnectionProvider
    extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private static final long serialVersionUID = 1L;

  private final DataSource iamDataSource;
  private final DataSourceHolder datasources;

  @Autowired
  public HibernateRealmAwareConnectionProvider(DataSource iamDs,
      DataSourceHolder realmDataSourceHolder) {
    iamDataSource = iamDs;
    datasources = realmDataSourceHolder;
  }

  @Override
  protected DataSource selectAnyDataSource() {
    return iamDataSource;
  }

  @Override
  protected DataSource selectDataSource(String tenantIdentifier) {
    if (HibernateRealmIdentifierResolver.DEFAULT_TENANT_IDENTIFIER.equals(tenantIdentifier)) {
      return iamDataSource;
    }
    return datasources.getRealmsDataSources().get(tenantIdentifier);
  }
}
