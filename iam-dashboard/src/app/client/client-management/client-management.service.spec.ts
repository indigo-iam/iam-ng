import { TestBed } from '@angular/core/testing';

import { ClientManagementService } from './client-management.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AppConfigService } from 'src/app/app-config.service';
import { HttpClient } from '@angular/common/http';
import { SINGLE_CLIENT_LIST, SINGLE_CLIENT, SAML_CLIENT } from 'src/app/utils/client-test-data';

describe('ClientManagementService', () => {
  let service: ClientManagementService;
  let appConfigService;
  let http: HttpTestingController;

  beforeEach(() => {
    appConfigService = jasmine.createSpyObj(['getKeycloakBaseUrl']);
    appConfigService.getKeycloakBaseUrl.and.returnValue('https://kc.test.example/');
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
    service = TestBed.inject(ClientManagementService);
    http = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get clients', () => {
    service.getClients('alice').subscribe(res => {
      expect(res).toEqual(SINGLE_CLIENT_LIST);
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients');
    expect(req.request.method).toEqual('GET');

    req.flush(SINGLE_CLIENT_LIST);
  });

  it('should create a client', () => {
    service.createClient('alice', SINGLE_CLIENT).subscribe(res => {
      expect(res).toEqual({});
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients');
    expect(req.request.method).toEqual('POST');

    req.flush({});
  });

  it('should update a client', () => {
    service.updateClient('alice', 'c476342d-3588-4877-b560-cd4c37b0fe65', SINGLE_CLIENT).subscribe(res => {
      expect(res).toEqual({});
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients/c476342d-3588-4877-b560-cd4c37b0fe65');
    expect(req.request.method).toEqual('PUT');

    req.flush({});
  });

  it('should get a client', () => {
    service.getClient('alice', 'c476342d-3588-4877-b560-cd4c37b0fe65').subscribe(res => {
      expect(res).toEqual(SINGLE_CLIENT);
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients/c476342d-3588-4877-b560-cd4c37b0fe65');
    expect(req.request.method).toEqual('GET');

    req.flush(SINGLE_CLIENT);
  });

  it('should get a client secret', () => {
    const secret = {
      value: 'secret123'
    }
    service.getClientSecret('alice', 'c476342d-3588-4877-b560-cd4c37b0fe65').subscribe(res => {
      expect(res).toEqual(secret);
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients/c476342d-3588-4877-b560-cd4c37b0fe65/client-secret');
    expect(req.request.method).toEqual('GET');

    req.flush(secret);
  });

  it('should create a SAML client', () => {
    service.createSamlClient('alice', '<xml>Sample XML</xml>').subscribe(res => {
      expect(res).toEqual(SAML_CLIENT);
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/client-description-converter');
    expect(req.request.method).toEqual('POST');

    req.flush(SAML_CLIENT);
  });

  it('should delete a client', () => {
    service.deleteClient('alice', 'c476342d-3588-4877-b560-cd4c37b0fe65').subscribe(res => {
      expect(res).toEqual({});
    });

    const req = http.expectOne('https://kc.test.example/admin/realms/alice/clients/c476342d-3588-4877-b560-cd4c37b0fe65');
    expect(req.request.method).toEqual('DELETE');

    req.flush({});
  });

});
