import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationComponent } from './registration.component';
import { FormBuilder } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationService } from './registration.service';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let rs: RegistrationService;
  let spy: any;
  let fb: FormBuilder;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatToolbarModule,
        MatCardModule,
        MatCheckboxModule,
        MatInputModule,
        MatButtonModule,
        BrowserAnimationsModule
      ],
      declarations: [
        RegistrationComponent,
      ],
      providers: [
        FormBuilder,
        RegistrationService
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    rs = fixture.debugElement.injector.get(RegistrationService);
    fb = fixture.debugElement.injector.get(FormBuilder);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form invalid when empty', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is valid when all inputs are made', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeTruthy();
  })

  it('form is invalid with bad email', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is invalid with no AUP signed', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(false);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is invalid with no firstName', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is invalid with no lastName', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is invalid with no username', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['notes'].setValue("Requesting account");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('form is invalid with no notes', () => {
    expect(component.RegistrationForm.valid).toBeFalsy();
    component.RegistrationForm.controls['firstName'].setValue("John");
    component.RegistrationForm.controls['lastName'].setValue("Smith");
    component.RegistrationForm.controls['email'].setValue("example@example.com");
    component.RegistrationForm.controls['username'].setValue("JSmith");
    component.RegistrationForm.controls['aup'].setValue(true);
    expect(component.RegistrationForm.valid).toBeFalsy();
  })

  it('validator null with username not in use', () => {
    spy = spyOn(rs, 'usernameExists').and.returnValue(false);
    let fg = fb.group({
      username: [''],
    })
    expect(component.usernameInUseValidator(fg)).toBeNull();
    expect(rs.usernameExists).toHaveBeenCalled();
  })

  it('validator true with username in use', () => {
    spy = spyOn(rs, 'usernameExists').and.returnValue(true);
    let fg = fb.group({
      username: [''],
    })
    expect(component.usernameInUseValidator(fg)).toBeTruthy();
    expect(rs.usernameExists).toHaveBeenCalled();
  })

  it('validator null with email not in use', () => {
    spy = spyOn(rs, 'emailExists').and.returnValue(false);
    let fg = fb.group({
      email: [''],
    })
    expect(component.emailInUseValidator(fg)).toBeNull();
    expect(rs.emailExists).toHaveBeenCalled();
  })

  it('validator true with email in use', () => {
    spy = spyOn(rs, 'emailExists').and.returnValue(true);
    let fg = fb.group({
      email: [''],
    })
    expect(component.emailInUseValidator(fg)).toBeTruthy();
    expect(rs.emailExists).toHaveBeenCalled();
  })


});
