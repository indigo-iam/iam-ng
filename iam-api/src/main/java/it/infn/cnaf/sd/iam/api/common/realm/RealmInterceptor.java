package it.infn.cnaf.sd.iam.api.common.realm;

import static it.infn.cnaf.sd.iam.api.common.realm.RealmFilter.REALM_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class RealmInterceptor implements HandlerInterceptor {

  private final RealmNameResolver resolver;
  public RealmInterceptor(RealmNameResolver resolver) {
    this.resolver = resolver;
  }
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    final String realm = resolver.resolveRealmName(request);
    request.setAttribute(REALM_KEY, realm);
    RealmContext.setCurrentRealm(realm);
    return true;
  }
  
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    RealmContext.clear();
  }

}
