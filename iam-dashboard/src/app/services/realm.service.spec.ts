import { TestBed } from '@angular/core/testing';

import { RealmService } from './realm.service';
import { AppConfigService } from '../app-config.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ListResponseDTO } from '../models/list-response-dto';
import { RealmDTO } from '../models/realm-dto';

describe('RealmService', () => {
  let service: RealmService;
  let http;
  let appConfigService;

  beforeEach(() => {
    appConfigService = jasmine.createSpyObj(['getIamApiBaseUrl']);
    appConfigService.getIamApiBaseUrl.and.returnValue('https://api.test.example/');
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        { provide: AppConfigService, useValue: appConfigService },
      ],
    });
    service = TestBed.inject(RealmService);
    http = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get realms', () => {
    const mockRealms: ListResponseDTO<RealmDTO> = {
      kind: '',
      totalResults: 5,
      itemsPerPage: 50,
      startIndex: 0,
      resources: [
        {name:"alice"},
        {name:"atlas"},
        {name:"cms"},
        {name:"iam"},
        {name:"lhcb"}
      ]
    }

    service.getRealms().subscribe(res => {
      expect(res).toEqual(mockRealms);
    });

    const req = http.expectOne('https://api.test.example//Realms');
    expect(req.request.method).toEqual('GET');

    req.flush(mockRealms);
  });
});
