import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RealmChooserComponent } from './realm-chooser.component';

describe('RealmChooserComponent', () => {
  let component: RealmChooserComponent;
  let fixture: ComponentFixture<RealmChooserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RealmChooserComponent ]
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
