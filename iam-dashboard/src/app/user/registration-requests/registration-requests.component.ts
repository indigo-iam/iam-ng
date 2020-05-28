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
import { SelectionModel } from '@angular/cdk/collections';
import { DEFAULT_PAGE_EVENT, UpdateableTableData } from 'src/app/utils/utils';
import { UpdateableTableService } from 'src/app/services/updateable-table.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-registration-requests',
  templateUrl: './registration-requests.component.html',
  styleUrls: ['./registration-requests.component.scss']
})
export class RegistrationRequestsComponent implements OnInit {

  realmName: string;

  dataSource;
  displayedColumns: string[] = ['select', 'username', 'givenName', 'familyName', 'email', 'submitted', 'notes', 'approve'];
  registrationRequests;
  selection: SelectionModel<any>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @BlockUI('registrationRequestsTable') blockUIregistrationRequestsTable: NgBlockUI;

  constructor(private sb: MatSnackBar, private registrationService: RegistrationService, private dialog: MatDialog, private realmService: RealmService, private updateableTableService: UpdateableTableService) { }

  ngOnInit(): void {
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      this.getNext(DEFAULT_PAGE_EVENT);
      this.selection = new SelectionModel(true, []);
    });

  }

  getNext(event: PageEvent) {
    const res: Subject<UpdateableTableData> = this.updateableTableService.getNext(event, this.blockUIregistrationRequestsTable, this.realmName, this.paginator, this.registrationService);
    res.subscribe(r => {
      this.dataSource = r.dataSource;
      this.registrationRequests = r.resources;
    });
  }

  actionRequest(requestId: string, type: string): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        message: 'Are you sure you wish to ' + type + ' this request? Please enter a reason that will be sent to the user.',
        showInput: true
      }
    });

    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {

          const reason = result.userInput;

          let action: Action;
          if (type === 'reject') {
            action = ACTION_REJECT;
          } else {
            action = ACTION_APPROVE;
          };

          this.registrationService.actionRegistrationRequest(this.realmName, requestId, action, reason).subscribe(
            (r) => {
              this.sb.open('Request ' + type + 'ed successfully', 'Close');
              this.getNext(DEFAULT_PAGE_EVENT);
            },
            (error) => {
              this.sb.open('Request modification was unsuccessful ' + error.message, 'Close');
            }
          );
        }
      }
    );
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  actionSelected(type: string) {
    if (this.selection.selected.length > 0) {
      let selectionNames = '';
      this.selection.selected.forEach(sel => {
        selectionNames += '' + sel.requesterInfo.givenName + ' ' + sel.requesterInfo.familyName + ' (' + sel.requesterInfo.username + ')<br />';
      });
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          message: 'Are you sure you wish to ' + type + ' the following requests? Please enter a reason that will be sent to the user(s). <br />' + selectionNames,
          showInput: true
        }
      });

      dialogRef.afterClosed().subscribe(
        (result) => {
          if (result) {

            const reason = result.userInput;

            let action: Action;
            if (type === 'reject') {
              action = ACTION_REJECT;
            } else {
              action = ACTION_APPROVE;
            };

            this.selection.selected.forEach(sel => {
              console.log(sel);
              this.registrationService.actionRegistrationRequest(this.realmName, sel.uuid, action, reason).subscribe(
                (r) => {
                  // Do nothing on success
                },
                (error) => {
                  console.log(error);
                  this.sb.open('Request modification was unsuccessful ' + error.message, 'Close');
                }
              );
            });
          }
        }
      );
    } else {
      this.sb.open('Select a request to approve or reject!', 'Close');
    }
  }

}
