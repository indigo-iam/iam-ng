import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { AppConfigService } from '../app-config.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let appConfigService;

  beforeEach(async(() => {

    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl', 'attributeExists', 'getLoginOrder', 'getCustomAttribute']);
    appConfigService.getIamApiBaseUrl.and.returnValue('');
    appConfigService.attributeExists.and.returnValue(true);
    appConfigService.getLoginOrder.and.returnValue({
      alice: {
        edugain: 1,
        local: 2,
        register: 3
      }}
    );

    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      providers: [
        { provide: ActivatedRoute, useValue: {
          paramMap: of(
            convertToParamMap(of(
              {
                realm: 'alice'
              }
            ))
          )
        }},
        { provide: AppConfigService, useValue: appConfigService },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
