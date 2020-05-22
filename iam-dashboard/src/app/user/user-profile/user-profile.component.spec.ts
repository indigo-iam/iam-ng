import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileComponent } from './user-profile.component';
import { RealmService } from 'src/app/services/realm.service';
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let realmService;
  let userService;
  let sb;

  beforeEach(async(() => {
    realmService = jasmine.createSpyObj(['getCurrentRealm']);
    userService = jasmine.createSpyObj(['getUser']);
    userService.getUser.and.returnValue(of());
    realmService.getCurrentRealm.and.returnValue(of('alice'));
    sb = jasmine.createSpyObj(['open']);
    TestBed.configureTestingModule({
      imports: [  RouterTestingModule ],
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
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
