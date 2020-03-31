import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RegistrationConfigurationDTO } from '../models/registration-configuration-dto';
import { Observable } from 'rxjs';
import { AppConfigService } from '../app-config.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  iamApiBaseUrl: string;

  constructor(private http: HttpClient, private appConfigService: AppConfigService) {
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
          givenName: registrationForm.get('firstName').value,
          familyName: registrationForm.get('lastName').value,
          email: registrationForm.get('email').value
        }
      });
  }

  getRegistrationConfig(realm: string): Observable<RegistrationConfigurationDTO> {
    return this.http.get<RegistrationConfigurationDTO>(this.iamApiBaseUrl + '/Realms/' + realm + '/Registrations/config');
  }


}
