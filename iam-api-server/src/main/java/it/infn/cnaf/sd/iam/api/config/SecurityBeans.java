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
package it.infn.cnaf.sd.iam.api.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;
import it.infn.cnaf.sd.iam.api.oauth.CompositeJwtDecoder;
import it.infn.cnaf.sd.iam.api.oauth.OidcConfigurationFetcher;
import it.infn.cnaf.sd.iam.api.oauth.TrustedJwtDecoderCacheLoader;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@Configuration
public class SecurityBeans {

  public static final Logger LOG = LoggerFactory.getLogger(ApiSecurityConfig.class);

  @Bean
  public ExecutorService singleThreadExecutorService() {
    return Executors.newSingleThreadExecutor();
  }

  @Bean
  public CompositeJwtDecoder jwtDecoder(IamProperties properties, RestTemplateBuilder builder,
      OidcConfigurationFetcher fetcher, ExecutorService executor, RealmRepository repo,
      RealmUtils utils) {

    TrustedJwtDecoderCacheLoader loader =
        new TrustedJwtDecoderCacheLoader(properties, builder, fetcher, executor, utils);

    LoadingCache<String, JwtDecoder> decoders = CacheBuilder.newBuilder()
      .refreshAfterWrite(properties.getOauthKeysRefreshPeriodMinutes(), TimeUnit.MINUTES)
      .build(loader);

    repo.findAll().forEach(realm -> {
      final String realmIssuer = utils.realmTrustedTokenIssuerAsString(realm.getName());
      LOG.info("Initializing OAuth trusted issuer: {} for realm: {}", realmIssuer, realm.getName());

      try {
        decoders.put(realmIssuer, loader.load(realmIssuer));
      } catch (Exception e) {
        LOG.warn("Error initializing trusted issuer: {}", e.getMessage());
        if (LOG.isDebugEnabled()) {
          LOG.warn("Error initializing trusted issuer: {}", e.getMessage(), e);
        }
      }
    });

    LOG.info("OAuth trusted issuer configuration will be refreshed every {} minutes",
        properties.getOauthKeysRefreshPeriodMinutes());

    return new CompositeJwtDecoder(decoders, utils);
  }

}
