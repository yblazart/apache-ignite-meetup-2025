import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {
    this.checkInitialAuth();
  }

  private checkInitialAuth(): void {
    const token = localStorage.getItem('token');
    this.isAuthenticatedSubject.next(!!token);
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';
    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = error.error.message;
    } else {
      // Erreur côté serveur
      errorMessage = error.error?.message || 'Le serveur ne répond pas';
    }
    return throwError(() => ({ message: errorMessage }));
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, { email, password })
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          this.isAuthenticatedSubject.next(true);
        }),
        catchError(this.handleError)
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  resetPassword(email: string, password: string): Observable<any> {
    return this.http.post('/api/auth/reset-password', { email, password });
  }

  decodeToken(): any {
    const token = this.getToken();
    if (token) {
      try {
        const payloadBase64 = token.split('.')[1];
        const payload = atob(payloadBase64);
        return JSON.parse(payload);
      } catch (e) {
        console.error('Error decoding token:', e);
        return null;
      }
    }
    return null;
  }
  getLoggedInUserEmail(): string | null {
    const decodedToken = this.decodeToken();
    return decodedToken ? decodedToken.sub : null;
  }
}