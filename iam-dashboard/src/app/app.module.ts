import { BrowserModule } from '@angular/platform-browser';
import { NgModule, DoBootstrap, CUSTOM_ELEMENTS_SCHEMA, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { AppRoutingModule } from './app-routing.module';
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
import { AppConfigService } from './app-config.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CookieService } from 'ngx-cookie-service';


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
    MatCardModule,
    MatSnackBarModule
  ],
  providers: [
    {
      provide: KeycloakService,
      useValue: keycloakService
    },
    FormBuilder,
    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [AppConfigService],
      useFactory: (appConfigService: AppConfigService) => {
        return () => {
          return appConfigService.loadAppConfig();
        };
      }
    },
    CookieService
  ],
  entryComponents: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule implements DoBootstrap {

  constructor(private appConfigService: AppConfigService) {

  }

  async ngDoBootstrap(app) {

    let patharray: string[] = window.location.pathname.split("/");

    let userrealm: string = "";

    if(patharray[1]) {
      userrealm = patharray[1]; // index 0 is a /
    } else {
      userrealm = this.appConfigService.getFallbackRealm(); // Fall back to master realm if there isn't a realm found
    }

    try {
      await keycloakService.init({
        config: {
          url: this.appConfigService.getKeycloakBaseUrl(),
          realm: userrealm,
          clientId: this.appConfigService.getKeycloakClientId()
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
