import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Routes } from '@angular/router';
import { AppAuthGuard } from './app.authguard';
import { RegistrationComponent } from './user/registration/registration.component';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './utils/page-not-found/page-not-found.component';
import { EmailConfirmationComponent } from './user/email-confirmation/email-confirmation.component';
import { RegistrationRequestsComponent } from './user/registration-requests/registration-requests.component';
import { PermissionDeniedComponent } from './utils/permission-denied/permission-denied.component';

const routes: Routes = [
  {
    path: '404',
    component: PageNotFoundComponent
  },
  {
    path: '403',
    component: PermissionDeniedComponent
  },
  {
    path: ':realm',
    component: LoginComponent
  },
  {
    path: ':realm/register',
    component: RegistrationComponent
  },
  {
    path: ':realm/register/confirm/:token',
    component: EmailConfirmationComponent
  },
  {
    path: ':realm/dashboard/requests/registration',
    component: RegistrationRequestsComponent,
    data: {
      roles: ['iam-admin'] // Restrict this page for the admin role only (we pass this data to the Auth Guard)
    },
    canActivate: [AppAuthGuard] // Run the auth guard against the page
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AppAuthGuard]
})
export class AppRoutingModule {}
