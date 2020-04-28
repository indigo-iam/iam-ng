import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewOrEditClientComponent } from './new-or-edit-client.component';

describe('NewOrEditClientComponent', () => {
  let component: NewOrEditClientComponent;
  let fixture: ComponentFixture<NewOrEditClientComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewOrEditClientComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewOrEditClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
