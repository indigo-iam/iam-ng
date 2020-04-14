import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AppConfig} from './models/app-config';

@Injectable({providedIn: 'root'})
export class AppConfigService {
  private APP_CONFIG_RESOURCE = '/assets/app-config.json';

  public appConfig: AppConfig;

  constructor(private http: HttpClient) {
  }

  loadAppConfig() {
    return this.http.get<AppConfig>(this.APP_CONFIG_RESOURCE)
        .toPromise()
        .then(data => {
          this.appConfig = data;
        });
  }

  getConfig() {
    return this.appConfig;
  }

  getFallbackRealm(): string {
    return this.appConfig.fallbackRealm;
  }

  getIamApiBaseUrl(): string {
    return this.appConfig.iamApiBaseUrl;
  }

  getKeycloakBaseUrl(): string {
    return this.appConfig.keycloakBaseUrl;
  }

  getKeycloakClientId(): string {
    return this.appConfig.keycloakClientId;
  }

  getCustomAttribute(realm: string, type: string, key: string): string {
    const customAttributes = this.appConfig.customAttributes;

    if (customAttributes.hasOwnProperty(realm)) {
      if (customAttributes[realm].hasOwnProperty(type)) {
        if (customAttributes[realm][type].hasOwnProperty(key)) {
          return customAttributes[realm][type][key];
        }
      }
    }

    return null;
  }

  attributeExists(realm: string, type: string, key: string): boolean {
    if (this.getCustomAttribute(realm, type, key) !== null) {
      return true;
    } else {
      return false;
    }
  }

  getLoginOrder(realm: string) {
    const loginOrder = this.appConfig.loginOrder;
    if (loginOrder.hasOwnProperty(realm)) {
      return loginOrder[realm];
    }

    return null;
  }

}
