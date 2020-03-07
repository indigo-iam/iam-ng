package it.infn.cnaf.sd.iam.api.oauth;

import static java.util.Objects.isNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  public static final String REALM_ACCESS_CLAIM = "realm_access";
  public static final String ROLES_CLAIM = "roles";

  public JwtAuthenticationConverter() {}

  protected Collection<GrantedAuthority> extractKeycloakAuthorities(Jwt jwt) {

    Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);

    if (!isNull(realmAccess)) {

      @SuppressWarnings("unchecked")
      List<String> realmRoles = (List<String>) realmAccess.get(ROLES_CLAIM);

      if (!isNull(realmRoles)) {
        return realmRoles.stream()
          .map(r -> String.format("ROLE_%s", r.toUpperCase().replace('-', '_')))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
      }
    }
    return Collections.emptySet();
  }

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    return new JwtAuthenticationToken(jwt, extractKeycloakAuthorities(jwt));
  }

}
