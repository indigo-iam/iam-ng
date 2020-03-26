import { TestBed } from '@angular/core/testing';

import { RegistrationService } from './registration.service';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from '../app-config.service';

describe('RegistrationService', () => {
  let service: RegistrationService;

  beforeEach(() => {
    let httpClient;
    let appConfigService;
    httpClient = jasmine.createSpyObj(['get', 'post']);
    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl']);
    appConfigService.getIamApiBaseUrl.and.returnValue("");
    TestBed.configureTestingModule(
      {
        providers: [
          { provide: HttpClient, useValue: httpClient},
          { provide: AppConfigService, useValue: appConfigService }
        ],
      }
    );
    service = TestBed.inject(RegistrationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

});
