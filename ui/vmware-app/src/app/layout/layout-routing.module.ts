import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainLayoutComponent } from './main-layout/main-layout.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: MainLayoutComponent,
    // canActivate: [AuthGuard],
    children: [
      { path: '', loadChildren: () => import('../home/home.module').then(m => m.HomeModule) },
      { path: 'about', loadChildren: () => import('../about/about.module').then(m => m.AboutModule) }
    ]
  },
  {
    path: 'login', loadChildren: () => import('../login/login.module').then(m => m.LoginModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LayoutRoutingModule { }
