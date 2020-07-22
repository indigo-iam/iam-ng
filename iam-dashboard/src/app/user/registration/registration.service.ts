import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RegistrationConfigurationDTO } from '../../models/registration-configuration-dto';
import { Observable } from 'rxjs';
import { AppConfigService } from '../../app-config.service';
import { Action } from '../registration-requests/registration-request-action';
import { PaginatedService } from 'src/app/utils/paginated.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends PaginatedService {

  iamApiBaseUrl: string;

  constructor(private http: HttpClient, private appConfigService: AppConfigService) {
    super();
    this.iamApiBaseUrl = this.appConfigService.getIamApiBaseUrl();
  }

  usernameExists(username: string): boolean {
    return false;
  }

  emailExists(email: string): boolean {
    return false;
  }

  createRegistration(registrationForm: FormGroup, realm: string): Observable<any> {
    return this.http.post<any>(this.iamApiBaseUrl + '/Realms/' + realm + '/Registrations',
      {
        requesterInfo: {
          username: registrationForm.get('username').value,
          givenName: registrationForm.get('givenName').value,
          familyName: registrationForm.get('familyName').value,
          email: registrationForm.get('email').value
        },
        messages: [
          {
            message: registrationForm.get('notes').value
          }
        ]
      });
  }

  getPaginated(realm: string, startIndex: number, count: number) {
    return this.http.get(this.iamApiBaseUrl + '/Realms/' + realm + '/Requests/registration/pending?startIndex=' + startIndex + '&count=' + count);
  }

  getRegistrationConfig(realm: string): Observable<RegistrationConfigurationDTO> {
    return this.http.get<RegistrationConfigurationDTO>(this.iamApiBaseUrl + '/Realms/' + realm + '/Registrations/config');
  }

  confirmEmail(token: string, realm: string): Observable<any> {
    return this.http.post<any>(this.iamApiBaseUrl + '/Realms/' + realm + '/Registrations/confirm/' + token, {});
  }

  actionRegistrationRequest(realm: string, requestId: string, decision: Action, reason: string) {
    return this.http.post<any>(this.iamApiBaseUrl + '/Realms/' + realm + '/Requests/registration/' + requestId, {decision: decision.action, message: reason});
  }


}
