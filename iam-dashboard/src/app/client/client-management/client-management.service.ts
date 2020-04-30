import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AppConfigService } from 'src/app/app-config.service';

@Injectable({
  providedIn: 'root'
})
export class ClientManagementService {

  keycloakBaseUrl: string;

  constructor(private http: HttpClient, private appConfig: AppConfigService) {
    this.keycloakBaseUrl = this.appConfig.getKeycloakBaseUrl();
  }

  getClients(realm: string) {
    return this.http.get(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients');
  }

  createClient(realm: string, clientInfo) {
    return this.http.post(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients', clientInfo);
  }

  updateClient(realm: string, id: string, clientInfo) {
    return this.http.put(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients/' + id, clientInfo);
  }

  getClient(realm: string, id: string) {
    return this.http.get(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients/' + id);
  }

  getClientSecret(realm: string, id: string) {
    return this.http.get(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients/' + id + '/client-secret');
  }

  createSamlClient(realm: string, clientInfo) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/xml' // We send XML formatted data
      })
    };

    return this.http.post(this.keycloakBaseUrl + 'admin/realms/' + realm + '/client-description-converter', clientInfo, httpOptions);
  }

  deleteClient(realm: string, id: string) {
    return this.http.delete(this.keycloakBaseUrl + 'admin/realms/' + realm + '/clients/' + id);
  }

}
