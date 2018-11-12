import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';

import { RestUtilsService } from '../../shared/service/restUtils.service';
import { HttpProxy } from '../../shared/service/httpProxy.component';

import * as model from '../../model/model';

/** Composant TS d'interface avec les API Back de manipulation des clients */
@Injectable()
export class ClientService {

  /** Constructeur avec injection */
  constructor(private http: HttpProxy, private restUtils: RestUtilsService) { }

  /**
   * Liste des clients dans un DTO particulier (pas l'objet métier) de manière paginée et triée.
   *
   * @param page La page demandée (nb éléments par page, index de la page et ordre de tri)
   */
  listerClientsDto(page: model.Page<any>): Observable<{} | model.Page<model.ClientDto>> {

    // Seul un tri par défaut est possible
    let triParNom: string = '';
    if (page.sort) {
      if (page.sort.sortOrder === 'asc') {
        triParNom = 'true';
      } else {
        triParNom = 'false';
      }
    }

    // Appel à l'API
    const url = environment.baseUrl + '/v1/clients?pageNumber=' + page.number + '&pageSize=' + page.size + '&triParNom=' + triParNom;
    return this.http.get<model.Page<model.ClientDto>>(url, this.restUtils.creerHeader());
  }

  /** Sauvegarde d'un client via l'API */
  sauvegarderClient(client: model.ClientDto): Observable<{} | void> {
    let donnees = new HttpParams().set('nom', client.nomClient);
    if (client.reference) {
      donnees = new HttpParams().set('nom', client.nomClient).set('reference', client.reference);
    }

    const url = environment.baseUrl + '/v1/clients';
    return this.http.post<void>(url, donnees, this.restUtils.creerHeaderPost());
  }

  /** Suppression d'un client via l'API */
  supprimerClient(client: model.ClientDto): Observable<{} | void> {
    const url = environment.baseUrl + '/v1/clients/' + client.reference;
    return this.http.delete<void>(url, this.restUtils.creerHeader());
  }
}
