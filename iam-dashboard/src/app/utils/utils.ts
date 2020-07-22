import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

export class Utils {

}

export const DEFAULT_PAGE_EVENT: PageEvent = { pageIndex: 0, pageSize: 10, length: 10 };

export interface UpdateableTableData {
    resources: any,
    dataSource: MatTableDataSource<any>
}