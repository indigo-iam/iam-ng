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

class MockRegistrationService {

}

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;

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
        { provide: RegistrationService, useClass: MockRegistrationService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  let registrationServiceSpy: { usernameInUse: jasmine.Spy, emailInUse: jasmine.Spy}

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    registrationServiceSpy = jasmine.createSpyObj('MockRegistrationService', ['usernameInUse', 'emailInUse'])
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

  it('validator false with username not in use', () => {
    registrationServiceSpy.usernameInUse.and.returnValue(false);
    component.RegistrationForm.controls['username'].setValue("JSmith");
    expect(component.usernameInUseValidator(component.RegistrationForm.controls['username'])).toBeFalsy();
    expect(registrationServiceSpy.usernameInUse.calls.count()).toBe(1, 'one call');
  })

  it('validator true with username not in use', () => {
    registrationServiceSpy.usernameInUse.and.returnValue(true);
    component.RegistrationForm.controls['username'].setValue("JSmith");
    expect(component.usernameInUseValidator(component.RegistrationForm.controls['username'])).toBeTruthy();
    expect(registrationServiceSpy.usernameInUse.calls.count()).toBe(1, 'one call');
  })


});
