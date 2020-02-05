package it.infn.cnaf.sd.iam.api.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import it.infn.cnaf.sd.iam.api.common.realm.RealmFilter;
import it.infn.cnaf.sd.iam.api.common.realm.RealmNameResolver;
import it.infn.cnaf.sd.iam.api.oauth.CompositeJwtDecoder;
import it.infn.cnaf.sd.iam.api.oauth.OidcConfigurationFetcher;
import it.infn.cnaf.sd.iam.api.oauth.TrustedJwtDecoderCacheLoader;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Configuration
public class SecurityBeans {

  public static final Logger LOG = LoggerFactory.getLogger(ApiSecurityConfig.class);

  // @Bean
  public FilterRegistrationBean<RealmFilter> realmFilter(RealmNameResolver resolver) {
    FilterRegistrationBean<RealmFilter> realmFilter =
        new FilterRegistrationBean<>(new RealmFilter(resolver));
    realmFilter.addUrlPatterns("/api/**");
    // This filter should run **before** the spring security filter
    realmFilter.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 100);
    return realmFilter;
  }


  @Bean
  public ExecutorService singleThreadExecutorService() {
    return Executors.newSingleThreadExecutor();
  }

  @Bean
  public CompositeJwtDecoder jwtDecoder(IamProperties properties, RestTemplateBuilder builder,
      OidcConfigurationFetcher fetcher, ExecutorService executor) {

    TrustedJwtDecoderCacheLoader loader =
        new TrustedJwtDecoderCacheLoader(properties, builder, fetcher, executor);

    LoadingCache<String, JwtDecoder> decoders = CacheBuilder.newBuilder()
      .refreshAfterWrite(properties.getOauthKeysRefreshPeriodMinutes(), TimeUnit.MINUTES)
      .build(loader);

    properties.getRealms().forEach((name, r) -> {
      final String issuer = r.getOauth2().getIssuer();
      LOG.info("Initializing OAuth trusted issuer: {} for realm: {}", issuer, name);
      try {
        decoders.put(issuer, loader.load(issuer));
      } catch (Exception e) {
        LOG.warn("Error initializing trusted issuer: {}", e.getMessage());
        if (LOG.isDebugEnabled()) {
          LOG.warn("Error initializing trusted issuer: {}", e.getMessage(), e);
        }
      }
    });

    LOG.info("OAuth trusted issuer configuration will be refreshed every {} minutes",
        properties.getOauthKeysRefreshPeriodMinutes());

    return new CompositeJwtDecoder(decoders);
  }

}
