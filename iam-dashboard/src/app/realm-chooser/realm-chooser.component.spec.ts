import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RealmChooserComponent } from './realm-chooser.component';
import { RealmService } from '../services/realm.service';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

describe('RealmChooserComponent', () => {
  let component: RealmChooserComponent;
  let fixture: ComponentFixture<RealmChooserComponent>;
  let realmService;

  beforeEach(async(() => {
    realmService = jasmine.createSpyObj(['getRealms']);
    realmService.getRealms.and.returnValue(of(
      {
        totalResults: 5,
        itemsPerPage: 50,
        startIndex: 0,
        resources: [
          {
            name: 'alice'
          },
          {
            name: 'atlas'
          },
          {
            name: 'cms'
          },
          {
            name: 'iam'
          },
          {
            name: 'lhcb'
          }
        ]
      }
    ));

    TestBed.configureTestingModule({
      declarations: [ RealmChooserComponent ],
      providers: [
        { provide: RealmService, useValue: realmService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RealmChooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
