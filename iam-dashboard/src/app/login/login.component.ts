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

  }

  ngOnInit(): void {

  }

  getCustomAttribute(key: string): string {
    return this.appConfigService.getCustomAttribute(this.realmName, 'login', key);
  }

  attributeExists(key: string): boolean {
    return this.appConfigService.attributeExists(this.realmName, 'login', key);
  }

}
