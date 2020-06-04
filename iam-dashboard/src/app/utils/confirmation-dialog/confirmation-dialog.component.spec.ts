import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('ClientDetailsComponent', () => {
  let component: ConfirmationDialogComponent;
  let fixture: ComponentFixture<ConfirmationDialogComponent>;
  let matDialogRef;

  beforeEach(async(() => {

    matDialogRef = jasmine.createSpyObj(['close']);

    TestBed.configureTestingModule({
      imports: [
        MatInputModule,
        MatDialogModule,
        MatCardModule,
        MatButtonModule,
        NoopAnimationsModule
      ],
      declarations: [ ConfirmationDialogComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {
          message: 'Hello World',
          showInput: true
        } },
        { provide: MatDialogRef, useValue: matDialogRef }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('no should call close', () => {
    component.no();
    expect(matDialogRef.close).toHaveBeenCalled();
  })
});
