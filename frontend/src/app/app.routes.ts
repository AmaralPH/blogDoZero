import { Routes } from '@angular/router';
import { AuthGuard } from './services/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'today' },
      {
        path: 'today',
        loadComponent: () => import('./pages/today/today.component').then((m) => m.TodayComponent)
      },
      {
        path: 'questions',
        loadComponent: () => import('./pages/questions/questions.component').then((m) => m.QuestionsComponent)
      },
      {
        path: 'history',
        loadComponent: () => import('./pages/history/history.component').then((m) => m.HistoryComponent)
      }
    ]
  },
  { path: '**', redirectTo: 'today' }
];
