import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Le composant de sécurité
import { AuthGuard } from '../service/auth-guard.service';

// Tous les composants applicatifs de l'application
import { PageUtilisateurComponent } from './page-utilisateur/page-utilisateur.component';
import { PageClientComponent } from './page-client/page-client.component';

/** Toutes les routes */
const routes: Routes = [
  { path: 'page-utilisateur-route', component: PageUtilisateurComponent, canActivate: [AuthGuard] },
  { path: 'page-client-route', component: PageClientComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
/** Module de déclaration des routes */
export class AdministrationRoutingModule { }
