import { Routes } from '@angular/router';
import { noAuthGuard } from './utils/noauth.guard';
import { authGuard } from './utils/auth.guard';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login.component').then(c => c.LoginComponent),
    canActivate: [noAuthGuard]
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register.component').then(c => c.RegisterComponent),
    canActivate: [noAuthGuard]
  },
  {
    path: '',
    loadComponent: () => import('./components/home/home.component').then(c => HomeComponent),
    canActivate: [authGuard]
  },
  {
    path: 'books/new',
    loadComponent: () =>
      import('./components/book-form/book-form.component').then(c => c.BookFormComponent),
    canActivate: [authGuard]
  },
  {
    path: 'books/:id',
    loadComponent: () =>
      import('./components/book-form/book-form.component').then(c => c.BookFormComponent),
    canActivate: [authGuard]
  },
  {
    path: 'categories',
    loadComponent: () =>
      import('./components/categories/categories.component').then(c => c.CategoriesComponent),
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
