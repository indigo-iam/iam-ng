import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';

import { UserManagementComponent } from './user-management.component';
import { RealmService } from 'src/app/services/realm.service';
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, Subject } from 'rxjs';
import { UpdateableTableService } from 'src/app/services/updateable-table.service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { USERS } from 'src/app/utils/test-data';
import { DEFAULT_PAGE_EVENT } from 'src/app/utils/utils';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BlockUIModule, BlockUIService } from 'ng-block-ui';
import { MatIconModule } from '@angular/material/icon';

describe('UserManagementComponent', () => {
  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;
  let realmService;
  let userService;
  let sb;
  let updateableTableService;
  const subject = new Subject();

  beforeEach(async(() => {
    realmService = jasmine.createSpyObj(['getCurrentRealm']);
    userService = jasmine.createSpyObj(['getPaginated']);
    userService.getPaginated.and.returnValue(of(
      USERS
    ));
    realmService.getCurrentRealm.and.returnValue(of('alice'));

    subject.next({
      resources: USERS.resources,
      dataSource: new MatTableDataSource(USERS.resources)
    });

    updateableTableService = jasmine.createSpyObj(['getNext']);
    updateableTableService.getNext.and.returnValue(subject);

    sb = jasmine.createSpyObj(['open']);

    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatInputModule,
        MatTableModule,
        NoopAnimationsModule,
        MatPaginatorModule,
        BlockUIModule.forRoot(),
        MatIconModule
      ],
      declarations: [ UserManagementComponent ],
      providers: [
        { provide: RealmService, useValue: realmService },
        { provide: UserService, useValue: userService },
        { provide: MatSnackBar, useValue: sb },
        { provide: UpdateableTableService, useValue: updateableTableService },
        BlockUIService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get users from updatable table service', () => {
    component.users = [];
    subject.next({
      resources: USERS.resources,
      dataSource: new MatTableDataSource(USERS.resources)
    });
    component.getNext(DEFAULT_PAGE_EVENT);
    expect(component.users).toEqual(USERS.resources);
  });
});
