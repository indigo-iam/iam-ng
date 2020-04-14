import { async, TestBed } from '@angular/core/testing';

import { AppConfigService } from './app-config.service';
import { AppConfig } from './models/app-config';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('AppConfigService', () => {
  let service: AppConfigService;
  let http;

  const mockAppConfig: AppConfig = {
      iamApiBaseUrl: '/dev-api-test',
      keycloakBaseUrl: 'https://kc.test.example/auth/',
      fallbackRealm: 'master',
      keycloakClientId: 'iam-dashboard',
      customAttributes: {
        alice : {
          registration: {
            welcomeTitle: '<b>Welcome to the registration page for ALICE</b>',
            welcomeText: '<i>When signing up please use your CERN email address, or an academic email address. Please do not use personal email addresses here.</i> <a href="https://google.com">Google</a>'
          },
          login: {
            loginTitle: 'Sign in with your eduGAIN credentials to get access to the dashboard'
          }
        }
      },
      loginOrder: {
        alice: {
          edugain: 1,
          local: 2,
          register: 3
        }
      }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(AppConfigService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load app config', () => {
    service.loadAppConfig();
    const req = http.expectOne('/assets/app-config.json');
    expect(req.request.method).toEqual('GET');

    req.flush(mockAppConfig);

    expect(service.getConfig()).toEqual(mockAppConfig);
  });
});
