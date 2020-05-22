import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from '../app-config.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  iamApiBaseUrl: string;
  constructor(private httpClient: HttpClient, private appConfigService: AppConfigService) { 
    this.iamApiBaseUrl = appConfigService.getIamApiBaseUrl();
  }

  getUsersPaginated(realm: string, startIndex: number, count: number) {
    return this.httpClient.get(this.iamApiBaseUrl + '/Realms/' + realm + '/Users?count=' + count + '&startIndex=' + startIndex);
  }

  getUser(realm: string, userId: string) {
    return this.httpClient.get(this.iamApiBaseUrl + '/Realms/' + realm + '/Users/' + userId);
  }
}
