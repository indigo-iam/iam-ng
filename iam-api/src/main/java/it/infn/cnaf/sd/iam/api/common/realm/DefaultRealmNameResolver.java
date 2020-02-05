package it.infn.cnaf.sd.iam.api.common.realm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class DefaultRealmNameResolver implements RealmNameResolver {

  public static final String SLASH = "/";
  public static final String API = "api";

  public DefaultRealmNameResolver() {}

  @Override
  public String resolveRealmName(HttpServletRequest request) {
    String[] pathParts = request.getRequestURI().split(SLASH);
    if (pathParts.length > 2) {
      if (!API.equals(pathParts[1])) {
        return null;
      } else
        return pathParts[2];
    }
    return null;
  }

}
