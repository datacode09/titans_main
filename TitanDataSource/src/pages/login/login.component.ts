import {OnInit, Component} from '@angular/core';
import {SessionService} from 'src/services/session/SessionService';

@Component({
  selector: 'login-root',
  templateUrl: './login.component.html',
  styleUrls: ['../../app/app.component.css', 'login.component.css']
})
export class LoginComponent implements OnInit {
  public message: string = "";
  public email: string = "";
  public password: string = "";
  private firstattempt: boolean = true;

  /**
   * 
   */
  public constructor(private service: SessionService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.service.isNotLoggedIn()
  }

  /**
   * 
   */
  public onSubmit(): void {
    this.message = '';
    this.firstattempt = true;
    this.service.login(this.email, this.password)
      .subscribe(this.onPostLogin.bind(this));
  }

  /**
   * 
   */
  private onPostLogin(success: boolean): void {
    if (!this.firstattempt) {
      if (!success) {
        this.message = "The e-mail or password is invalid"
      }
    }
    this.firstattempt = false;
  }

  /**
   * 
   */
  public onDummyRequest(): void {
    this.service.dummyrequest();
  }
}

