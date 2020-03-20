import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';

import {AppConfigService} from './app-config.service';
import {AppConfig} from './models/app-config';
import {ListResponseDTO} from './models/list-response-dto';
import {Realm} from './models/realm';

@Injectable({providedIn: 'root'})
export class RealmsService {
  constructor(private http: HttpClient, private appConfig: AppConfigService) {}

  public getRealms(): Observable<ListResponseDTO<Realm>> {
    const appConfig: AppConfig = this.appConfig.config;
    return this.http.get<ListResponseDTO<Realm>>(
        appConfig.iamApiBaseUrl + '/Realms');
  }
}
