import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationActionDialogComponent } from './registration-action-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { REGISTRATION_REQUESTS } from 'src/app/utils/test-data';
import { of, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RegistrationService } from '../registration/registration.service';
import { NotificationsService } from 'angular2-notifications';
import { FormBuilder } from '@angular/forms';

describe('RegistrationActionDialogComponent', () => {
  let component: RegistrationActionDialogComponent;
  let fixture: ComponentFixture<RegistrationActionDialogComponent>;
  let matDialogRef;
  let registrationService;
  let notificationsService;
  let httpClient;

  beforeEach(async(() => {
    matDialogRef = jasmine.createSpyObj(['close']);
    registrationService = jasmine.createSpyObj(['actionRegistrationRequest']);
    notificationsService = jasmine.createSpyObj(['create']);
    httpClient = jasmine.createSpyObj(['get', 'post']);

    TestBed.configureTestingModule({
      declarations: [ RegistrationActionDialogComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue:
          {
            selection: { selected: REGISTRATION_REQUESTS.resources },
            realmName: 'alice',
            type: 'reject'
          }
        },
        { provide: MatDialogRef, useValue: matDialogRef },
        { provide: HttpClient, useValue: httpClient },
        { provide: RegistrationService, useValue: registrationService },
        { provide: NotificationsService, useValue: notificationsService },
        FormBuilder
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationActionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should action accept and reject request', () => {

    registrationService.actionRegistrationRequest.and.returnValue(of({}));
    component.ReasonForm.setValue({reason: 'A valid reason'});

    component.action();
    expect(registrationService.actionRegistrationRequest).toHaveBeenCalled();
    expect(notificationsService.create).toHaveBeenCalled();

    registrationService.actionRegistrationRequest.and.returnValue(throwError({status: 500}));
    component.action();
    expect(component.ReasonForm.get('reason').hasError('error')).toEqual(true);

  });

  it('no should call close', () => {
    component.no();
    expect(matDialogRef.close).toHaveBeenCalled();
  })
});
