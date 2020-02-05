package it.infn.cnaf.sd.iam.api.properties;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

@ConfigurationProperties("iam")
@Configuration
public class IamProperties {

  public static class OAuth2ProviderProperties {
    private String issuer;

    public String getIssuer() {
      return issuer;
    }

    public void setIssuer(String issuer) {
      this.issuer = issuer;
    }
  }
  
  public static class RealmProperties {
    private DataSourceProperties datasource;
    
    private OAuth2ProviderProperties oauth2;

    public DataSourceProperties getDatasource() {
      return datasource;
    }

    public void setDatasource(DataSourceProperties datasource) {
      this.datasource = datasource;
    }

    public OAuth2ProviderProperties getOauth2() {
      return oauth2;
    }

    public void setOauth2(OAuth2ProviderProperties oauth2) {
      this.oauth2 = oauth2;
    }
  }
  
  int oauthKeysRefreshPeriodMinutes = (int) TimeUnit.HOURS.toSeconds(1);

  String testFlywayLocationBasePath;
  
  Map<String, RealmProperties> realms = Maps.newHashMap();

  public int getOauthKeysRefreshPeriodMinutes() {
    return oauthKeysRefreshPeriodMinutes;
  }

  public void setOauthKeysRefreshPeriodMinutes(int oauthKeysRefreshPeriodMinutes) {
    this.oauthKeysRefreshPeriodMinutes = oauthKeysRefreshPeriodMinutes;
  }

  public void setTestFlywayLocationBasePath(String testFlywayLocationBasePath) {
    this.testFlywayLocationBasePath = testFlywayLocationBasePath;
  }

  public String getTestFlywayLocationBasePath() {
    return testFlywayLocationBasePath;
  }
  
  public Map<String, RealmProperties> getRealms() {
    return realms;
  }
  
  public void setRealms(Map<String, RealmProperties> realms) {
    this.realms = realms;
  }
}
