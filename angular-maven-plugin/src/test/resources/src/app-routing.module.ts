import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Le composant de sécurité
import { AuthGuard } from './service/auth-guard.service';

// Tous les composants applicatifs de l'application
import { PageAccueilComponent } from './page-accueil/page-accueil.component';
import { PageConnexionComponent } from './page-connexion/page-connexion.component';
import { PageUtilisateurComponent } from './administration/page-utilisateur/page-utilisateur.component';

/** Toutes les routes */
const routes: Routes = [
  // pour rediriger par défaut sur le dashboard
  { path: '', redirectTo: '/page-accueil-route', pathMatch: 'full' },
  { path: 'page-accueil-route', component: PageAccueilComponent, canActivate: [AuthGuard] },
  { path: 'page-utilisateur-route', component: PageUtilisateurComponent, canActivate: [AuthGuard] },
  { path: 'page-connexion-route', component: PageConnexionComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
/** Module de déclaration des routes */
export class AppRoutingModule { }
