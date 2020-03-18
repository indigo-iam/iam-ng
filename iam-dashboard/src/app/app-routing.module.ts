import { NgModule, Component } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Routes } from '@angular/router';
import { AppAuthGuard } from './app.authguard';
import { AppComponent } from './app.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [AppAuthGuard],
    component: AppComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AppAuthGuard]
})
export class AppRoutingModule {}
