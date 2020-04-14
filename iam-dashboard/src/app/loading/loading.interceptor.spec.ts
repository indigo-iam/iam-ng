import { TestBed } from '@angular/core/testing';

import { LoadingInterceptor } from './loading.interceptor';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { LoadingService } from './loading.service';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('LoadingInterceptor', () => {
  let ls: any;
  let interceptor: LoadingInterceptor;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let sb;
  beforeEach(() => {
    ls = jasmine.createSpyObj(['show', 'hide', 'getIsLoading']);
    sb = jasmine.createSpyObj(['open']);

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
        {
          provide: MatSnackBar, useValue: sb
        },
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
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    sb = TestBed.inject(MatSnackBar);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('loadingservice show and hide should be called on intercept', () => {

    httpClient.get('/test').subscribe(() => {});

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toEqual('GET');

    req.flush({});

    expect(ls.show).toHaveBeenCalled();
    expect(ls.hide).toHaveBeenCalled();
  });

  it('snackbar should be called on intercept error req', () => {

    httpClient.get('/test').subscribe(
      (response) => response,
      (error) => error
    );

    httpMock.expectOne('/test').error(new ErrorEvent('Internal server error'), {status: 500});

    httpMock.verify();

    expect(sb.open).toHaveBeenCalled();
  });
});
