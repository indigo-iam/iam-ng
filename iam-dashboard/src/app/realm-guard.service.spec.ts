import { TestBed } from '@angular/core/testing';

import { RealmGuardService } from './realm-guard.service';

describe('RealmGuardService', () => {
  let service: RealmGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RealmGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
