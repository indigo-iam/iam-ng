import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { ClientManagementService } from '../client-management/client-management.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
  selector: 'app-new-or-edit-client',
  templateUrl: './new-or-edit-client.component.html',
  styleUrls: ['./new-or-edit-client.component.scss']
})
export class NewOrEditClientComponent implements OnInit {

  newClient: boolean;
  id: string;
  name: string;
  homePage: string;
  description: string;
  public: boolean;
  realmName: string;
  webOrigins: FormArray;
  redirectUris: FormArray;
  openIdForm: FormGroup;
  clientProtocol: string;
  protocolChosen = false;
  samlForm: FormGroup;

  @BlockUI('clientForm') blockUIclientForm: NgBlockUI;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, public dialogRef: MatDialogRef<NewOrEditClientComponent>, private clientManagementService: ClientManagementService, private sb: MatSnackBar) { }

  ngOnInit(): void {
    this.realmName = this.data.realm;

    this.samlForm = this.fb.group({
      xml: ['', [Validators.required]]
    })

    this.openIdForm = this.fb.group({
      id: ['', [Validators.required]],
      name: ['', []],
      homePage: ['', []],
      webOrigins: this.fb.array([]),
      redirectUris: this.fb.array([]),
      description: ['', []],
      public: ['', []]
    });

    if (this.data.client) {
      this.openIdForm.patchValue({
        id: this.data.client.clientId ? this.data.client.clientId : '',
        name: this.data.client.name ? this.data.client.name : '',
        homePage: this.data.client.adminUrl ? this.data.client.adminUrl : '',
        description: this.data.client.description ? this.data.client.description : '',
        public: this.data.client.publicClient ? this.data.client.publicClient : true
      });

      if (this.data.client.redirectUris) {
        this.data.client.redirectUris.forEach(uri => {
          this.addRedirectUri(uri);
        });
      } else {
        this.addRedirectUri();
      }

      if (this.data.client.webOrigins) {
        this.data.client.webOrigins.forEach(origin => {
          this.addWebOrigin(origin);
        });
      } else {
        this.addWebOrigin();
      }

      this.newClient = false;
    } else {
      this.newClient = true;
    }
  }

  setClientProtocol(protocol: string) {
    this.clientProtocol = protocol;
    this.protocolChosen = true;
  }

  resetClientProtocol() {
    this.clientProtocol = null;
    this.protocolChosen = false;
  }

  createFormGroup(input: string = ''): FormGroup {
    return this.fb.group({
      userInput: input
    });
  }

  addWebOrigin(origin: string = ''): void {
    this.webOrigins = this.openIdForm.get('webOrigins') as FormArray;
    this.webOrigins.push(this.createFormGroup(origin));
  }

  addRedirectUri(uri: string = ''): void {
    this.redirectUris = this.openIdForm.get('redirectUris') as FormArray;
    this.redirectUris.push(this.createFormGroup(uri));
  }

  removeRedirectUri(index: number): void {
    this.redirectUris = this.openIdForm.get('redirectUris') as FormArray;
    this.redirectUris.removeAt(index);
  }

  removeWebOrigin(index: number): void {
    this.webOrigins = this.openIdForm.get('webOrigins') as FormArray;
    this.webOrigins.removeAt(index);
  }

  formArrayToArray(arrayName: string): Array<string> {
    const arr = this.openIdForm.get(arrayName) as FormArray;
    const finalArray = [];
    for (const control of arr.controls) {
      finalArray.push(control.value.userInput);
    }
    return finalArray;
  }

  save() {
    this.blockUIclientForm.start();
    const clientRepresentation = {
      adminUrl: this.openIdForm.get('homePage').value,
      clientId: this.openIdForm.get('id').value,
      description: this.openIdForm.get('description').value,
      enabled: true,
      name: this.openIdForm.get('name').value,
      redirectUris: this.formArrayToArray('redirectUris'),
      webOrigins: this.formArrayToArray('webOrigins'),
      publicClient: this.openIdForm.get('public').value
    };

    if (this.newClient) {
      this.clientManagementService.createClient(this.realmName, clientRepresentation).subscribe(
        (response) => {
          this.blockUIclientForm.stop();
          this.sb.open('Client successfully added', 'Close');
          this.dialogRef.close();
        }
      );
    } else {
      this.clientManagementService.updateClient(this.realmName, this.data.client.id, clientRepresentation).subscribe(
        (response) => {
          this.blockUIclientForm.stop();
          this.sb.open('Client successfully updated', 'Close');
          this.dialogRef.close();
        }
      );
    }
  }

  createSamlClient() {
    this.blockUIclientForm.start();
    this.clientManagementService.createSamlClient(this.realmName, this.samlForm.get('xml').value).subscribe(
      (response) => {
        this.clientManagementService.createClient(this.realmName, response).subscribe(
          (r) => {
            this.blockUIclientForm.stop();
            this.sb.open('Client successfully added', 'Close');
            this.dialogRef.close();
          }
        )
      },
      (error) => {
        this.blockUIclientForm.stop();
        this.sb.open('There was an error with your SP metadata!');
      }
    )
  }

  close() {
    this.blockUIclientForm.stop();
    this.dialogRef.close();
  }

}
