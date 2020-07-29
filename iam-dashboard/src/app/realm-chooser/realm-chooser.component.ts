import { Component, OnInit } from '@angular/core';
import { RealmService } from '../services/realm.service';

@Component({
  selector: 'app-realm-chooser',
  templateUrl: './realm-chooser.component.html',
  styleUrls: ['./realm-chooser.component.scss']
})
export class RealmChooserComponent implements OnInit {

  realms;
  constructor(private realmService: RealmService) { }

  ngOnInit(): void {
    this.realmService.getRealms().subscribe(
      (response) => {
        this.realms = response.resources;
      }
    );
  }

}
