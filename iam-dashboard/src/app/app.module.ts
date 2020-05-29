import { BrowserModule } from '@angular/platform-browser';
import { NgModule, DoBootstrap, CUSTOM_ELEMENTS_SCHEMA, APP_INITIALIZER, Inject, Injector } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationComponent } from './user/registration/registration.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatCardModule } from '@angular/material/card';
import { FormBuilder, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './utils/page-not-found/page-not-found.component';
import { AppConfigService } from './app-config.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CookieService } from 'ngx-cookie-service';
import { LoadingComponent } from './utils/loading/loading.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { LoadingService } from './utils/loading/loading.service';
import { LoadingInterceptor } from './utils/loading/loading.interceptor';
import { BlockUIModule } from 'ng-block-ui';
import { EmailConfirmationComponent } from './user/email-confirmation/email-confirmation.component';
import { RegistrationRequestsComponent } from './user/registration-requests/registration-requests.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { PermissionDeniedComponent } from './utils/permission-denied/permission-denied.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CustomBlockUIComponent } from './utils/custom-block-ui/custom-block-ui.component';
import { ClientManagementComponent } from './client/client-management/client-management.component';
import { NewOrEditClientComponent } from './client/new-or-edit-client/new-or-edit-client.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ClientDetailsComponent } from './client/client-details/client-details.component';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { TextFieldModule } from '@angular/cdk/text-field';
import { ConfirmationDialogComponent } from './utils/confirmation-dialog/confirmation-dialog.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { NavigationComponent } from './navigation/navigation.component';
import { RealmChooserComponent } from './realm-chooser/realm-chooser.component';
import { UserManagementComponent } from './user/user-management/user-management.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { SimpleNotificationsModule } from 'angular2-notifications';

const keycloakService: KeycloakService = new KeycloakService();

@NgModule({
  declarations: [AppComponent, RegistrationComponent, LoginComponent, PageNotFoundComponent, LoadingComponent, EmailConfirmationComponent, RegistrationRequestsComponent, PermissionDeniedComponent, CustomBlockUIComponent, ClientManagementComponent, NewOrEditClientComponent, ClientDetailsComponent, ConfirmationDialogComponent, NavigationComponent, RealmChooserComponent, UserManagementComponent, UserProfileComponent],
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
    MatSnackBarModule,
    MatProgressBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    ClipboardModule,
    TextFieldModule,
    BlockUIModule.forRoot({
      template: CustomBlockUIComponent
    }),
    LayoutModule,
    MatSidenavModule,
    MatListModule,
    FormsModule,
    SimpleNotificationsModule.forRoot()
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
    CookieService,
    LoadingService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true
    }
  ],
  entryComponents: [AppComponent, CustomBlockUIComponent, NewOrEditClientComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule implements DoBootstrap {

  constructor(private appConfigService: AppConfigService) {}

  async ngDoBootstrap(app) {

    const patharray: string[] = window.location.pathname.split('/');

    let userrealm = '';

    if (patharray[1]) {
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
          // promiseType: 'native' Will's Note: keycloak-angular library does not currently support this.
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
