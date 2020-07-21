import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Action, ACTION_REJECT, ACTION_APPROVE } from '../registration-requests/registration-request-action';
import { RegistrationService } from '../registration/registration.service';
import { NotificationsService } from 'angular2-notifications';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-registration-action-dialog',
  templateUrl: './registration-action-dialog.component.html',
  styleUrls: ['./registration-action-dialog.component.scss']
})
export class RegistrationActionDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<RegistrationActionDialogComponent>, private registrationService: RegistrationService, private notificationsService: NotificationsService, private fb: FormBuilder) { }

  submissionError = false;
  markupError = false;

  ReasonForm = this.fb.group({
    reason: ['', [Validators.required]]
  });

  ngOnInit(): void {
  }

  no(): void {
    this.dialogRef.close();
  }

  action(): void {

    let checked = 0;

    let action: Action;
    if (this.data.type === 'reject') {
      action = ACTION_REJECT;
    } else {
      action = ACTION_APPROVE;
    };


    // Don't even bother submitting the requests if there isn't a reason
    if (!this.ReasonForm.get('reason').hasError('required')) {
      this.data.selection.selected.forEach((sel) => {
        // We convert to a promise here as we want this to be synchronous
        this.registrationService.actionRegistrationRequest(this.data.realmName, sel.uuid, action, this.ReasonForm.get('reason').value).subscribe(
          (r) => {
            console.log(r);
            this.notificationsService.create('' + sel.requesterInfo.givenName + ' ' + sel.requesterInfo.familyName + ' (' + sel.requesterInfo.username + ') was actioned');
            checked++;
            // All actions have been successful so we can close the dialog!
            if (checked === this.data.selection.selected.length) {
              this.dialogRef.close(true);
            }
          },
          (error) => {
            console.log(error);
            this.ReasonForm.get('reason').setErrors({ error: true });
          });
      });
    }
  }

}
