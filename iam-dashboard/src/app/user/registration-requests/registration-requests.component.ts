import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-registration-requests',
  templateUrl: './registration-requests.component.html',
  styleUrls: ['./registration-requests.component.scss']
})
export class RegistrationRequestsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'submitted', 'email', 'approve'];
  dataSource = new MatTableDataSource<RegistrationRequest>(REGISTRATION_REQUESTS);

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor() { }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
  }

}

export interface RegistrationRequest {
  name: string;
  submitted: string;
  email: string;
}

const REGISTRATION_REQUESTS: RegistrationRequest[] = [
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
  {name: 'John Smith', submitted: '2020-01-01T01:01', email: 'e@example.com'},
];
