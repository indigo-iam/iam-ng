import { LayoutModule } from '@angular/cdk/layout';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';

import { NavigationComponent } from './navigation.component';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { RealmService } from '../services/realm.service';

describe('NavigationComponent', () => {
  let component: NavigationComponent;
  let fixture: ComponentFixture<NavigationComponent>;
  let realmService;

  beforeEach(async(() => {

    realmService = jasmine.createSpyObj(['setCurrentRealm']);

    TestBed.configureTestingModule({
      declarations: [NavigationComponent],
      imports: [
        NoopAnimationsModule,
        LayoutModule,
        MatButtonModule,
        MatIconModule,
        MatListModule,
        MatSidenavModule,
        MatToolbarModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: {
          paramMap: of(
            convertToParamMap(of(
              {
                realm: 'alice'
              }
            ))
          )
        }},
        { provide: RealmService, useValue: realmService}
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should compile', () => {
    expect(component).toBeTruthy();
  });

  it('should render title', () => {
    expect(fixture.debugElement.query(By.css('#title')).nativeElement.textContent).toContain('IAM Dashboard');
  });
});
