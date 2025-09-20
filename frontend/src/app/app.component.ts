import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav *ngIf="isAuthenticated()">
      <div class="nav-content">
        <div>
          <strong>Study Tracker</strong>
        </div>
        <div>
          <a routerLink="/today" routerLinkActive="active">Hoje</a>
          <a routerLink="/questions" routerLinkActive="active">Questões</a>
          <a routerLink="/history" routerLinkActive="active">Histórico</a>
          <span style="margin-left: 1rem; opacity: 0.8;">{{ displayName() }}</span>
          <button class="btn" style="margin-left: 1rem;" (click)="logout()">Sair</button>
        </div>
      </div>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly displayName = computed(() => this.auth.displayName());

  isAuthenticated(): boolean {
    return this.auth.isAuthenticated();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
