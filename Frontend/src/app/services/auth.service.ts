import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import {jwtDecode, JwtPayload} from 'jwt-decode';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8001/auth';
  private tokenKey = 'token';
 
  constructor(private http: HttpClient, private router : Router) { }
 
  login(credentials: { username: string; password: string }) {
    return this.http.post<{ token: string }>(`${this.baseUrl}/login`, credentials).pipe(
      tap(response => localStorage.setItem('token', response.token))
    );
  }
 
  register(user: any) {
    return this.http.post(`${this.baseUrl}/register`, user, { responseType: 'text' as 'json' });
  }
 
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
 
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/user-home']);
  }
 
  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
