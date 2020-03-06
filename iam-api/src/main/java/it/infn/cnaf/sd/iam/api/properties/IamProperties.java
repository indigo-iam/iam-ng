package it.infn.cnaf.sd.iam.api.properties;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
  
  int oauthKeysRefreshPeriodMinutes = (int) TimeUnit.HOURS.toSeconds(1);

  String testFlywayLocationBasePath;
  
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
}
