import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { RegistrationService } from '../registration/registration.service';
import { ConfirmationDialogComponent } from 'src/app/utils/confirmation-dialog/confirmation-dialog.component';
import { ACTION_REJECT, Action, ACTION_APPROVE } from './registration-request-action';
import { MatDialog } from '@angular/material/dialog';
import { RealmService } from 'src/app/services/realm.service';

@Component({
  selector: 'app-registration-requests',
  templateUrl: './registration-requests.component.html',
  styleUrls: ['./registration-requests.component.scss']
})
export class RegistrationRequestsComponent implements OnInit {

  realmName: string;

  dataSource;
  displayedColumns: string[] = ['username', 'givenName', 'familyName', 'email', 'submitted', 'approve'];
  registrationRequests;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @BlockUI('registrationRequestsTable') blockUIregistrationRequestsTable: NgBlockUI;

  defaultPageEvent: PageEvent = {pageIndex: 0, pageSize: 10, length: 10};

  constructor(private sb: MatSnackBar, private registrationService: RegistrationService, private dialog: MatDialog, private realmService: RealmService) { }

  ngOnInit(): void {
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      this.getNext(this.defaultPageEvent);
    });
  }

  getNext(event: PageEvent) {
    this.blockUIregistrationRequestsTable.start();
    const offset = event.pageSize * event.pageIndex;
    this.registrationService.getRegistrationRequestsPaginated(this.realmName, offset, event.pageSize).subscribe(
      (response: any) => {
        this.registrationRequests = response.resources;
        this.dataSource = new MatTableDataSource(this.registrationRequests);
        this.dataSource.paginator = this.paginator;
        this.blockUIregistrationRequestsTable.stop();
      },
      (error) => {
        this.sb.open('An error occoured changing pages: ' + error.message, 'Close');
        this.blockUIregistrationRequestsTable.stop();
      }
    );
  }

  actionRequest(requestId: string, type: string): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Are you sure you wish to ' + type + ' this request?'
    });

    dialogRef.afterClosed().subscribe(
      (result) => {
        if(result) {

          let action: Action;
          if (type === 'reject') {
            action = ACTION_REJECT;
          } else {
            action = ACTION_APPROVE;
          };

          this.registrationService.actionRegistrationRequest(this.realmName, requestId, action).subscribe(
            (r) => {
              this.sb.open('Request ' + type + 'ed successfully', 'Close');
              this.getNext(this.defaultPageEvent);
            },
            (error) => {
              this.sb.open('Request modification was unsuccessful ' + error.message, 'Close');
            }
          );
        }
      }
    );
  }

}
