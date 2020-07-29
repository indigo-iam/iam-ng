import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionDeniedComponent } from './permission-denied.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

describe('PermissionDeniedComponent', () => {
  let component: PermissionDeniedComponent;
  let fixture: ComponentFixture<PermissionDeniedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatIconModule,
        MatCardModule
      ],
      declarations: [ PermissionDeniedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionDeniedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
