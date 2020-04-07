import { Component, OnInit } from '@angular/core';
import { AppConfigService } from '../app-config.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  realmName = '';
  loginOrder = {};

  constructor(private appConfigService: AppConfigService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe((paramMap) => {
      this.realmName = paramMap.get('realm');
    });

    this.loginOrder = this.appConfigService.getLoginOrder(this.realmName);
    if (this.loginOrder === null) {
      // Default order
      this.loginOrder = {
        edugain: 1,
        local: 2,
        register: 3
      };
    }
  }

  ngOnInit(): void {

  }

  getCustomAttribute(key: string): string {
    return this.appConfigService.getCustomAttribute(this.realmName, 'login', key);
  }

  attributeExists(key: string): boolean {
    return this.appConfigService.attributeExists(this.realmName, 'login', key);
  }

  getLoginOrderClass(type: string): string {
    if (this.loginOrder.hasOwnProperty(type)) {
      return 'order-' + this.loginOrder[type];
    } else {
      return 'order-1';
    }
  }

}
