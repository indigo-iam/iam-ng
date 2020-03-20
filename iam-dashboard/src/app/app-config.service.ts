import {HttpClient} from '@angular/common/http';
import {Injectable, Injector} from '@angular/core';
import {AppConfig} from './models/app-config';

@Injectable({providedIn: 'root'})
export class AppConfigService {
  private APP_CONFIG_RESOURCE = '/assets/app-config.json';

  private appConfig: AppConfig;

  constructor(private injector: Injector) {}

  loadAppConfig() {
    const http = this.injector.get(HttpClient);
    return http.get<AppConfig>(this.APP_CONFIG_RESOURCE)
        .toPromise()
        .then(data => {
          this.appConfig = data;
        });
  }

  get config() {
    return this.appConfig;
  }
}
