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
import { ClientManagementComponent } from './client/client-management/client-management.component';
import { NavigationComponent } from './navigation/navigation.component';
import { RealmChooserComponent } from './realm-chooser/realm-chooser.component';
import { UserManagementComponent } from './user/user-management/user-management.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { DashboardRootComponent } from './dashboard-root/dashboard-root.component';

const routes: Routes = [
  {
    path: '404',
    component: PageNotFoundComponent,
    data: {
      title: 'Page not found'
    }
  },
  {
    path: '403',
    component: PermissionDeniedComponent,
    data: {
      title: 'Permission denied'
    }
  },
  {
    path: '',
    component: RealmChooserComponent,
    data: {
      title: 'Choose a realm'
    }
  },
  /*{
    path: ':realm',
    component: LoginComponent,
    data: {
      title: 'Login or register'
    }
  },*/
  {
    path: ':realm/register',
    component: RegistrationComponent,
    data: {
      title: 'Register'
    }
  },
  {
    path: ':realm/registeridp',
    component: RegistrationComponent,
    data: {
      title: 'Register with IdP',
      idpRegistration: true
    },
    canActivate: [AppAuthGuard]
  },
  {
    path: ':realm/register/confirm/:token',
    component: EmailConfirmationComponent,
    data: {
      title: 'Confirm your email address'
    }
  },
  {
    path: ':realm',
    redirectTo: ':realm/dashboard',
    pathMatch: 'full'
  },
  {
    path: ':realm',
    component: NavigationComponent,
    data: {
      title: 'IAM Dashboard'
    },
    children: [
      {
        path: 'dashboard',
        component: DashboardRootComponent,
        data: {
          title: 'IAM Dashboard',
          roles: ['iam-admin']
        },
        canActivate: [AppAuthGuard]
      },
      {
        path: 'dashboard/requests/registration',
        component: RegistrationRequestsComponent,
        data: {
          roles: ['iam-admin'], // Restrict this page for the admin role only (we pass this data to the Auth Guard)
          title: 'Registration requests'
        },
        canActivate: [AppAuthGuard] // Run the auth guard against the page
      },
      {
        path: 'dashboard/client-management',
        component: ClientManagementComponent,
        data: {
          roles: ['iam-admin'],
          title: 'Client management'
        },
        canActivate: [AppAuthGuard]
      },
      {
        path: 'dashboard/user-management',
        component: UserManagementComponent,
        data: {
          roles: ['iam-admin'],
          title: 'User management'
        },
        canActivate: [AppAuthGuard]
      },
      {
        path: 'dashboard/user/:userid',
        component: UserProfileComponent,
        data: {
          roles: ['iam-admin'],
          title: 'User profile'
        },
        canActivate: [AppAuthGuard]
      }
    ]
  },
  {
    path: '**',
    component: PageNotFoundComponent,
    data: {
      title: 'Page not found'
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AppAuthGuard]
})
export class AppRoutingModule {}
