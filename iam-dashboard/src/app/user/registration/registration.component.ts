import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RealmService } from '../../services/realm.service';
import { RealmDTO } from '../../models/realm-dto';
import { RegistrationConfigurationDTO } from '../../models/registration-configuration-dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CookieService } from 'ngx-cookie-service';
import { AppConfigService } from '../../app-config.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  iamName = 'test';
  realmName = '';
  realms: RealmDTO[] = [];
  registrationConfiguration: RegistrationConfigurationDTO = {
    kind: '',
    registrationEnabled: true,
    privacyPolicyUrl: '',
    aupUrl: '',
    logoUrl: ''
  };
  dataLoaded = false;
  registrationSuccess = false;
  iamLogo = 'https://fakeimg.pl/200/';

  @BlockUI('regform') blockUIRegForm: NgBlockUI;

  constructor(private fb: FormBuilder, public registrationService: RegistrationService, private route: ActivatedRoute, private realmService: RealmService, private router: Router, private snackBar: MatSnackBar, private cookieService: CookieService, private appConfigService: AppConfigService) {

  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      this.realmName = paramMap.get('realm');
    });

    this.realmService.getRealms().subscribe(
      (response) => {
        this.realms = response.resources;
        this.errorIfRealmNotDefined();
      },
      (error) => {
        console.error('Error loading realms from API ' + error.message);
        this.snackBar.open('There was an error loading realms, are you connected to the Internet?');
        this.realms = [];
      }
    );

    this.registrationService.getRegistrationConfig(this.realmName).subscribe(
      (response) => {
        this.registrationConfiguration = response;
      },
      (error) => {
        console.error('Error loading registration configuration from API ' + error.message);
        this.snackBar.open('There was an error loading registration information, are you connected to the Internet?');
      }
    );

    this.dataLoaded = true;
  }

  usernameInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const username = control.get('username');

    return username && this.registrationService.usernameExists(username.value) ? { usernameInUse: true } : null;
  }

  emailInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const email = control.get('email');

    return email && this.registrationService.emailExists(email.value) ? { emailInUse: true } : null;
  }

  RegistrationForm = this.fb.group({
    givenName: ['', [Validators.required]],
    familyName: ['', [Validators.required]],
    email: ['', [Validators.email, Validators.required, this.emailInUseValidator]],
    username: ['', [Validators.required, this.usernameInUseValidator]],
    notes: ['', [Validators.required]],
    aup: [false, Validators.requiredTrue]
  });

  register() {
    this.blockUIRegForm.start();
    this.registrationService.createRegistration(this.RegistrationForm, this.realmName).subscribe(
      (response) => {
        console.log(response);
        this.blockUIRegForm.stop();
        if (response.message && response.message === 'Request created') {
          this.registrationSuccess = true;
          this.cookieService.set('requestId', response.requestId);
          this.cookieService.set('requestChallenge', response.requestChallenge);
        }
      },
      (error) => {
        console.log(error);
        this.blockUIRegForm.stop();
        if (error.error && error.error.error === 'bad_request') {
          if (error.error.fieldErrors.length !== 0) {
              error.error.fieldErrors.forEach(element => {
                const control = element.fieldName.split('.')[2];
                this.RegistrationForm.get(control).setErrors({error: true, message: element.fieldError});
              });
          } else {
            this.snackBar.open('There was an error during form submission. Please check you have entered all data in the form correctly and try again!', 'Close');
          }
        } else {
          this.snackBar.open('There was an error during form submission. Please check you have entered all data in the form correctly and try again!', 'Close');
        }
      }
    );
  }

  errorIfRealmNotDefined(): void {
    if (!this.realms.some((e) => e.name === this.realmName)) {
      // There are no realms with the given name, so lets 404 here...
      this.router.navigate(['/404']);
    }
  }

  getCustomAttribute(key: string): string {
    return this.appConfigService.getCustomAttribute(this.realmName, 'registration', key);
  }

  atttributeExists(key: string): boolean {
    return this.appConfigService.attributeExists(this.realmName, 'registration', key);
  }

}
