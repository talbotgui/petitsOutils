// Les modules Angular importés
import { NgModule } from '@angular/core';

// Import du module partagé
import { SharedModule } from '../shared/shared.module';

// Tous les composants applicatifs de l'application
import { PageUtilisateurComponent } from './page-utilisateur/page-utilisateur.component';
import { PageClientComponent } from './page-client/page-client.component';

// Les composants injectables
import { UtilisateurService } from './service/utilisateur.service';
import { ClientService } from './service/client.service';

@NgModule({
  imports: [
    // Le module partagé
    SharedModule
  ],

  // Tous les composants applicatifs du module
  declarations: [
    PageUtilisateurComponent, PageClientComponent
  ],

  // Les services
  providers: [
    UtilisateurService, ClientService
  ]
})
export class AdministrationModule { }
