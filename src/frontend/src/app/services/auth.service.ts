import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, catchError, Observable, of, switchMap, throwError } from "rxjs";
import { JWTdata, UserCredentialRequest, UserRegisterRequest } from "../interfaces/types";
import { environment } from "../../environment/environment";
import * as jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private client = inject(HttpClient);
  private springApiUrl = environment.springApiUrl;
  private loginData$ = new BehaviorSubject<UserCredentialRequest>({
    login: '',
    password: ''
  });

  private registerData$ = new BehaviorSubject<UserRegisterRequest>({
    firstName: '',
    lastName: '',
    login: '',
    password: '',
    email: '',
    phone: ''
  });


  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  setLoginData(formData: UserCredentialRequest) {
    this.loginData$.next(formData);
  }

  setRegisterData(formData: UserRegisterRequest) {
    this.registerData$.next(formData);
  }

  setToken(token: string): void {
    sessionStorage.setItem('token', token);
    this.loggedIn.next(true);
  }
  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  isLoggedIn(): Observable<boolean> {
    const token = this.getToken();
    if (!token) return of(false);
    try {
      const decoded: any = jwt_decode.jwtDecode(token);
      const expirationDate = decoded.exp * 1000;
      const isExpired = expirationDate < Date.now();
      if (isExpired) {
        this.logout();
        return of(false);
      }
    } catch (error) { }

    return this.loggedIn.asObservable();
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  decodeToken(): JWTdata | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwt_decode.jwtDecode(token);
    } catch (error) {
      console.error('Invalid token:', error);
      return null;
    }
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.loggedIn.next(false);
  }


  registerPost(): Observable<any> {
    // console.log(this.registerData$)
    return this.registerData$.pipe(
      switchMap(formData => this.client.post<UserRegisterRequest>(`${this.springApiUrl}/auth/register`, formData,
        { observe: 'response' })),
      catchError((error) => throwError(() => error))
    );
  }

  loginPost(): Observable<any> {
    // console.table(this.loginData$)
    return this.loginData$.pipe(
      switchMap(formData => this.client.post<UserCredentialRequest>(`${this.springApiUrl}/auth/login`, formData,
        { observe: 'response' })),
      catchError((error) => throwError(() => error))
    );
  }
}
