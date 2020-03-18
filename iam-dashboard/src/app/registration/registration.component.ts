import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  RegistrationForm = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.email, Validators.required]],
    username: ['', [Validators.required]],
    notes: ['', [Validators.required]],
    aup: [false, Validators.requiredTrue]
  });

  iamName = "test";
  auplink = "test";

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  register(): void {

  }

}
