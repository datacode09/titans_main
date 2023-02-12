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
    if (this.session.isNotLoggedIn()) {  
      this.router.navigateByUrl("/login", {skipLocationChange: false, });
      
    }
    this.session.loggedin.subscribe(this.initNavigationBasedOnLogin.bind(this));
  }

  /**
   * 
   */
  private initNavigationBasedOnLogin(loggedin: boolean): void {
    if (loggedin) {
      if (this.location.path() === '/login') {  
        this.router.navigateByUrl("/home/main", {skipLocationChange: false});
      }
    } else if (this.location.path() !== "/login") {
      this.router.navigateByUrl("/login", {skipLocationChange: false});
    }
  }
  
  /**
   * 
   */
  public ngOnDestroy(): void {
  }
}
