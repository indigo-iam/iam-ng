import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientManagementService } from './client-management.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { NewOrEditClientComponent } from '../new-or-edit-client/new-or-edit-client.component';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { ClientDetailsComponent } from '../client-details/client-details.component';
import { ConfirmationDialogComponent } from 'src/app/utils/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-client-management',
  templateUrl: './client-management.component.html',
  styleUrls: ['./client-management.component.scss']
})
export class ClientManagementComponent implements OnInit {

  clients;
  realmName: string;
  dataSource;

  displayedColumns: string[] = ['clientId', 'publicClient', 'redirectUris', 'edit'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @BlockUI('clientTable') blockUIclientTable: NgBlockUI;

  constructor(private clientManagementService: ClientManagementService, private sb: MatSnackBar, private route: ActivatedRoute, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      this.realmName = paramMap.get('realm');
    });

    this.updateTable();
  }

  updateTable() {
    this.blockUIclientTable.start();
    this.clientManagementService.getClients(this.realmName).subscribe(
      (response) => {
        this.clients = response;
        console.log(this.clients);
        this.dataSource = new MatTableDataSource(this.clients);
        this.dataSource.paginator = this.paginator;
        this.blockUIclientTable.stop();
      },
      (error) => {
        this.sb.open('An error occoured when getting clients: ' + error.message, 'Close');
        this.blockUIclientTable.stop();
      }
    );
  }

  editClient(clientId: string): void {
    const dialogRef = this.dialog.open(NewOrEditClientComponent, {
      data: {
        client: this.clients.find(client => client.clientId === clientId),
        realm: this.realmName
      },
      width: '100%'
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.updateTable();
    });
  }

  newClient(): void {
    const dialogRef = this.dialog.open(NewOrEditClientComponent, {
      data: {
        realm: this.realmName
      },
      width: '100%'
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.updateTable();
    });
  }

  deleteClient(clientId: string): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Are you sure you wish to delete this client?'
    });

    dialogRef.afterClosed().subscribe(
      (result) => {
        if(result) {
          this.clientManagementService.deleteClient(this.realmName, clientId).subscribe(
            (r) => {
              this.sb.open('Client successfully deleted', 'Close');
              this.updateTable();
            },
            (error) => {
              this.sb.open('Client could not be deleted!', 'Close');
            }
          );
        }
      }
    );
  }

  getClientSamlDetails(id: string): void {
    const client = this.clients.find(c => c.id === id);

    this.dialog.open(ClientDetailsComponent, {
      data: {
        samlCert: client.attributes['saml.signing.certificate'],
        samlKey: client.attributes['saml.signing.private.key'],
        samlAlgorithm: client.attributes['saml.signature.algorithm'],
        protocol: 'saml'
      },
      width: '100%'
    });
  }

  getClientSecret(id: string) {
    this.clientManagementService.getClientSecret(this.realmName, id).subscribe(
      (response: any) => {
        this.dialog.open(ClientDetailsComponent, {
          data: {
            secret: response.value,
            protocol: 'openid-connect'
          }
        });
      },
      (error: Error) => {
        this.sb.open('An error occoured: ' + error.message, 'Close');
      }
    );
  }

}
