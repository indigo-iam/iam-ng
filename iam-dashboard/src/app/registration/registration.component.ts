import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RealmService } from '../services/realm.service';
import { RealmDTO } from '../models/realm-dto';
import { RegistrationConfigurationDTO } from '../models/registration-configuration-dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  iamName: string = "test";
  realmName: string = "";
  realms: RealmDTO[] = [];
  registrationConfiguration: RegistrationConfigurationDTO = {
    kind: "",
    registrationEnabled: true,
    privacyPolicyUrl: "",
    aupUrl: "",
    logoUrl: ""
  };
  dataLoaded: boolean = false;
  registrationSuccess: boolean = false;


  constructor(private fb: FormBuilder, public registrationService: RegistrationService, private route: ActivatedRoute, private realmService: RealmService, private router: Router, private snackBar: MatSnackBar) {

  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.realmName = params.get('realm');
    });

    this.realmService.getRealms().subscribe(
      (response) => {
        console.log(response);
        this.realms = response.resources;
        this.errorIfRealmNotDefined();
      },
      (error) => {
        console.error("Error loading realms from API " + error);
        this.snackBar.open("There was an error loading realms, are you connected to the Internet?");
        this.realms = [];
      }
    )

    this.registrationService.getRegistrationConfig(this.realmName).subscribe(
      (response) => {
        this.registrationConfiguration = response;
      },
      (error) => {
        console.error("Error loading registration configuration from API " + error);
        this.snackBar.open("There was an error loading registration information, are you connected to the Internet?");
      }
    );

    this.dataLoaded = true;
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

  register() {
    this.registrationService.createRegistration(this.RegistrationForm, this.realmName).subscribe(
      (response) => {
        if(response.message && response.message === "Request created") {
          this.registrationSuccess = true;
        } else if(response.error && response.error === "bad_request") {
          this.snackBar.open("There was an error during form submission. Please check you have entered all data in the form correctly and try again!");
        }
      },
      (error) => {
        this.snackBar.open("There was an error during form submission. Please check you have entered all data in the form correctly and try again!");
      }
    )
  }

  errorIfRealmNotDefined(): void {
    if(!this.realms.some((e) => e.name === this.realmName)) {
      // There are no realms with the given name, so lets 404 here...
      this.router.navigate(["/404"]);
    }
  }

}
