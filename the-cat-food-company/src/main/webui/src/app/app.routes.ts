import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { authGuard } from './auth/auth.guard';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      // Protected routes
      { path: '', component: HomeComponent, pathMatch: 'full' },
    ]
  },
  // Catch-all redirect to root
  { path: '**', redirectTo: '' }
];
