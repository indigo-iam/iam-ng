import { Component, OnInit, ViewChild, Input, OnDestroy } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { RegistrationService } from '../registration/registration.service';
import { ConfirmationDialogComponent } from 'src/app/utils/confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { RealmService } from 'src/app/services/realm.service';
import { SelectionModel } from '@angular/cdk/collections';
import { DEFAULT_PAGE_EVENT, UpdateableTableData } from 'src/app/utils/utils';
import { UpdateableTableService } from 'src/app/services/updateable-table.service';
import { Subject, interval } from 'rxjs';
import { NotificationsService } from 'angular2-notifications';
import { RegistrationActionDialogComponent } from '../registration-action-dialog/registration-action-dialog.component';

@Component({
  selector: 'app-registration-requests',
  templateUrl: './registration-requests.component.html',
  styleUrls: ['./registration-requests.component.scss']
})
export class RegistrationRequestsComponent implements OnInit, OnDestroy {

  realmName: string;

  dataSource;
  displayedColumns: string[] = ['select', 'username', 'givenName', 'familyName', 'email', 'submitted', 'approve'];
  registrationRequests;
  selection: SelectionModel<any>;
  source = interval(60000); // refresh every 60 seconds
  subscription;
  dialogOpen = false;
  messages = '<p>Click on a row to see a users request message(s)</p>';

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @BlockUI('registrationRequestsTable') blockUIregistrationRequestsTable: NgBlockUI;

  constructor(private registrationService: RegistrationService, private dialog: MatDialog, private realmService: RealmService, private updateableTableService: UpdateableTableService) { }

  ngOnInit(): void {
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      this.getNext(DEFAULT_PAGE_EVENT);
      this.selection = new SelectionModel(true, []);
    });

    this.subscription = this.source.subscribe(() => {
      // Only refresh if we don't have any requests selected, otherwise the refresh will overwrite our selection!
      if (this.selection.selected.length === 0 && !this.dialogOpen) {
        this.getNext(DEFAULT_PAGE_EVENT);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  getNext(event: PageEvent) {
    const res: Subject<UpdateableTableData> = this.updateableTableService.getNext(event, this.blockUIregistrationRequestsTable, this.realmName, this.paginator, this.registrationService);
    res.subscribe(r => {
      this.dataSource = r.dataSource;
      this.registrationRequests = r.resources;
    });
  }

  actionRequest(requestId: string, type: string): void {
    const dialogRef = this.dialog.open(RegistrationActionDialogComponent, {
      data: {
        message: 'Are you sure you wish to ' + type + ' this request? Please enter a reason that will be sent to the user.',
        showInput: true,
        selection: { selected: [this.registrationRequests.find(req => req.uuid === requestId)] },
        type,
        realmName: this.realmName
      }
    });

    this.dialogOpen = true;

    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {
          this.getNext(DEFAULT_PAGE_EVENT);
        }
        this.dialogOpen = false;
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
    let selectionNames = '';
    this.selection.selected.forEach(sel => {
      selectionNames += '' + sel.requesterInfo.givenName + ' ' + sel.requesterInfo.familyName + ' (' + sel.requesterInfo.username + ')<br />';
    });
    const dialogRef = this.dialog.open(RegistrationActionDialogComponent, {
      data: {
        message: 'Are you sure you wish to ' + type + ' the following requests? Please enter a reason that will be sent to the user(s). <br />' + selectionNames,
        showInput: true,
        selection: this.selection,
        type,
        realmName: this.realmName
      }
    });

    this.dialogOpen = true;

    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {
          this.getNext(DEFAULT_PAGE_EVENT);
        }
        this.dialogOpen = false;
      }
    );
  }

  rowClicked(row: any) {
    this.messages = '<b>' + row.requesterInfo.username + '</b>';
    for(const m of row.messages) {
      this.messages += '<p>' + m.message + '</p>';
    }
  }

}
