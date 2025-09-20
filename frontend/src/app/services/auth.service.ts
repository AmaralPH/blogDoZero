import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

export interface AuthResponse {
  token: string;
  username: string;
  displayName: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'study-tracker-token';
  private readonly apiUrl = 'http://localhost:8080';
  private readonly userNameSignal = signal<string | null>(null);

  readonly displayName = this.userNameSignal.asReadonly();

  constructor(private readonly http: HttpClient) {
    const token = this.getToken();
    const name = localStorage.getItem('study-tracker-display');
    if (token && name) {
      this.userNameSignal.set(name);
    }
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/login`, { username, password })
      .pipe(tap((res) => this.persistAuth(res)));
  }

  register(username: string, password: string, displayName: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/register`, { username, password, displayName })
      .pipe(tap((res) => this.persistAuth(res)));
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    localStorage.removeItem('study-tracker-display');
    this.userNameSignal.set(null);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.storageKey);
  }

  private persistAuth(res: AuthResponse): void {
    localStorage.setItem(this.storageKey, res.token);
    localStorage.setItem('study-tracker-display', res.displayName);
    this.userNameSignal.set(res.displayName);
  }
}
