import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementComponent } from './user-management.component';
import { RealmService } from 'src/app/services/realm.service';
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';

describe('UserManagementComponent', () => {
  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;
  let realmService;
  let userService;
  let sb;
  beforeEach(async(() => {
    realmService = jasmine.createSpyObj(['getCurrentRealm']);
    userService = jasmine.createSpyObj(['getUser', 'getUsersPaginated']);
    userService.getUser.and.returnValue(of());
    userService.getUsersPaginated.and.returnValue(of());
    realmService.getCurrentRealm.and.returnValue(of('alice'));
    sb = jasmine.createSpyObj(['open']);
    TestBed.configureTestingModule({
      declarations: [ UserManagementComponent ],
      providers: [
        { provide: RealmService, useValue: realmService },
        { provide: UserService, useValue: userService },
        { provide: MatSnackBar, useValue: sb }
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
});
