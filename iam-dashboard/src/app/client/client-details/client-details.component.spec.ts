import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientDetailsComponent } from './client-details.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

describe('ClientDetailsComponent', () => {
  let component: ClientDetailsComponent;
  let fixture: ComponentFixture<ClientDetailsComponent>;
  let matDialogRef;

  beforeEach(async(() => {
    matDialogRef = jasmine.createSpyObj(['close']);

    TestBed.configureTestingModule({
      declarations: [ ClientDetailsComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: matDialogRef }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog on close', () => {
    component.close();
    expect(matDialogRef.close).toHaveBeenCalled();
  })
});
