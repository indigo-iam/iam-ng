import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationRequestsComponent } from './registration-requests.component';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { REGISTRATION_REQUESTS } from 'src/app/utils/test-data';
import { RegistrationService } from '../registration/registration.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { RealmService } from 'src/app/services/realm.service';
import { MatTableDataSource } from '@angular/material/table';

describe('RegistrationRequestsComponent', () => {
  let component: RegistrationRequestsComponent;
  let fixture: ComponentFixture<RegistrationRequestsComponent>;
  let registrationService;
  let realmService;
  let sb;
  let dialog;

  beforeEach(async(() => {
    registrationService = jasmine.createSpyObj(['getRegistrationRequestsPaginated', 'actionRegistrationRequest']);
    registrationService.getRegistrationRequestsPaginated.and.returnValue(of(
      REGISTRATION_REQUESTS
    ));

    realmService = jasmine.createSpyObj(['getCurrentRealm']);

    realmService.getCurrentRealm.and.returnValue(of('alice'));

    sb = jasmine.createSpyObj(['open']);

    dialog = jasmine.createSpyObj('dialog', ['open']);

    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        MatButtonModule,
        MatDialogModule
      ],
      declarations: [ RegistrationRequestsComponent ],
      providers: [
        MatSnackBar,
        { provide: RegistrationService, useValue: registrationService },
        { provide: RealmService, useValue: realmService },
        { provide: MatSnackBar, useValue: sb },
        { provide: MatDialog, useValue: dialog }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should action accept and reject request', () => {
    const dialogRef = jasmine.createSpyObj(['afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(true));
    dialog.open.and.returnValue(dialogRef);

    registrationService.actionRegistrationRequest.and.returnValue(of({}));

    component.actionRequest('id', 'accept');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();

    registrationService.actionRegistrationRequest.and.returnValue(throwError({status: 500}));

    component.actionRequest('id', 'accept');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();

    registrationService.actionRegistrationRequest.and.returnValue(of({}));

    component.actionRequest('id', 'reject');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();
  });

  it('should not do anything when dialog closed', () => {
    const dialogRef = jasmine.createSpyObj(['afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(false));
    dialog.open.and.returnValue(dialogRef);
    component.actionRequest('id', 'reject');
    expect(sb.open).not.toHaveBeenCalled();
  });

  it('should update table on page change event', () => {
    component.registrationRequests = [];
    component.getNext({pageSize: 10, pageIndex: 10, length: 10});
    expect(sb.open).not.toHaveBeenCalled();
    expect(component.registrationRequests).toEqual(REGISTRATION_REQUESTS.resources);
  });

  it('should not update table on page change event with http error', () => {
    component.registrationRequests = [];
    registrationService.getRegistrationRequestsPaginated.and.returnValue(throwError({status: 500}));
    component.getNext({pageSize: 10, pageIndex: 10, length: 10});
    expect(sb.open).toHaveBeenCalled();
    expect(component.registrationRequests).toEqual([]);
  });
});
