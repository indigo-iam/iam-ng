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

  usernameExists(username: String): boolean {
    return false;
  }

  emailExists(email: String): boolean {
    return false;
  }

  createRegistration(registrationForm: FormGroup): boolean {
    return true;
  }

  getRegistrationConfig(realm: String): Observable<RegistrationConfigurationDTO> {
    return this.http.get<RegistrationConfigurationDTO>(this.iamApiBaseUrl + "/Realms/" + realm + "/Registrations/config");
  }


}
