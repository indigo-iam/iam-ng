/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2014-2020.
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

import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

public class TrustedJwtDecoderCacheLoader extends CacheLoader<String, JwtDecoder> {

  public static final Logger LOG = LoggerFactory.getLogger(TrustedJwtDecoderCacheLoader.class);

  private final OidcConfigurationFetcher fetcher;
  private final ExecutorService executor;
  private final IamProperties props;


  private final OAuth2TokenValidator<Jwt> validator;

  @Autowired
  public TrustedJwtDecoderCacheLoader(IamProperties props, RestTemplateBuilder builder,
      OidcConfigurationFetcher fetcher, ExecutorService executor, RealmUtils realmUtils) {
    this.fetcher = fetcher;
    this.executor = executor;
    this.props = props;

    validator = new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(),
        new RealmAwareTokenIssuerValidator(realmUtils));
  }

  @Override
  public JwtDecoder load(String issuer) throws Exception {

    if (!issuer.startsWith(props.getKeycloakBaseUrl())) {
      throw new UnknownTokenIssuerError(issuer);
    }

    Map<String, Object> oidcConfiguration = fetcher.loadConfigurationForIssuer(issuer);

    NimbusJwtDecoder decoder =
        NimbusJwtDecoder.withJwkSetUri(oidcConfiguration.get("jwks_uri").toString()).build();

    decoder.setJwtValidator(validator);
    return decoder;
  }

  @Override
  public ListenableFuture<JwtDecoder> reload(String issuer, JwtDecoder oldValue) throws Exception {

    LOG.debug("Scheduling reload configuration for OAuth issuer '{}'", issuer);

    ListenableFutureTask<JwtDecoder> task = ListenableFutureTask.create(() -> load(issuer));
    executor.execute(task);

    return task;
  }
}
