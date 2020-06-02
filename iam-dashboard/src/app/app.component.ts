import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, NavigationCancel, NavigationError } from '@angular/router';
import { LoadingService } from './utils/loading/loading.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'IAM Dashboard';

  constructor(private router: Router, private loadingService: LoadingService, private titleService: Title) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        this.loadingService.show();
      }

      if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
        this.loadingService.hide();
        if (event instanceof NavigationEnd) {
          const title = this.getTitle(this.router.routerState, this.router.routerState.root).reverse().join(' | ');
          this.titleService.setTitle(title);
        }
      }
    });
  }

  ngOnInit() {

  }

  // From: https://stackoverflow.com/questions/47900447/how-to-change-page-title-with-routing-in-angular-application
  getTitle(state, parent) {
    const data = [];
    if (parent && parent.snapshot.data && parent.snapshot.data.title) {
      data.push(parent.snapshot.data.title);
    }

    if (state && parent) {
      data.push(... this.getTitle(state, state.firstChild(parent)));
    }
    return data;
  }

  public setTitle(title: string) {
    this.titleService.setTitle(title);
  }
}
