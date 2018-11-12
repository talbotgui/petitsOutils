import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, NavigationExtras } from '@angular/router';
import { map } from 'rxjs/operators';

import { SecuriteService } from '../service/securite.service';

/** Garde permettant de protéger une route contre l'accès d'un utilisateur non connecté. */
@Injectable()
export class AuthGuard implements CanActivate {

  /** Constructeur avec injection */
  constructor(private router: Router, private securiteService: SecuriteService) { }

  /** Garde contre l'accès d'un utilisateur non connecté (renvoi à la route de connexion si nécessaire) */
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.securiteService.estConnecte().pipe(map(((b) => {
      if (!!b) {
        return true;
      } else {
        const navigationExtras: NavigationExtras = { queryParams: { redirect: state.url } };
        this.router.navigate(['page-connexion-route'], navigationExtras);
        return false;
      }
    })));
  }
}
