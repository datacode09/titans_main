import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Objects} from 'src/core/types/Objects';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private baseUrl: string = "http://localhost:8081/titansdata.web/";

  public loggedin: BehaviorSubject<boolean> = new BehaviorSubject(false);
  public token: BehaviorSubject<string> = new BehaviorSubject(null);

  /**
   * 
   */
  public constructor(private http: HttpClient) {
    this.initToken();
    this.token.subscribe(this.updateLoginState.bind(this));
    this.token.subscribe(this.storeToken.bind(this));
  }

  /**
   * 
   */
  private initToken(): void {
    console.log("hello");
    this.token.next(localStorage.getItem("token"));
  }

  /**
   * 
   */
  private storeToken(token: string) {
    if (Objects.isNotNull(token)) {
      localStorage.setItem("token", token);
    } else {
      localStorage.removeItem("token");
    }

  }

  /**
   * 
   */
  private updateLoginState(token: string): void {
    this.loggedin.next(token !== null);
  }

  /**
   * 
   */
  public isNotLoggedIn(): boolean {
    return this.loggedin.value;
  }


  /**
   * 
   */
  public login(email: string, password: string): Observable<boolean> {
    const url = this.baseUrl + "login";
    const params = new HttpParams().set("email", email);
    const headers = new HttpHeaders()
      .set("KEY", password)
      .set('Content-Type', 'application/json');
    const result = new BehaviorSubject<boolean>(false);
    this.http.post(url, {}, {
      params: params
      , headers: headers
    }).subscribe((r: any) => {
      const success = Objects.isNotNull(r.token);
      this.token.next(success ? r.token : null);
      result.next(success);
    });
    return result;
  }


  /**
   * 
   */
  public dummyrequest(): void {
    const url = this.baseUrl + "dummyrequest";
    this.http.get(url).subscribe((r: any) => {
      console.log(r);
    });
  }

  /**
   * 
   */
  public logout() {
    const url = this.baseUrl + "logout";
    if (Objects.isNotNull(this.token.value)) {
      const params = new HttpHeaders().set("AUTH-TOKEN", this.token.value);
      this.http.post(url, {}, {
        headers: params
      }).subscribe(r => {
        this.token.next(null);
      });
    }
  }

}

