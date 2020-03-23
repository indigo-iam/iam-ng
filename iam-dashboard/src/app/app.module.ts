import { BrowserModule } from '@angular/platform-browser';
import { NgModule, DoBootstrap, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { AppRoutingModule } from './app-routing.module';

import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './registration/registration.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatCardModule } from '@angular/material/card';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ActivatedRoute } from '@angular/router';


let keycloakService: KeycloakService = new KeycloakService();

@NgModule({
  declarations: [AppComponent, RegistrationComponent, LoginComponent, PageNotFoundComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    KeycloakAngularModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatCardModule
  ],
  providers: [
    {
      provide: KeycloakService,
      useValue: keycloakService
    },
    FormBuilder
  ],
  entryComponents: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule implements DoBootstrap {

  constructor(private route: ActivatedRoute) {

  }

  async ngDoBootstrap(app) {
    const { keycloakConfig } = environment;

    let patharray: string[] = window.location.pathname.split("/");

    let userrealm: string = "";

    if(patharray[1]) {
      userrealm = patharray[1]; // index 0 is a /
    } else {
      userrealm = "master"; // Fall back to master realm if there isn't a realm found
    }

    try {
      await keycloakService.init({
        config: {
          url: 'https://localhost:8443/auth/',
          realm: userrealm,
          clientId: 'iam-dashboard'
        },
        initOptions: {
          checkLoginIframe: false
          //promiseType: "native" Will's Note: keycloak-angular library does not currently support this.
        },
        enableBearerInterceptor: true,
        bearerExcludedUrls: [],
      });
      app.bootstrap(AppComponent);
    } catch (error) {
      console.error('Keycloak init failed', error);
    }
  }
}
