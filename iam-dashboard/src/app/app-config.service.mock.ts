import { Injectable, Injector } from '@angular/core';

@Injectable({providedIn: 'root'})
export class MockAppConfigService {

  constructor() {
  }

  getFallbackRealm(): string {
    return '';
  }

  getIamApiBaseUrl(): string {
    return '';
  }

  getKeycloakBaseUrl(): string {
    return '';
  }

  getKeycloakClientId(): string {
    return '';
  }
}
