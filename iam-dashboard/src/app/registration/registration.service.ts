import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor() { }

  usernameExists(username: String): boolean {
    return false;
  }

  emailExists(email: String): boolean {
    return false;
  }

  createRegistration(registrationForm: FormGroup): boolean {
    return true;
  }

}
