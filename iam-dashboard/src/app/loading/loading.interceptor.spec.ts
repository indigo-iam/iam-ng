import { TestBed } from '@angular/core/testing';

import { LoadingInterceptor } from './loading.interceptor';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('LoadingInterceptor', () => {
  let sb;
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      LoadingInterceptor,
      MatSnackBar
      ]
  }));

  it('should be created', () => {
    const interceptor: LoadingInterceptor = TestBed.inject(LoadingInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
