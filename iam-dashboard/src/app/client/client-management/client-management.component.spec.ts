import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { ClientManagementComponent } from './client-management.component';
import { ClientManagementService } from './client-management.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { SAML_CLIENT_LIST } from 'src/app/utils/test-data';
import { RealmService } from 'src/app/services/realm.service';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule } from '@angular/material/paginator';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { BlockUIModule, BlockUIService } from 'ng-block-ui';

describe('ClientManagementComponent', () => {
  let component: ClientManagementComponent;
  let fixture: ComponentFixture<ClientManagementComponent>;
  let clientManagementService;
  let realmService;
  let sb;
  let dialog;

  beforeEach(async(() => {
    clientManagementService = jasmine.createSpyObj(['getClients', 'deleteClient', 'getClientSecret', 'getClientsOffset', 'searchClients']);
    clientManagementService.getClients.and.returnValue(of(
      SAML_CLIENT_LIST
    ));

    clientManagementService.getClientsOffset.and.returnValue(of(
      SAML_CLIENT_LIST
    ));

    realmService = jasmine.createSpyObj(['getCurrentRealm']);
    realmService.getCurrentRealm.and.returnValue(of('alice'));

    sb = jasmine.createSpyObj('snackbar', ['open']);
    dialog = jasmine.createSpyObj('dialog', ['open']);

    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        MatTableModule,
        MatPaginatorModule,
        NoopAnimationsModule,
        MatInputModule,
        BlockUIModule.forRoot()
      ],
      declarations: [
        ClientManagementComponent
      ],
      providers: [
        { provide: ClientManagementService, useValue: clientManagementService },
        { provide: ActivatedRoute, useValue: {
          paramMap: of(
            convertToParamMap(of(
              {
                realm: 'alice'
              }
            ))
          )
        }},
        { provide: MatSnackBar, useValue: sb },
        { provide: MatDialog, useValue: dialog },
        { provide: RealmService, useValue: realmService },
        BlockUIService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update table', () => {
    component.updateTable();
    expect(component.clients).toEqual(SAML_CLIENT_LIST);
  });

  it('should not update table on error', () => {
    clientManagementService.getClients.and.returnValue(throwError({status: 500, message: 'Internal server error'}));
    clientManagementService.getClientsOffset.and.returnValue(throwError({status: 500, message: 'Internal server error'}));
    component.updateTable();
    expect(sb.open).toHaveBeenCalled();
  });

  it('should open dialog when editing client', () => {
    const dialogRef = jasmine.createSpyObj(['afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(true));
    dialog.open.and.returnValue(dialogRef);
    component.editClient('test');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
  });

  it('should open dialog when creating client', () => {
    const dialogRef = jasmine.createSpyObj(['afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(true));
    dialog.open.and.returnValue(dialogRef);
    component.newClient();
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
  });

  it('should delete client', () => {
    const dialogRef = jasmine.createSpyObj(['afterClosed']);
    dialogRef.afterClosed.and.returnValue(of(true));
    dialog.open.and.returnValue(dialogRef);

    clientManagementService.deleteClient.and.returnValue(of({}));

    component.deleteClient('id');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();

    clientManagementService.deleteClient.and.returnValue(throwError({status: 500}));

    component.deleteClient('id');
    expect(dialog.open).toHaveBeenCalled();
    expect(dialogRef.afterClosed).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();
  });

  it('should open dialog when getting SAML details', () => {
    // SAML client ID is 7d48ec06-2ebe-41ed-8f33-8f8e2ae3096c

    component.getClientSamlDetails('7d48ec06-2ebe-41ed-8f33-8f8e2ae3096c');
    expect(dialog.open).toHaveBeenCalled();
  });

  it('should open dialog when getting client secret', () => {
    clientManagementService.getClientSecret.and.returnValue(of({value: 'secret123'}));
    /*clientManagementService.getClientSecret('a', 'b').subscribe(
      (response: any) => {
        console.log(response.value);
      },
      (error) => {
        console.log(error.message);
      }
    );*/
    component.getClientSecret('7d48ec06-2ebe-41ed-8f33-8f8e2ae3096c');
    expect(dialog.open).toHaveBeenCalled();
  });

  it('should not open dialog with error secret', () => {
    clientManagementService.getClientSecret.and.returnValue(throwError({status: 500, message: 'Internal server error'}));
    /*clientManagementService.getClientSecret('a', 'b').subscribe(
      (response: any) => {
        console.log(response.value);
      },
      (error) => {
        console.log(error.message);
      }
    );*/
    component.getClientSecret('7d48ec06-2ebe-41ed-8f33-8f8e2ae3096c');
    expect(dialog.open).not.toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();
  });

  it('should update table on page change event', () => {
    component.clients = [];
    component.getNext({pageSize: 10, pageIndex: 10, length: 10});
    expect(sb.open).not.toHaveBeenCalled();
    expect(component.clients).toEqual(SAML_CLIENT_LIST);
  });

  it('should not update table on page change event with http error', () => {
    component.clients = [];
    clientManagementService.getClientsOffset.and.returnValue(throwError({status: 500}));
    component.getNext({pageSize: 10, pageIndex: 10, length: 10});
    expect(sb.open).toHaveBeenCalled();
    expect(component.clients).toEqual([]);
  });

  /*
  * Important note: We use fakeAsync here because this function call (component.search()) includes a debounceTime - so we need to fake time passing
  * as the debounce time is 300ms we wait 301ms
  */
 it('should search for clients', fakeAsync(() => {
  component.search();
  component.clients = [];

  clientManagementService.searchClients.and.returnValue(of(
    SAML_CLIENT_LIST
  ));

  component.searchQuery.next('test');
  tick(301);

  expect(component.clients).toEqual(SAML_CLIENT_LIST);
}));

it('should search for clients, but fail', fakeAsync(() => {
  component.search();
  component.clients = [];

  clientManagementService.searchClients.and.returnValue(throwError({status: 500}));

  component.searchQuery.next('test');
  tick(301);

  expect(sb.open).toHaveBeenCalled();

  expect(component.clients).toEqual([]);
}));

});
