import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="auth-wrapper">
      <div class="card auth-card">
        <h2>{{ mode() === 'login' ? 'Entrar' : 'Criar conta' }}</h2>
        <form [formGroup]="form" (ngSubmit)="submit()">
          <label>Usuário</label>
          <input formControlName="username" placeholder="Usuário" required />

          <label>Senha</label>
          <input type="password" formControlName="password" placeholder="Senha" required />

          <ng-container *ngIf="mode() === 'register'">
            <label>Nome de exibição</label>
            <input formControlName="displayName" placeholder="Seu nome" required />
          </ng-container>

          <button class="btn" type="submit" [disabled]="form.invalid">
            {{ mode() === 'login' ? 'Entrar' : 'Registrar' }}
          </button>
        </form>

        <p style="margin-top: 1rem; text-align: center;">
          <a href="#" (click)="toggleMode($event)">
            {{ mode() === 'login' ? 'Criar uma conta' : 'Já tenho conta' }}
          </a>
        </p>
      </div>
    </div>
  `
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly mode = signal<'login' | 'register'>('login');

  readonly form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    displayName: ['']
  });

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    const { username, password, displayName } = this.form.value;
    const obs =
      this.mode() === 'login'
        ? this.auth.login(username!, password!)
        : this.auth.register(username!, password!, displayName ?? username!);
    obs.subscribe({
      next: () => this.router.navigate(['/today'])
    });
  }

  toggleMode(event: Event): void {
    event.preventDefault();
    if (this.mode() === 'login') {
      this.mode.set('register');
      this.form.get('displayName')?.setValidators([Validators.required]);
    } else {
      this.mode.set('login');
      this.form.get('displayName')?.clearValidators();
      this.form.get('displayName')?.setValue('');
    }
    this.form.get('displayName')?.updateValueAndValidity();
  }
}
