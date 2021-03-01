import { Injectable } from '@angular/core';
import { AppConstants } from '@app/app.constants';
// import DataJson from '@assets/configs/data.json';
import { DataService } from './data.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class AuthenticationService {

  private congifToken = '';
  private sessionToken = new BehaviorSubject<string>('');
  public $sessionToken = this.sessionToken.asObservable();


  constructor(
    private dataService: DataService,
    private router: Router
  ) {
  }

  /*
  * This method sets JWT token which is configurable.
  */
  set token(token: string) {
    this.congifToken = token;
  }

  /*
  * This method sends configurable JWT token.
  * @returns congifToken.
  */
  get token() {
    return this.congifToken;
  }

  /*
  * This method sets JWT token after login.
  */
  set loginSessionToken(token: string) {
    this.sessionToken.next(token);
  }

  /**
   * This method sends true if JWT token is saved in session storage else it sends false.
   * @returns true if token is available else false.
   */
  isUserLoggedIn(): Observable<boolean> {
    const isLogin = new BehaviorSubject<boolean>(false);
    this.$sessionToken.subscribe(token => {
      isLogin.next(token?.length > 0 ?? false);
    });
    return isLogin.asObservable();
  }

  /**
   * This method clears sesssion storage, data service and redirects to login page.
   */
  logOut() {
    this.resetToken();
    this.redirectToLogIn();
  }

  resetToken() {
    this.loginSessionToken = '';
  }

  redirectToLogIn() {
    this.router.navigate([`${AppConstants.APP_URLS.LOGIN}`]);
  }
  
  redirectToHome() {
    this.router.navigate([`${AppConstants.APP_URLS.HOME}`]);
  }

}
