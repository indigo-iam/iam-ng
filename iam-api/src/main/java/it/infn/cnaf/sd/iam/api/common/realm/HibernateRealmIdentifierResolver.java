package it.infn.cnaf.sd.iam.api.common.realm;

import java.util.Objects;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class HibernateRealmIdentifierResolver implements CurrentTenantIdentifierResolver {

  public static final String DEFAULT_TENANT_IDENTIFIER = "__default__";

  @Override
  public String resolveCurrentTenantIdentifier() {
    final String currentRealm = RealmContext.getCurrentRealm();
    if (Objects.isNull(currentRealm)) {
      return DEFAULT_TENANT_IDENTIFIER;
    }
    return currentRealm;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }

}
