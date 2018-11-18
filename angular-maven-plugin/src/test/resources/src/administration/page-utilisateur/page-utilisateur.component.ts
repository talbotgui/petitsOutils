import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Language } from 'angular-l10n';

import { UtilisateurService } from '../service/utilisateur.service';
import * as model from '../../model/model';

/**
 * Page de visualisation et création des utilisateurs
 */
@Component({ selector: 'page-utilisateur', templateUrl: './page-utilisateur.component.html', styleUrls: ['./page-utilisateur.component.css'] })
export class PageUtilisateurComponent implements OnInit {

  /** Decorateur nécessaire aux libellés internationnalisés dans des tooltips */
  @Language() lang: string;

  /** Liste des utilisateurs à afficher */
  utilisateurs: model.Utilisateur[];

  /** Utilisateur en cours d'édition */
  utilisateurSelectionne: model.Utilisateur | undefined;

  /** Flag permettant de savoir si c'est une création ou une modification (pour bloquer le login) */
  creation: boolean = false;

  /** Un constructeur pour se faire injecter les dépendances */
  constructor(private route: ActivatedRoute, private utilisateurService: UtilisateurService) { }

  /** Appel au service à l'initialisation du composant */
  ngOnInit(): void {

    this.chargerDonnees();

    // Si des paramètres sont présents, initialisation du formulaire avec les données de l'objet indiqué
    this.route.params.subscribe((params: { [key: string]: any }) => {
      /* tslint:disable-next-line */
      const loginUtilisateur = params['loginUtilisateur'];
      if (loginUtilisateur) {
        this.utilisateurs.forEach((u) => {
          if (u.login === loginUtilisateur) {
            this.utilisateurSelectionne = u;
          }
        });
      }
    });
  }

  /** Chargement de la liste des utilisateurs */
  chargerDonnees() {

    // Chargement de la liste des utilisateurs
    this.utilisateurService.listerUtilisateurs().subscribe((liste: model.Utilisateur[]) => {
      this.utilisateurs = liste;
    });

    // Reset du formulaire
    this.annulerCreationUtilisateur();
  }

  /** On annule la creation en masquant le formulaire */
  annulerCreationUtilisateur() {
    this.utilisateurSelectionne = undefined;
  }

  /** Initialisation de l'objet Utilisateur pour accueillir les données du formulaire */
  creerUtilisateur() {
    this.utilisateurSelectionne = new model.Utilisateur();
    this.creation = true;
  }

  /** Appel au service de sauvegarde puis rechargement des données */
  sauvegarderUtilisateur() {
    if (this.utilisateurSelectionne) {
      this.utilisateurService.sauvegarderUtilisateur(this.utilisateurSelectionne)
        .subscribe((retour) => { this.chargerDonnees(); });
    }
  }

  /** A la sélection */
  selectionnerUtilisateur(utilisateur: model.Utilisateur) {
    this.utilisateurSelectionne = utilisateur;
    this.creation = false;
  }

  /** A la suppression  */
  supprimerUtilisateur(utilisateur: model.Utilisateur) {
    this.utilisateurService.supprimerUtilisateur(utilisateur)
      .subscribe((retour) => { this.chargerDonnees(); });
  }

  /** Méthode appelée par le composant parent par exemple (@@angular:analyse:ignorerLigneSuivante@@)*/
  methodeIgnoree() {
  }
}
