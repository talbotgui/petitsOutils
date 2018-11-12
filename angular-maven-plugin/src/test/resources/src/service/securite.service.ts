import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

import { RestUtilsService } from '../shared/service/restUtils.service';
import { environment } from '../../environments/environment';
import { HttpProxy } from '../shared/service/httpProxy.component';

import * as model from '../model/model';

/** Composant TS de gestion de la sécurité */
@Injectable()
export class SecuriteService {

  /** Flag indiquant que l'utilisateur s'est déconnecté */
  private aDemandeLaDeconnexion = false;

  /** Flag evitant l'appel REST pour valider le token */
  private tokenDejaValide = false;

  /** Un constructeur pour se faire injecter les dépendances */
  constructor(private http: HttpProxy, private restUtils: RestUtilsService) { }

  /** Tentative de connexion d'un utilisateur */
  connecter(login: string, mdp: string, callback: () => void, callbackErreurParametresConnexion: (error: any) => void): void {

    // Pour lever le flag sur le cache de la méthode estConnecte()
    this.aDemandeLaDeconnexion = false;

    // Appel au login
    const donnees = { login, mdp };
    this.http.postForResponse<void>(environment.baseUrl + '/login', donnees, { observe: 'response' })
      .pipe(catchError<any>((error) => {
        // Suppression du token si le login est une erreur
        localStorage.removeItem('JWT');

        // Dans le cas d'une erreur 403 (paramètres de connexion par exemple)
        // appel à la callback pour que le composant traite son erreur
        if (error.status && error.status === 403) {
          callbackErreurParametresConnexion(error);
        }

        // On laisse se faire le traitement global d'erreur
        throw error;
      }))
      .subscribe((reponse: HttpResponse<void>) => {

        // Lecture du token
        const token = reponse.headers.get('Authorization');

        // Sauvegarde du token dans le localstorage
        if (token) {
          localStorage.setItem('JWT', token);
          this.tokenDejaValide = true;
        }

        // Appel à la callback
        callback();
      });
  }

  /** Demande de déconnexion */
  deconnecter(): void {
    this.aDemandeLaDeconnexion = true;
    localStorage.removeItem('JWT');
  }

  /** Informe si l'utilisateur est bien connecté */
  estConnecte(): Observable<{} | boolean> {
    if (this.aDemandeLaDeconnexion) {
      return of(false);
    } else if (this.tokenDejaValide) {
      const token = localStorage.getItem('JWT');
      return of(!!token);
    } else {
      return this.invaliderTokenSiPresentEtExpire();
    }
  }

  /**
   * Appel à l'API REST /utilisateurs/moi et vérification de l'expiration du token
   *
   * S'il est encore bon, rien ne se passe. Sinon, le localStorage est vidé.
   */
  invaliderTokenSiPresentEtExpire(): Observable<{} | model.Utilisateur> {
    const url = environment.baseUrl + '/v1/utilisateurs/moi';
    return this.http.get<model.Utilisateur>(url, this.restUtils.creerHeader()).pipe(catchError<any, boolean>(() => {
      localStorage.removeItem('JWT');
      return of(false);
    }));
  }
}
