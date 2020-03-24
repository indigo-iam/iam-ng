import { Injectable } from '@angular/core';

import { AppConfigService } from '../app-config.service';
import { Observable } from 'rxjs';
import { ListResponseDTO } from 'src/app/models/list-response-dto';
import { RealmDTO } from 'src/app/models/realm-dto';
import { AppConfig } from 'src/app/models/app-config';
import { HttpClient } from '@angular/common/http';
import { Utils } from 'src/app/utils/utils';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RealmService {

  iamApiBaseUrl: string;

  constructor(private appConfigService: AppConfigService, private http: HttpClient) {
    this.iamApiBaseUrl = this.appConfigService.getIamApiBaseUrl();
  }

  public getRealms(): Observable<ListResponseDTO<RealmDTO>> {
    return this.http.get<ListResponseDTO<RealmDTO>>(this.iamApiBaseUrl + "Realms");
  }


}
