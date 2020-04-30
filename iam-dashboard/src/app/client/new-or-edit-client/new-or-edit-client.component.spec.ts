import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewOrEditClientComponent } from './new-or-edit-client.component';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { ClientManagementService } from '../client-management/client-management.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { of } from 'rxjs';

describe('NewOrEditClientComponent', () => {
  let component: NewOrEditClientComponent;
  let fixture: ComponentFixture<NewOrEditClientComponent>;
  let clientManagementService;

  beforeEach(async(() => {
    clientManagementService = jasmine.createSpyObj(['getClients', 'deleteClient', 'getClientSecret']);
    clientManagementService.getClients.and.returnValue(of(
      [{
        id: 'c476342d-3588-4877-b560-cd4c37b0fe65',
        clientId: 'account',
        name: '${client_account}',
        rootUrl: '${authBaseUrl}',
        baseUrl: '/realms/alice/account/',
        surrogateAuthRequired: false,
        enabled: true,
        alwaysDisplayInConsole: false,
        clientAuthenticatorType: 'client-secret',
        defaultRoles: [
          'view-profile',
          'manage-account'
        ],
        redirectUris: [
          '/realms/alice/account/*'
        ],
        webOrigins: [],
        notBefore: 0,
        bearerOnly: false,
        consentRequired: false,
        standardFlowEnabled: true,
        implicitFlowEnabled: false,
        directAccessGrantsEnabled: false,
        serviceAccountsEnabled: false,
        publicClient: false,
        frontchannelLogout: false,
        protocol: 'openid-connect',
        attributes: {},
        authenticationFlowBindingOverrides: {},
        fullScopeAllowed: false,
        nodeReRegistrationTimeout: 0,
        defaultClientScopes: [
          'web-origins',
          'role_list',
          'profile',
          'roles',
          'email'
        ],
        optionalClientScopes: [
          'address',
          'phone',
          'offline_access',
          'microprofile-jwt'
        ],
        access: {
          view: true,
          configure: true,
          manage: true
        }
      }]
    ));

    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        MatDialogModule
      ],
      declarations: [ NewOrEditClientComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} },
        { provide: ClientManagementService, useValue: clientManagementService},
        FormBuilder
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewOrEditClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
