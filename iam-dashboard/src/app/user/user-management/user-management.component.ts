import { Component, OnInit, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { RealmService } from 'src/app/services/realm.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { UserService } from 'src/app/services/user.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private realmService: RealmService, private userService: UserService, private sb: MatSnackBar) { }

  ngOnInit(): void {
    this.realmService.getCurrentRealm().subscribe(r => {
      this.realmName = r;
      this.updateTable();
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

  updateTable() {
    this.blockUIuserTable.start();
    this.userService.getUsersPaginated(this.realmName, 0, 10).subscribe(
      (response: any) => {
        this.users = response.resources;
        console.log(response);
        this.dataSource = new MatTableDataSource(this.users);
        this.dataSource.paginator = this.paginator;
        this.blockUIuserTable.stop();
      },
      (error) => {
        this.sb.open('An error occoured when getting users: ' + error.message, 'Close');
        this.blockUIuserTable.stop();
      }
    );
  }

  getNext(event: PageEvent) {
    this.blockUIuserTable.start();
    const offset = event.pageSize * event.pageIndex;
    this.userService.getUsersPaginated(this.realmName, offset, event.pageSize).subscribe(
      (response: any) => {
        this.users = response.resources;
        this.dataSource = new MatTableDataSource(this.users);
        this.blockUIuserTable.stop();
      },
      (error) => {
        this.sb.open('An error occoured changing pages: ' + error.message, 'Close');
        this.blockUIuserTable.stop();
      }
    );
  }
}
