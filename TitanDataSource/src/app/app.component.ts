import {Component, OnInit, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {SessionService} from 'src/services/session/SessionService';

declare var dojo: any;
declare var esri: any;
declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['../app/app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  public title = 'titans.datasource';

  /**
   * 
   */
  public constructor(private session: SessionService, private router: Router, private location: Location) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    console.log("application on init ");
    if (this.session.isNotLoggedIn()) {
      console.log("routing to login");
      this.router.navigateByUrl("/login", {skipLocationChange: false});
    } else {
      this.session.loggedin.subscribe(this.initNavigationBasedOnLogin.bind(this));
    }
  }

  /**
   * 
   */
  private initNavigationBasedOnLogin(loggedin: boolean): void {
    const locationpath = this.location.path();
    console.log("location path = " + locationpath);
    console.log("loggedin =" + loggedin);
    if (loggedin && (locationpath === '/login' || locationpath === '')) {
      this.router.navigateByUrl("/home/main", {skipLocationChange: false});
    } else if (!loggedin && locationpath !== "/login") {
      this.router.navigateByUrl("/login", {skipLocationChange: false});
    }
  }

  /**
   * 
   */
  public ngOnDestroy(): void {
  }
}
