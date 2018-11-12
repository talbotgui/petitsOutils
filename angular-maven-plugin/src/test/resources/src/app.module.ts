// Les modules Angular importés
import { NgModule } from '@angular/core';

// La configuration de l'application
import { environment } from '../environments/environment';

// Tous les composants applicatifs de ce module
import { AppComponent } from './app.component';
import { PageAccueilComponent } from './page-accueil/page-accueil.component';
import { PageConnexionComponent } from './page-connexion/page-connexion.component';
import { CadreMenuComponent } from './cadre-menu/cadre-menu.component';

// Les composants injectabables de ce module
import { SecuriteService } from './service/securite.service';
import { PwaService } from './service/pwa.service';
import { AuthGuard } from './service/auth-guard.service';

// Import des modules
import { SharedModule, l10nConfig } from './shared/shared.module';
import { ReservationsModule } from './reservations/reservations.module';
import { AdministrationModule } from './administration/administration.module';

// Le composant contenant les routes
import { AppRoutingModule } from './app-routing.module';
import { AdministrationRoutingModule } from './administration/administration-routing.module';
import { ReservationsRoutingModule } from './reservations/reservations-routing.module';

// Déclaration du module
@NgModule({

  // Le composant principal
  bootstrap: [AppComponent],

  // Tous les composants applicatifs de ce module
  declarations: [AppComponent, PageAccueilComponent, PageConnexionComponent, CadreMenuComponent],

  // Les modules importés
  imports: [

    // Le module partagé
    SharedModule,

    // Déclaration des modules applicatifs
    ReservationsModule, AdministrationModule,

    // Déclaration des routes
    AppRoutingModule, AdministrationRoutingModule, ReservationsRoutingModule
  ],

  // Les composants injectables
  providers: [
    // Les services propre à ce module
    SecuriteService, PwaService, AuthGuard, PwaService
  ]
})

/** Module principal */
export class AppModule {

}
