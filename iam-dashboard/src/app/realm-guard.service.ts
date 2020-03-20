import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';

import {Realm} from './models/realm';

@Injectable({providedIn: 'root'})
export class RealmGuardService implements CanActivate {
  public REALM_KEY = 'realm';
  private realms: Realm[];

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      boolean|UrlTree {
    const realm: string = route.params[this.REALM_KEY];

    if (!realm) {
      return this.router.parseUrl('');
    }


    return false;
  }
}
