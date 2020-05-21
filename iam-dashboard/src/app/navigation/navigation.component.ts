import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { RealmService } from '../services/realm.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  realmName: string;

  constructor(private breakpointObserver: BreakpointObserver, private route: ActivatedRoute, private realmService: RealmService) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      this.realmName = paramMap.get('realm');
      this.realmService.setCurrentRealm(this.realmName);
    });
  }

}
