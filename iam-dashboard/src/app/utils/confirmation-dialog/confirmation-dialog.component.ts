import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent implements OnInit {

  message = '';
  showInput = false;
  userInput = '';

  constructor(@Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<ConfirmationDialogComponent>) { }

  ngOnInit(): void {
    if(this.data) {
      if(this.data.message) {
        this.message = this.data.message;
      }
      if(this.data.showInput) {
        this.showInput = true;
      }
    }
  }

  no(): void {
    this.dialogRef.close();
  }

}
