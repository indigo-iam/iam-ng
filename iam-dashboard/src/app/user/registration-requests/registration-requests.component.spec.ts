import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationRequestsComponent } from './registration-requests.component';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { REGISTRATION_REQUESTS } from 'src/app/utils/test-data';
import { RegistrationService } from '../registration/registration.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { RealmService } from 'src/app/services/realm.service';

describe('RegistrationRequestsComponent', () => {
  let component: RegistrationRequestsComponent;
  let fixture: ComponentFixture<RegistrationRequestsComponent>;
  let registrationService;
  let realmService;

  beforeEach(async(() => {
    registrationService = jasmine.createSpyObj(['getRegistrationRequestsPaginated']);
    registrationService.getRegistrationRequestsPaginated.and.returnValue(of(
      REGISTRATION_REQUESTS
    ));

    realmService = jasmine.createSpyObj(['getCurrentRealm']);

    realmService.getCurrentRealm.and.returnValue(of('alice'));

    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        MatButtonModule,
        MatDialogModule
      ],
      declarations: [ RegistrationRequestsComponent ],
      providers: [
        MatSnackBar,
        { provide: RegistrationService, useValue: registrationService},
        { provide: RealmService, useValue: realmService}
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
});
