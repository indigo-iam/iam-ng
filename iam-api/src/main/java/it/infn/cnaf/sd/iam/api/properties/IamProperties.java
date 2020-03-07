package it.infn.cnaf.sd.iam.api.properties;

import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("iam")
@Configuration
@Validated
public class IamProperties {

  public static final int T_48_HOURS_IN_MINUTES = 48 * 60;

  @Min(value = 0, message = "Please provide a zero or positive OAuth keys refresh period")
  @Max(value = T_48_HOURS_IN_MINUTES,
      message = "Please provide a keys refresh period shorter than 48 hours (in minutes)")
  int oauthKeysRefreshPeriodMinutes = (int) TimeUnit.HOURS.toSeconds(1);

  @NotBlank(message = "Please provide a keycloak base URL")
  String keycloakBaseUrl = "http://localhost:8080/auth/realms";

  public int getOauthKeysRefreshPeriodMinutes() {
    return oauthKeysRefreshPeriodMinutes;
  }

  public void setOauthKeysRefreshPeriodMinutes(int oauthKeysRefreshPeriodMinutes) {
    this.oauthKeysRefreshPeriodMinutes = oauthKeysRefreshPeriodMinutes;
  }

  public String getKeycloakBaseUrl() {
    return keycloakBaseUrl;
  }

  public void setKeycloakBaseUrl(String keycloakBaseUrl) {
    this.keycloakBaseUrl = keycloakBaseUrl;
  }

}
