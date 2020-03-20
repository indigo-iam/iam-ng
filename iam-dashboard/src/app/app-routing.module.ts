import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {RootComponent} from './root/root.component';
import {WelcomeComponent} from './welcome/welcome.component';


const routes: Routes = [
  {path: '', component: RootComponent},
  {path: ':realm', component: WelcomeComponent}
];

@NgModule({imports: [RouterModule.forRoot(routes)], exports: [RouterModule]})
export class AppRoutingModule {
}
