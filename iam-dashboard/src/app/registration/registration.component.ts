import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RealmService } from '../services/realm.service';
import { RealmDTO } from '../models/realm-dto';
import { RegistrationConfigurationDTO } from '../models/registration-configuration-dto';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  iamName: string = "test";
  realmName: string = "";
  realms: RealmDTO[];
  registrationConfiguration: RegistrationConfigurationDTO;


  constructor(private fb: FormBuilder, public registrationService: RegistrationService, private route: ActivatedRoute, private realmService: RealmService, private router: Router) {
    this.route.paramMap.subscribe((params) => {
      this.realmName = params.get('realm');
    });

    this.realmService.getRealms().subscribe(
      (response) => {
        this.realms = response.resources;
      },
      (error) => {
        console.error("Error loading realms from API " + error);
        this.realms = [];
      }
    )

    if(!this.realms.some((e) => e.name === this.realmName)) {
      // There are no realms with the given name, so lets 404 here...
      router.navigate(["/404"]);
    }

    this.registrationService.getRegistrationConfig(this.realmName).subscribe(
      (response) => {
        this.registrationConfiguration = response;
      },
      (error) => {
        console.error("Error loading registration configuration from API " + error);
      }
    )
  }

  ngOnInit(): void {
  }

  usernameInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const username = control.get('username');

    return username && this.registrationService.usernameExists(username.value) ? { 'usernameInUse': true } : null;
  };

  emailInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const email = control.get('email');

    return email && this.registrationService.emailExists(email.value) ? { 'emailInUse': true } : null;
  };

  RegistrationForm = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.email, Validators.required, this.emailInUseValidator]],
    username: ['', [Validators.required, this.usernameInUseValidator]],
    notes: ['', [Validators.required]],
    aup: [false, Validators.requiredTrue]
  });

  async register() {
    if(await this.registrationService.createRegistration(this.RegistrationForm)) {
      console.log("success");
    } else {
      console.log("oh no");
    }
  }

}
