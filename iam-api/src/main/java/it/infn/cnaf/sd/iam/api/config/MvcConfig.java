package it.infn.cnaf.sd.iam.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.infn.cnaf.sd.iam.api.common.realm.RealmInterceptor;
import it.infn.cnaf.sd.iam.api.common.realm.RealmNameResolver;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Autowired
  RealmNameResolver resolver;

  @Autowired
  RealmRepository repo;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RealmInterceptor(resolver, repo));
  }


}
