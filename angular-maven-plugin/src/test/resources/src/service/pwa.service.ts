// Sources : https://medium.com/poka-techblog/turn-your-angular-app-into-a-pwa-in-4-easy-steps-543510a9b626

import { Injectable } from '@angular/core';
import { SwUpdate } from '@angular/service-worker';
import { BrowserComponent } from '../shared/service/browser.component';

/** Classe gérant l'installation PWA. */
@Injectable()
export class PwaService {

  /** Evenement reçu. */
  private promptEvent: any;

  /** Contructeur avec injection des dépendances */
  constructor(private swUpdate: SwUpdate, private browserComponent: BrowserComponent) {

    // Ajout d'un listener sur l'évènement envoyé par le navigateur si l'application
    // répond aux critères d'installation d'une PWA sur le terminal
    window.addEventListener('beforeinstallprompt', (event) => {
      this.promptEvent = event;
    });
  }

  /** Flag autorisant l'installation sur IOS */
  installationPwaManuelleIosAutorisee(): boolean {
    // Safari sur IOS>=11.3
    return this.browserComponent.nomNavigateur === 'Safari' && this.browserComponent.nomOs === 'IOS' && parseFloat(this.browserComponent.versionOs) > 11.2;
  }

  /** L'application, dans ses conditions d'utilisation, est-elle éligible à l'installation */
  installationPwaSemiAutomatiqueAutorisee(): boolean {
    // il ne faut pas que l'event ait déjà été traité
    return !!this.promptEvent && (
      // chrome avant la 68 (depuis le navigateur affiche un message automatiquement)
      this.browserComponent.nomNavigateur === 'Chrome' && this.browserComponent.versionMajeurNavigateur < 68
    );
  }

  /** Le navigateur supporte-t-il les PWA. */
  installationPwaImpossible(): boolean {
    // Chrome depuis la 68
    return !(
      (this.browserComponent.nomNavigateur === 'Chrome' && this.browserComponent.versionMajeurNavigateur > 67)
      // Safari sur IOS>=11.3
      || (this.browserComponent.nomNavigateur === 'Safari' && this.browserComponent.nomOs === 'IOS' && parseFloat(this.browserComponent.versionOs) > 11.2)
    );

  }

  /** Installation de l'application PWA */
  installerPwa() {
    this.promptEvent.prompt();
  }
}
