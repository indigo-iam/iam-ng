package it.infn.cnaf.sd.iam.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("h2-console")
public class H2ConsoleSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    HttpSecurity h2Console = http.requestMatchers()
      .antMatchers("/h2-console", "/h2-console/**")
      .and()
      .csrf()
      .disable();

    h2Console.httpBasic();
    h2Console.headers().frameOptions().disable();

    h2Console.authorizeRequests().antMatchers("/h2-console/**", "/h2-console").permitAll();
  }


}
