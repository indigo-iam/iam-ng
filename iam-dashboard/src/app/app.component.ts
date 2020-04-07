import { Component, OnInit } from '@angular/core';
import { KeycloakProfile } from 'keycloak-js';
import { KeycloakService } from 'keycloak-angular';
import { Router, NavigationStart, NavigationEnd, NavigationCancel, NavigationError } from '@angular/router';
import { LoadingService } from './loading/loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'IAM Dashboard';
  userDetails: KeycloakProfile;

  constructor(private keycloakService: KeycloakService, private router: Router, private loadingService: LoadingService) {
    this.router.events.subscribe((event) => {
        if (event instanceof NavigationStart) {
          this.loadingService.show();
        }

        if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
          this.loadingService.hide();
        }
      }
    );
  }

  async ngOnInit() {
    if (await this.keycloakService.isLoggedIn()) {
      this.userDetails = await this.keycloakService.loadUserProfile();
      console.log(this.userDetails);
    }
  }

  async doLogout() {
    await this.keycloakService.logout();
  }
}
