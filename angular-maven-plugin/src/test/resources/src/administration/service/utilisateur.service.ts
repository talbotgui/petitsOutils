import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { RestUtilsService } from '../../shared/service/restUtils.service';
import { environment } from '../../../environments/environment';
import { HttpProxy } from '../../shared/service/httpProxy.component';

import * as model from '../../model/model';

/** Composant TS d'interface avec les API Back de manipulation des utilisateurs */
@Injectable()
export class UtilisateurService {

  /** Un constructeur pour se faire injecter les dépendances */
  constructor(private http: HttpProxy, private restUtils: RestUtilsService) { }

  /** Lit la liste complète des utilisateurs */
  listerUtilisateurs(): Observable<{} | model.Utilisateur[]> {
    const url = environment.baseUrl + '/v1/utilisateurs';
    return this.http.get<model.Utilisateur[]>(url, this.restUtils.creerHeader());
  }

  /** Création d'un utilisateur */
  sauvegarderUtilisateur(utilisateur: model.Utilisateur): Observable<{} | void> {
    const donnees = new HttpParams()
      .set('login', utilisateur.login)
      .set('mdp', utilisateur.mdp);

    const url = environment.baseUrl + '/v1/utilisateurs';
    return this.http.post<void>(url, donnees, this.restUtils.creerHeaderPost());
  }

  /** Suppression d'un utilisateur */
  supprimerUtilisateur(utilisateur: model.Utilisateur): Observable<{} | void> {
    const url = environment.baseUrl + '/v1/utilisateurs/' + utilisateur.login;
    return this.http.delete<void>(url, this.restUtils.creerHeaderPost());
  }
}
