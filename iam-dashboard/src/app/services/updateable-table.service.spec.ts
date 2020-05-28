import { TestBed } from '@angular/core/testing';

import { UpdateableTableService } from './updateable-table.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DEFAULT_PAGE_EVENT } from '../utils/utils';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { REGISTRATION_REQUESTS } from '../utils/test-data';
import { of, throwError } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';

describe('UpdateableTableService', () => {
  let service: UpdateableTableService;
  let sb;
  let bui;
  let registrationService;
  let paginator;

  beforeEach(() => {
    sb = jasmine.createSpyObj(['open']);
    bui = jasmine.createSpyObj(['start', 'stop']);
    registrationService = jasmine.createSpyObj(['getPaginated']);
    registrationService.getPaginated.and.returnValue(of(
      REGISTRATION_REQUESTS
    ));
    paginator = jasmine.createSpyObj(['']);

    TestBed.configureTestingModule({
      providers: [
        { provide: MatSnackBar, useValue: sb }
      ]
    });
    service = TestBed.inject(UpdateableTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get data from registration service', () => {
    const ret = service.getNext(DEFAULT_PAGE_EVENT, bui, 'alice', paginator, registrationService);
    expect(bui.start).toHaveBeenCalled();
    expect(bui.stop).toHaveBeenCalled();
    ret.subscribe(r => {
      expect(r.resources).toEqual(REGISTRATION_REQUESTS.resources);
    });

  });

  it('should get data from registration service', () => {
    registrationService.getPaginated.and.returnValue(throwError({status: 500}));
    const ret = service.getNext(DEFAULT_PAGE_EVENT, bui, 'alice', paginator, registrationService);
    expect(bui.start).toHaveBeenCalled();
    expect(bui.stop).toHaveBeenCalled();
    expect(sb.open).toHaveBeenCalled();
    ret.subscribe(r => {
      expect(r.resources).toEqual(undefined);
    });

  });
});
