import {Component, OnInit} from '@angular/core';

import {Realm} from '../models/realm';
import {RealmsService} from '../realms.service';

@Component({
  selector: 'app-root',
  templateUrl: './root.component.html',
  styleUrls: ['./root.component.css']
})
export class RootComponent implements OnInit {
  public realms: Realm[];

  constructor(private realmsService: RealmsService) {}

  ngOnInit(): void {
    this.realmsService.getRealms().subscribe(r => {
      this.realms = r.resources;
    });
  }
}
