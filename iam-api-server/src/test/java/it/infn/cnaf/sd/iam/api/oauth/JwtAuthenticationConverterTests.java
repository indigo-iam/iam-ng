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
package it.infn.cnaf.sd.iam.api.oauth;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.testcontainers.shaded.com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.testcontainers.shaded.com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationConverterTests {

  JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

  @Test
  public void testNoRealmAccessClaimYeldsEmptyAuthorities() {

    Jwt jwt = Jwt.withTokenValue("test")
      .header("alg", "none")
      .claim("iss", "https://test")
      .claim("sub", "pippo")
      .build();

    JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

    assertThat(token.getAuthorities(), empty());
  }

  @Test
  public void testAuthoritiesAreFoundAndMapped() {
    List<String> roles = newArrayList("iam-admin", "iam-user");
    Map<String, Object> claims = Maps.newHashMap();
    claims.put("roles", roles);

    Jwt jwt = Jwt.withTokenValue("test")
      .header("alg", "none")
      .claim("iss", "https://test")
      .claim("sub", "pippo")
      .claim("realm_access", claims)
      .build();

    JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);
    assertThat(token.getAuthorities(), hasItems(new SimpleGrantedAuthority("ROLE_IAM_ADMIN"),
        new SimpleGrantedAuthority("ROLE_IAM_USER")));
  }

  @Test
  public void testEmptyRealmAccessYeldsEmptyAuthorities() {

    Map<String, Object> claims = Maps.newHashMap();

    Jwt jwt = Jwt.withTokenValue("test")
      .header("alg", "none")
      .claim("iss", "https://test")
      .claim("sub", "pippo")
      .claim("realm_access", claims)
      .build();

    JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);
    assertThat(token.getAuthorities(), empty());
  }

}
