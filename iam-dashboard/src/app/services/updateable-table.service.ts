import { Injectable } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { PageEvent, MatPaginator } from '@angular/material/paginator';
import { NgBlockUI } from 'ng-block-ui';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UpdateableTableData } from '../utils/utils';
import { Subject } from 'rxjs';
import { PaginatedService } from '../utils/paginated.service';

@Injectable({
  providedIn: 'root'
})
export class UpdateableTableService {

  constructor(private sb: MatSnackBar) { }

  getNext(event: PageEvent, blockUi: NgBlockUI, realmName: string, paginator: MatPaginator, service: PaginatedService): Subject<UpdateableTableData> {
    const subject = new Subject<UpdateableTableData>();
    blockUi.start();
    const offset = event.pageSize * event.pageIndex;
    let resources;
    let dataSource: MatTableDataSource<any>;
    service.getPaginated(realmName, offset, event.pageSize).subscribe(
      (response: any) => {
        resources = response.resources;
        dataSource = new MatTableDataSource(resources);
        dataSource.paginator = paginator;
        subject.next({
          resources: resources,
          dataSource: dataSource
        })
        blockUi.stop();
      },
      (error) => {
        this.sb.open('An error occoured changing pages: ' + error.message, 'Close');
        blockUi.stop();
        subject.next({
          resources: undefined,
          dataSource: undefined
        })
      }
    );
    return subject;
  }
}