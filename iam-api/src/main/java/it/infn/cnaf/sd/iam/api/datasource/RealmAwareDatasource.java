package it.infn.cnaf.sd.iam.api.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;

public class RealmAwareDatasource extends AbstractRoutingDataSource {

  public RealmAwareDatasource() {

  }

  @Override
  protected Object determineCurrentLookupKey() {
    return RealmContext.getCurrentRealm();
  }

}
