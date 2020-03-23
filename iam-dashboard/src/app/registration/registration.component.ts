import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';
import { RegistrationService } from './registration.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  iamName: string = "test";
  auplink: string  = "test";

  realm: string = "";

  constructor(private fb: FormBuilder, public rs: RegistrationService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe((params) => {
      this.realm = params.get('realm');
    })
  }

  ngOnInit(): void {
  }

  usernameInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const username = control.get('username');

    return username && this.rs.usernameExists(username.value) ? { 'usernameInUse': true } : null;
  };

  emailInUseValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const email = control.get('email');

    return email && this.rs.emailExists(email.value) ? { 'emailInUse': true } : null;
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
    if(await this.rs.createRegistration(this.RegistrationForm)) {
      console.log("success");
    } else {
      console.log("oh no");
    }
  }

}
