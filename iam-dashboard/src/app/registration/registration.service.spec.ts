import { TestBed } from '@angular/core/testing';

import { RegistrationService } from './registration.service';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from '../app-config.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { RegistrationConfigurationDTO } from '../models/registration-configuration-dto';
import { FormBuilder, FormGroup } from '@angular/forms';

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
  }

  const mockRegResponse = {
    message: 'Success'
  }

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
    http = TestBed.get(HttpTestingController);
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

    let fb = new FormBuilder();
    let fg = fb.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      username: [''],
    });

    service.createRegistration(fg, 'alice').subscribe(res => {
      expect(res).toEqual(mockRegResponse);
    });

    const req = http.expectOne({url: 'https://api.test.example//Realms/alice/Registrations', method: 'POST'});

    req.flush(mockRegResponse);
    http.verify();
  });

});
