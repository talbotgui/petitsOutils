import { Component } from '@angular/core';
import { Language } from 'angular-l10n';

import { LocaleService } from 'angular-l10n';
import { PwaService } from './service/pwa.service';

/** Composant ROOT de l'application */
@Component({ selector: 'app-root', templateUrl: './app.component.html', styleUrls: ['./app.component.css'] })
export class AppComponent {

  /** Decorateur nécessaire aux libellés internationnalisés dans des tooltips */
  @Language() lang: string;

  /** Constructeur avec injection */
  constructor(public locale: LocaleService, private pwaService: PwaService) { }

  /** Méthode permettant de changer de langue */
  changerLaLangueDesLibelles(language: string): void {
    this.locale.setCurrentLanguage(language);
  }

  /** L'application, dans ses conditions d'utilisation, est-elle éligible à l'installation semi-automatique */
  get installationPwaSemiAutomatiqueAutorisee(): boolean {
    return this.pwaService.installationPwaSemiAutomatiqueAutorisee();
  }

  /** L'application, dans ses conditions d'utilisation, est-elle éligible à l'installation sur IOS */
  get installationPwaManuelleIosAutorisee(): boolean {
    return this.pwaService.installationPwaManuelleIosAutorisee();
  }

  /** Le navigateur ne supporte pas les PWA */
  get installationPwaImpossible(): boolean {
    return this.pwaService.installationPwaImpossible();
  }

  /** Installer l'application comme une application mobile */
  installerPwa(): void {
    this.pwaService.installerPwa();
  }
}
