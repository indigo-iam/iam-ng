import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileComponent } from './user-profile.component';
import { RealmService } from 'src/app/services/realm.service';
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { USERS } from 'src/app/utils/test-data';
import { MatCardModule } from '@angular/material/card';
import { BlockUIModule, BlockUIService } from 'ng-block-ui';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let realmService;
  let userService;
  let sb;
  let router: Router;

  beforeEach(async(() => {
    realmService = jasmine.createSpyObj(['getCurrentRealm']);
    userService = jasmine.createSpyObj(['getUser']);
    userService.getUser.and.returnValue(of(USERS.resources[0]));
    realmService.getCurrentRealm.and.returnValue(of('alice'));
    sb = jasmine.createSpyObj(['open']);
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        MatCardModule,
        BlockUIModule.forRoot(),
        MatListModule,
        MatDividerModule
      ],
      declarations: [ UserProfileComponent ],
      providers: [
        { provide: RealmService, useValue: realmService },
        { provide: UserService, useValue: userService },
        { provide: MatSnackBar, useValue: sb },
        { provide: ActivatedRoute, useValue: {
          paramMap: of(
            convertToParamMap(of(
              {
                userid: '123'
              }
            ))
          )
        }},
        BlockUIService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProfileComponent);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.user).toEqual(USERS.resources[0]);
  });

  it('should 404 with invalid user', () => {
    userService.getUser.and.returnValue(throwError({status: 500}));
    const nav = spyOn(router, 'navigateByUrl');
    component.ngOnInit();
    expect(nav).toHaveBeenCalledWith('/404', {skipLocationChange: true});
  })
});
