import { Component, OnInit, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { RealmService } from 'src/app/services/realm.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { UserService } from 'src/app/services/user.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DEFAULT_PAGE_EVENT, UpdateableTableData } from 'src/app/utils/utils';
import { UpdateableTableService } from 'src/app/services/updateable-table.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  realmName: string;
  dataSource;
  searchQuery = new Subject<string>();
  users;

  displayedColumns: string[] = ['username', 'name', 'emails', 'active', 'manage'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @BlockUI('userTable') blockUIuserTable: NgBlockUI;

  constructor(private realmService: RealmService, private userService: UserService, private sb: MatSnackBar, private updateableTableService: UpdateableTableService) { }

  ngOnInit(): void {
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      this.getNext(DEFAULT_PAGE_EVENT);
    });
  }

  search() {
    /*this.searchQuery.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((value) => {
      this.blockUIclientTable.start();
      this.clientManagementService.searchClients(this.realmName, value).subscribe(
        (response) => {
          this.clients = response;
          this.dataSource = new MatTableDataSource(this.clients);
          this.blockUIclientTable.stop();
        },
        (error) => {
          this.sb.open('An error occoured whilst searching: ' + error.message, 'Close');
          this.blockUIclientTable.stop();
        }
      );
    });*/
  }

  getNext(event: PageEvent) {
    const res: Subject<UpdateableTableData> = this.updateableTableService.getNext(event, this.blockUIuserTable, this.realmName, this.paginator, this.userService);
    res.subscribe(r => {
      this.dataSource = r.dataSource;
      this.users = r.resources;
    });
  }
}
