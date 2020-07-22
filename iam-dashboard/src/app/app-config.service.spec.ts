import { async, TestBed, tick, fakeAsync } from '@angular/core/testing';

import { AppConfigService } from './app-config.service';
import { AppConfig } from './models/app-config';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

describe('AppConfigService', () => {
  let service: AppConfigService;
  let mockHttp;

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
      }
  };

  beforeEach(() => {
    mockHttp = jasmine.createSpyObj(['get']);
    mockHttp.get.and.returnValue(of(mockAppConfig));

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        { provide: HttpClient, useValue: mockHttp }
      ]
    });
    service = TestBed.inject(AppConfigService);
    service.appConfig = mockAppConfig;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load app config', fakeAsync(() => {
    service.appConfig = undefined; // reset this var as we are testing setting to it
    service.loadAppConfig();
    tick();
    expect(service.getConfig()).toEqual(mockAppConfig);
  }));

  it('should get fallback realm', () => {
    expect(service.getFallbackRealm()).toEqual('master');
  });

  it('should get IAM API base URL', () => {
    expect(service.getIamApiBaseUrl()).toEqual('/dev-api-test');
  });

  it('should get Keycloak base URL', () => {
    expect(service.getKeycloakBaseUrl()).toEqual('https://kc.test.example/auth/');
  });

  it('should get Keycloak client ID', () => {
    expect(service.getKeycloakClientId()).toEqual('iam-dashboard');
  });

  it('should get a custom attribute', () => {
    expect(service.getCustomAttribute('alice', 'registration', 'welcomeTitle')).toEqual('<b>Welcome to the registration page for ALICE</b>');
    expect(service.getCustomAttribute('alice', 'notexist', 'notexist')).toEqual(null);
    expect(service.getCustomAttribute('notexist', 'notexist', 'notexist')).toEqual(null);
    expect(service.getCustomAttribute('alice', 'registration', 'notexist')).toEqual(null);
  });

  it('should check a custom attribute exists', () => {
    expect(service.attributeExists('alice', 'registration', 'welcomeTitle')).toBeTrue();
    expect(service.attributeExists('alice', 'notexist', 'notexist')).toBeFalse();
    expect(service.attributeExists('notexist', 'notexist', 'notexist')).toBeFalse();
    expect(service.attributeExists('alice', 'registration', 'notexist')).toBeFalse();
  });
});
