import { TestBed } from '@angular/core/testing';

import { RealmsService } from './realms.service';

describe('RealmsService', () => {
  let service: RealmsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RealmsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
