/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.oauth.CompositeJwtDecoder;
import it.infn.cnaf.sd.iam.api.oauth.JwtAuthenticationConverter;

@Component
public class RealmAuthenticationManagerResolver
    implements AuthenticationManagerResolver<HttpServletRequest> {

  private final CompositeJwtDecoder decoder;
  private final RealmNameResolver realmResolver;
  private final JwtAuthenticationConverter converter;

  private final Map<String, AuthenticationManager> authenticationManagers =
      new ConcurrentHashMap<>();

  @Autowired
  public RealmAuthenticationManagerResolver(RealmNameResolver realmResolver,
      CompositeJwtDecoder decoder, JwtAuthenticationConverter converter) {
    this.realmResolver = realmResolver;
    this.decoder = decoder;
    this.converter = converter;
  }

  private Supplier<RuntimeException> unknownRealm() {
    return () -> new NotFoundError("Unknown realm");
  }

  private String toRealm(HttpServletRequest request) {
    String realm = realmResolver.resolveRealmName(request);
    RealmContext.setCurrentRealmName(realm);
    return realm;
  }

  private JwtAuthenticationProvider buildAuthenticationProvider(String issuer) {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
    provider.setJwtAuthenticationConverter(converter);
    return provider;
  }

  private AuthenticationManager createForRealm(String realm) {
    return Optional.ofNullable(realm)
      .map(this::buildAuthenticationProvider)
      .orElseThrow(unknownRealm())::authenticate;
  }

  @Override
  public AuthenticationManager resolve(HttpServletRequest context) {
    return authenticationManagers.computeIfAbsent(toRealm(context), this::createForRealm);
  }

}
