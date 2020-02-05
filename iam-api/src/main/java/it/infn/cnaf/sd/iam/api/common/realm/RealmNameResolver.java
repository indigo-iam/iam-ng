package it.infn.cnaf.sd.iam.api.common.realm;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface RealmNameResolver {
  String resolveRealmName(HttpServletRequest request);
}
