import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, Validators } from '@angular/forms';
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

  @BlockUI('clientForm') blockUIclientForm: NgBlockUI;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, public dialogRef: MatDialogRef<NewOrEditClientComponent>, private clientManagementService: ClientManagementService, private sb: MatSnackBar) { }

  ngOnInit(): void {
    this.realmName = this.data.realm;

    if (this.data.client) {
      this.ClientForm.setValue({
        id: this.data.client.clientId ? this.data.client.clientId : '',
        name: this.data.client.name ? this.data.client.name : '',
        homePage: this.data.client.redirectUris[0] ? this.data.client.redirectUris[0] : '',
        description: this.data.client.description ? this.data.client.description : '',
        public: this.data.client.publicClient ? this.data.client.publicClient : true
      });
      this.newClient = false;
    } else {
      this.newClient = true;
    }
  }

  ClientForm = this.fb.group({
    id: ['', [Validators.required]],
    name: ['', []],
    homePage: ['', [Validators.required]],
    description: ['', []],
    public: ['', [Validators.required]],
  });

  save() {
    this.blockUIclientForm.start();
    const clientRepresentation = {
      clientId: this.ClientForm.get('id').value,
      description: this.ClientForm.get('description').value,
      enabled: true,
      name: this.ClientForm.get('name').value,
      redirectUris: [this.ClientForm.get('homePage').value],
      webOrigins: [this.ClientForm.get('homePage').value],
      publicClient: this.ClientForm.get('public').value
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

  close() {
    this.blockUIclientForm.stop();
    this.dialogRef.close();
  }

}
