import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailConfirmationComponent } from './email-confirmation.component';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegistrationService } from '../registration/registration.service';
import { By } from '@angular/platform-browser';

describe('EmailConfirmationComponent', () => {
  let component: EmailConfirmationComponent;
  let fixture: ComponentFixture<EmailConfirmationComponent>;
  let rs;

  beforeEach(async(() => {
    rs = jasmine.createSpyObj(['confirmEmail', 'getRegistrationConfig']);
    rs.getRegistrationConfig.and.returnValue(
      of(
        {
          kind: 'RegistrationConfiguration',
          registrationEnabled: true,
          privacyPolicyUrl: 'https://alice.example.com/privacy',
          aupUrl: 'https://alice.example.com/aup',
          logoUrl: ''
        }
      )
    );

    rs.confirmEmail.and.returnValue(of(
      {
        message: 'Success',
        detail: 'Success'
      }
    ));

    TestBed.configureTestingModule({
      declarations: [ EmailConfirmationComponent ],
      providers: [
        { provide: ActivatedRoute, useValue: {
          paramMap: of(
            convertToParamMap(of(
              {
                realm: 'alice',
                token: 'abc'
              }
            ))
          )
        }
      },
      { provide: RegistrationService, useValue: rs}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should error with bad token', () => {
    rs.confirmEmail.and.returnValue(of(
      {
        error: 'Error',
        detail: 'Some details'
      }
    ));
    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#message')).nativeElement.textContent).toContain('Error');
  });

  it('should success with good token', () => {
    expect(fixture.debugElement.query(By.css('#message')).nativeElement.textContent).toContain('Success');
  });

  it('should error with http error', () => {
    rs.confirmEmail.and.returnValue(throwError(
      {
        error: 'Error',
        detail: 'Some details'
      }
    ));
    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#message')).nativeElement.textContent).toContain('Error');
  });

  it('should error with http error', () => {
    rs.confirmEmail.and.returnValue(throwError(
      {
        error: 'Error',
        errorDescription: 'Some details',
        status: 404
      }
    ));
    fixture = TestBed.createComponent(EmailConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#message')).nativeElement.textContent).toContain('Error');
  });

});
