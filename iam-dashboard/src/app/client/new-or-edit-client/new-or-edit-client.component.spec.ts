import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewOrEditClientComponent } from './new-or-edit-client.component';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { ClientManagementService } from '../client-management/client-management.service';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { of, Observable, throwError } from 'rxjs';
import { SINGLE_CLIENT_LIST, SINGLE_CLIENT } from 'src/app/utils/client-test-data';
import { HttpErrorResponse } from '@angular/common/http';

describe('NewOrEditClientComponent', () => {
  let component: NewOrEditClientComponent;
  let fixture: ComponentFixture<NewOrEditClientComponent>;
  let clientManagementService;
  let matDialogRef;
  let fb: FormBuilder;
  let sb;

  beforeEach(async(() => {
    clientManagementService = jasmine.createSpyObj(['getClients', 'deleteClient', 'getClientSecret', 'createSamlClient', 'createClient', 'updateClient']);
    clientManagementService.getClients.and.returnValue(of(
      SINGLE_CLIENT_LIST
    ));

    clientManagementService.createClient.and.returnValue(of({}));
    clientManagementService.createSamlClient.and.returnValue(of({}));
    clientManagementService.updateClient.and.returnValue(of({}));

    matDialogRef = jasmine.createSpyObj(['open', 'close']);

    sb = jasmine.createSpyObj(['open']);

    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        MatDialogModule
      ],
      declarations: [ NewOrEditClientComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {
          client: SINGLE_CLIENT
        } },
        { provide: MatDialogRef, useValue: matDialogRef },
        { provide: ClientManagementService, useValue: clientManagementService},
        { provide: MatSnackBar, useValue: sb },
        FormBuilder
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewOrEditClientComponent);
    component = fixture.componentInstance;
    fb = fixture.debugElement.injector.get(FormBuilder);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog', () => {
    component.close();
    expect(matDialogRef.close).toHaveBeenCalled();
  });

  it('should set client protocol', () => {
    component.setClientProtocol('saml');
    expect(component.clientProtocol).toEqual('saml');
    expect(component.protocolChosen).toBeTrue();
  });

  it('should unset client protocol', () => {
    component.resetClientProtocol();
    expect(component.clientProtocol).toBeNull();
    expect(component.protocolChosen).toBeFalse();
  });

  it('should create form group', () => {
    const fg1ret = component.createFormGroup();
    expect(fg1ret.get('userInput').value).toEqual('');
    const fg2ret = component.createFormGroup('Test');
    expect(fg2ret.get('userInput').value).toEqual('Test');
  });

  it('should add and remove web origin', () => {
    component.addWebOrigin('Testwo');
    expect(component.webOrigins.at(component.webOrigins.length - 1).get('userInput').value).toEqual('Testwo');
    const length = component.webOrigins.length;
    component.removeWebOrigin(length - 1);
    expect(component.webOrigins.length).toEqual(length - 1);
  });

  it('should add and remove redirect uri', () => {
    component.addRedirectUri('Testri');
    expect(component.redirectUris.at(component.redirectUris.length - 1).get('userInput').value).toEqual('Testri');
    const length = component.redirectUris.length;
    component.removeRedirectUri(length - 1);
    expect(component.redirectUris.length).toEqual(length - 1);
  });

  it('should create array from formarray', () => {
    component.addRedirectUri('test');
    const retarray = component.formArrayToArray('redirectUris');
    expect(retarray).toEqual(['/realms/alice/account/*', 'test']);
  });

  it('should save existing client', () => {
    component.save();
    expect(sb.open).toHaveBeenCalled();
    expect(matDialogRef.close).toHaveBeenCalled();
  });

  it('should create new client', () => {
    component.newClient = true;
    component.save();
    expect(sb.open).toHaveBeenCalled();
    expect(matDialogRef.close).toHaveBeenCalled();
  });

  it('should create SAML client', () => {
    component.createSamlClient();
    expect(sb.open).toHaveBeenCalled();
    expect(matDialogRef.close).toHaveBeenCalled();
  });

  it('shouldnt create bad SAML client', () => {
    clientManagementService.createSamlClient.and.returnValue(throwError({status: 500}));
    component.createSamlClient();
    expect(sb.open).toHaveBeenCalled();
  });

});
