import { TestBed, inject } from '@angular/core/testing';

import { LoadingInterceptor } from './loading.interceptor';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LoadingService } from './loading.service';
import { Subject, of } from 'rxjs';
import { HTTP_INTERCEPTORS, HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('LoadingInterceptor', () => {
  let sb;
  let ls;
  let interceptor: LoadingInterceptor;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    ls = jasmine.createSpyObj(['show', 'hide', 'getIsLoading']);

    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: LoadingInterceptor,
          multi: true
        },
        MatSnackBar,
        {
          provide: LoadingService,
          useValue: ls
        },
        LoadingInterceptor
      ]
    });
  });



  beforeEach(() => {
    interceptor = TestBed.inject(LoadingInterceptor);
    httpMock = TestBed.get(HttpTestingController);
  })

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  /*it('loadingservice show should be called on intercept', () => {

    let goodRequest = httpMock.expectOne('/test');
    goodRequest.flush({});

    expect(ls.show).toHaveBeenCalled();
    expect(ls.hide).toHaveBeenCalled();
  });*/


});
