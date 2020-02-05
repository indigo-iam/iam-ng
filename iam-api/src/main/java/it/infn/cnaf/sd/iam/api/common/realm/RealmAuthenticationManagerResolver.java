package it.infn.cnaf.sd.iam.api.common.realm;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.oauth.CompositeJwtDecoder;

@Component
public class RealmAuthenticationManagerResolver
    implements AuthenticationManagerResolver<HttpServletRequest> {

  private final CompositeJwtDecoder decoder;
  private final RealmNameResolver realmResolver;
  private final Map<String, AuthenticationManager> authenticationManagers =
      new ConcurrentHashMap<>();

  @Autowired
  public RealmAuthenticationManagerResolver(RealmNameResolver realmResolver,
      CompositeJwtDecoder decoder) {
    this.realmResolver = realmResolver;
    this.decoder = decoder;
  }

  private Supplier<IllegalArgumentException> unknownTenant() {
    return () -> new IllegalArgumentException("unknown realm");
  }

  private String toRealm(HttpServletRequest request) {
    String realm = realmResolver.resolveRealmName(request);
    RealmContext.setCurrentRealm(realm);
    return realm;
  }

  private AuthenticationManager createForRealm(String realm) {
    return Optional.ofNullable(realm)
      .map(s -> new JwtAuthenticationProvider(decoder))
      .orElseThrow(unknownTenant())::authenticate;
  }

  @Override
  public AuthenticationManager resolve(HttpServletRequest context) {
    return authenticationManagers.computeIfAbsent(toRealm(context), this::createForRealm);
  }

}
