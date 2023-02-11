import {Injectable} from '@angular/core';
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {SessionService} from 'src/services/session/SessionService';

@Injectable({
  providedIn: 'root',
  deps: [Router, SessionService, Location]
})
export class AuthInterceptor  implements HttpInterceptor{
  
  /**
   * 
   */
  public constructor(private router: Router, //
    private session: SessionService, private location: Location) {

  }
  /**
   * 
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let result; 
    if (request.url.endsWith("login") || request.url.endsWith("logout")) {
      result = next.handle(request);  
    } else if (this.session.isLoggedIn()) {
      result = next.handle(this.createAuthRequest(request));  
    } else {
      result = next.handle(request);  
    }
    return result;
  }
  
  
  /**
   * 
   */
  private createAuthRequest(request: HttpRequest<any>): HttpRequest<any> {
    const token = this.session.token.value;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'AUTH-TOKEN': token
    });
    const authRequest = request.clone({
      headers: headers
    });
    return authRequest;
  }
}

