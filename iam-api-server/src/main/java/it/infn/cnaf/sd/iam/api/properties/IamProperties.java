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
package it.infn.cnaf.sd.iam.api.properties;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.Email;
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

  public static class RequestsProperties {

    @Email
    @NotBlank
    String adminEmailAddress = "admin@example.io";

    public String getAdminEmailAddress() {
      return adminEmailAddress;
    }

    public void setAdminEmailAddress(String adminEmailAddress) {
      this.adminEmailAddress = adminEmailAddress;
    }
  }

  public static class NotificationProperties {

    boolean disable = false;

    @Email
    String from;

    boolean cleanupDeliveredNotifications = true;

    @Min(value = 0)
    int deliveredNotificationsRetentionPeriodDays = 30;

    @Min(value = 5)
    int deliveryTaskPeriodSeconds = 30;

    public boolean isDisable() {
      return disable;
    }

    public void setDisable(boolean disable) {
      this.disable = disable;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public boolean isCleanupDeliveredNotifications() {
      return cleanupDeliveredNotifications;
    }

    public void setCleanupDeliveredNotifications(boolean cleanupDeliveredNotifications) {
      this.cleanupDeliveredNotifications = cleanupDeliveredNotifications;
    }

    public int getDeliveredNotificationsRetentionPeriodDays() {
      return deliveredNotificationsRetentionPeriodDays;
    }

    public void setDeliveredNotificationsRetentionPeriodDays(
        int deliveredNotificationsRetentionPeriodDays) {
      this.deliveredNotificationsRetentionPeriodDays = deliveredNotificationsRetentionPeriodDays;
    }

    public int getDeliveryTaskPeriodSeconds() {
      return deliveryTaskPeriodSeconds;
    }

    public void setDeliveryTaskPeriodSeconds(int deliveryTaskPeriodSeconds) {
      this.deliveryTaskPeriodSeconds = deliveryTaskPeriodSeconds;
    }
  }

  public static class CorsProperties {
    boolean allowAllOrigins;

    List<String> allowedOrigins = newArrayList();

    public boolean isAllowAllOrigins() {
      return allowAllOrigins;
    }

    public void setAllowAllOrigins(boolean allowAllOrigins) {
      this.allowAllOrigins = allowAllOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
      this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedOrigins() {
      return allowedOrigins;
    }
  }

  // This needs to be constant, that's why TimeUnit is not used for the conversion
  public static final int T_48_HOURS_IN_MINUTES = 48 * 60;

  @Min(value = 0, message = "Please provide a zero or positive OAuth keys refresh period")
  @Max(value = T_48_HOURS_IN_MINUTES,
      message = "Please provide a keys refresh period shorter than 48 hours (in minutes)")
  int oauthKeysRefreshPeriodMinutes = (int) TimeUnit.HOURS.toMinutes(1);

  @NotBlank(message = "Please provide a keycloak base URL")
  String keycloakBaseUrl = "http://localhost:8080/auth";

  @NotBlank(message = "Please provide a keycloak admin base URL")
  String keycloakAdminBaseUrl = "http://localhost:8080/auth";

  @NotBlank(message = "Please provide a base URL for this API server")
  String apiBaseUrl = "http://localhost:9876";

  @NotBlank(message = "Please provide a base URL for the IAM dashboard")
  String dashboardBaseUrl = "http://localhost:4200";

  CorsProperties cors = new CorsProperties();

  @Valid
  NotificationProperties notification = new NotificationProperties();

  @Valid
  RequestsProperties requests = new RequestsProperties();

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

  public String getKeycloakAdminBaseUrl() {
    return keycloakAdminBaseUrl;
  }

  public void setKeycloakAdminBaseUrl(String keycloakAdminBaseUrl) {
    this.keycloakAdminBaseUrl = keycloakAdminBaseUrl;
  }

  public String getApiBaseUrl() {
    return apiBaseUrl;
  }

  public void setApiBaseUrl(String apiBaseUrl) {
    this.apiBaseUrl = apiBaseUrl;
  }

  public String getDashboardBaseUrl() {
    return dashboardBaseUrl;
  }

  public void setDashboardBaseUrl(String dashboardBaseUrl) {
    this.dashboardBaseUrl = dashboardBaseUrl;
  }

  public CorsProperties getCors() {
    return cors;
  }

  public void setCors(CorsProperties cors) {
    this.cors = cors;
  }

  public NotificationProperties getNotification() {
    return notification;
  }

  public void setNotification(NotificationProperties notification) {
    this.notification = notification;
  }

  public RequestsProperties getRequests() {
    return requests;
  }

  public void setRequests(RequestsProperties requests) {
    this.requests = requests;
  }

}
