import { TestBed } from '@angular/core/testing';

import { LoadingService } from './loading.service';

describe('LoadingService', () => {
  let service: LoadingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be true when show is called', () => {
    service.isLoading.subscribe(v => {
      expect(v).toBeTruthy();
    });
    service.show();
  });

  it('should be false when hide is called', () => {
    service.isLoading.subscribe(v => {
      expect(v).toBeFalsy();
    });
    service.hide();
  });

});
