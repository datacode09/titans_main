import {OnInit, Component} from '@angular/core';
import {SessionService} from 'src/services/session/SessionService';

@Component({
  selector: "header-root"
  , templateUrl: "./header.component.html"
  , styleUrls: ["../app.component.css", 'header.component.css']
})
export class HeaderComponent implements OnInit {

  public constructor(private session: SessionService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
  }

  /**
   * 
   */
  public logout(): void {
    this.session.logout();
  }
}

