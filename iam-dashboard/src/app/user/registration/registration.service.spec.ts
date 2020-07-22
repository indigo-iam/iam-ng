import { TestBed } from '@angular/core/testing';

import { RegistrationService } from './registration.service';
import { AppConfigService } from '../../app-config.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { RegistrationConfigurationDTO } from '../../models/registration-configuration-dto';
import { FormBuilder } from '@angular/forms';
import { REGISTRATION_REQUESTS } from 'src/app/utils/test-data';
import { ACTION_APPROVE } from '../registration-requests/registration-request-action';

describe('RegistrationService', () => {
  let service: RegistrationService;
  let appConfigService;
  let http: HttpTestingController;

  const mockRegConfig: RegistrationConfigurationDTO = {
    kind: 'config',
    registrationEnabled: true,
    privacyPolicyUrl: '',
    aupUrl: '',
    logoUrl: ''
  };

  const mockRegResponse = {
    message: 'Success'
  };

  const mockEmailResponse = {
    message: 'Success',
    detail: 'Success'
  };

  beforeEach(() => {
    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl']);
    appConfigService.getIamApiBaseUrl.and.returnValue('https://api.test.example/');
    TestBed.configureTestingModule(
      {
        imports: [
          HttpClientTestingModule
        ],
        providers: [
          { provide: AppConfigService, useValue: appConfigService }
        ],
      }
    );
    service = TestBed.inject(RegistrationService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get registration config', () => {
    service.getRegistrationConfig('alice').subscribe(res => {
      expect(res).toEqual(mockRegConfig);
    });

    const req = http.expectOne('https://api.test.example//Realms/alice/Registrations/config');
    expect(req.request.method).toEqual('GET');

    req.flush(mockRegConfig);
  });

  it('should make registration request', () => {

    const fb = new FormBuilder();
    const fg = fb.group({
      givenName: [''],
      familyName: [''],
      email: [''],
      username: [''],
      notes: ['']
    });

    service.createRegistration(fg, 'alice').subscribe(res => {
      expect(res).toEqual(mockRegResponse);
    });

    const req = http.expectOne({url: 'https://api.test.example//Realms/alice/Registrations', method: 'POST'});

    req.flush(mockRegResponse);
    http.verify();
  });

  it('should return false for all emails and usernames existing', () => {
    expect(service.emailExists('')).toBeFalse();
    expect(service.usernameExists('')).toBeFalse();
  });

  it('should make confirm email request', () => {
    service.confirmEmail('abc', 'alice').subscribe(res => {
      expect(res).toEqual(mockEmailResponse);
    });

    const req = http.expectOne({url: 'https://api.test.example//Realms/alice/Registrations/confirm/abc', method: 'POST'});

    req.flush(mockEmailResponse);
    http.verify();
  });

  it('should get reg requests paginated', () => {
    service.getPaginated('alice', 0, 10).subscribe(res => {
      expect(res).toEqual(REGISTRATION_REQUESTS);
    });

    const req = http.expectOne('https://api.test.example//Realms/alice/Requests/registration/pending?startIndex=0&count=10');
    expect(req.request.method).toEqual('GET');

    req.flush(REGISTRATION_REQUESTS);
    http.verify();
  });


  it('should action a registration request', () => {
    service.actionRegistrationRequest('alice', '123', ACTION_APPROVE, 'yay good luck').subscribe(res => {
      expect(res).toEqual({});
    });

    const req = http.expectOne({url: 'https://api.test.example//Realms/alice/Requests/registration/123', method: 'POST'});
    expect(req.request.method).toEqual('POST');

    req.flush({});
    http.verify();
  });

});
