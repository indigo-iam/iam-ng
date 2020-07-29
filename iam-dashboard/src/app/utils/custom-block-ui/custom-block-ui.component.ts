import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-custom-block-ui',
  template: `
    <div class="block-ui-template">
      <mat-spinner style="margin: 0 auto;"></mat-spinner>
      <p>{{message}}</p>
    </div>
  `,
  styles: [
  ]
})
export class CustomBlockUIComponent {
  @Input() message: any;
}

