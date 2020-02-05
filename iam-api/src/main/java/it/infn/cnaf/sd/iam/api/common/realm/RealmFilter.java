package it.infn.cnaf.sd.iam.api.common.realm;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RealmFilter implements Filter {

  public static final String REALM_KEY = "iam.realm";
  
  final RealmNameResolver resolver;
  
  public RealmFilter(RealmNameResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    
    final String realm = resolver.resolveRealmName(httpRequest);
    RealmContext.setCurrentRealm(realm);
    request.setAttribute(REALM_KEY, realm);
    
    try {
      chain.doFilter(httpRequest, response);
    } finally {
      RealmContext.clear();
    }
  }

}
