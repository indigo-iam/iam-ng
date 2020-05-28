import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AppConfigService } from '../app-config.service';
import { USERS } from '../utils/test-data';

describe('UserService', () => {
  let service: UserService;
  let appConfigService;
  let http: HttpTestingController;

  beforeEach(() => {
    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl']);
    appConfigService.getIamApiBaseUrl.and.returnValue('https://api.test.example/');
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        { provide: AppConfigService, useValue: appConfigService },
      ]
    });
    service = TestBed.inject(UserService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get a user', () => {
    service.getUser('alice', '123').subscribe(res => {
      expect(res).toEqual(USERS.resources[0]);
    });

    const req = http.expectOne('https://api.test.example//Realms/alice/Users/123');
    expect(req.request.method).toEqual('GET');

    req.flush(USERS.resources[0]);
    http.verify();
  });

  it('should get paginated list of users', () => {
    service.getPaginated('alice', 0, 10).subscribe(res => {
      expect(res).toEqual(USERS);
    });

    const req = http.expectOne('https://api.test.example//Realms/alice/Users?count=10&startIndex=0');
    expect(req.request.method).toEqual('GET');

    req.flush(USERS);
    http.verify();
  });
});
