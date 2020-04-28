import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientManagementService } from './client-management.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { NewOrEditClientComponent } from '../new-or-edit-client/new-or-edit-client.component';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

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
        this.sb.open('An error occoured when getting clients: ' + error.message);
        this.blockUIclientTable.stop();
      }
    );
  }

  editClient(clientId: string): void {
    const dialogRef = this.dialog.open(NewOrEditClientComponent, {
      data: {
        client: this.clients.find(client => client.clientId === clientId),
        realm: this.realmName
      }
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.updateTable();
    });
  }

  newClient(): void {
    const dialogRef = this.dialog.open(NewOrEditClientComponent, {
      data: {
        realm: this.realmName
    }});

    dialogRef.afterClosed().subscribe((result) => {
      this.updateTable();
    });
  }

}
