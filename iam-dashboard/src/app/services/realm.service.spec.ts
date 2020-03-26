import { TestBed } from '@angular/core/testing';

import { RealmService } from './realm.service';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from '../app-config.service';

describe('RealmService', () => {
  let service: RealmService;
  let httpClient;
  let appConfigService;

  beforeEach(() => {
    httpClient = jasmine.createSpyObj(['get', 'post']);
    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl']);
    appConfigService.getIamApiBaseUrl.and.returnValue("");
    TestBed.configureTestingModule({
      providers: [
        { provide: HttpClient, useValue: httpClient},
        { provide: AppConfigService, useValue: appConfigService },
      ],
    });
    service = TestBed.inject(RealmService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
