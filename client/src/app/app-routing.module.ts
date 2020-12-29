import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home';
import { LoginComponent } from './shared/component/pages/login';
import { AdminComponent } from './admin';
import { AdminGuard, GuestGuard, LoginGuard } from './guard';
import { ChangePasswordComponent } from './shared/component/pages/change-password';

import { AdminPharmacyPanelComponent } from './admin/admin-pharmacy-panel/admin-pharmacy-panel.component';
import { ForbiddenComponent } from './shared/component/pages/forbidden';
import { NotFoundComponent } from './shared/component/pages/not-found';
import { SignupComponent } from './shared/component/pages/signup';
import { AdminDiscountsPanelComponent } from './admin/admin-discounts-panel/admin-discounts-panel.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [GuestGuard],
    pathMatch: 'full'
  },
  {
    path: 'admin-pharmacy-panel',
    component: AdminPharmacyPanelComponent

  },

  {
    path: 'login',
    component: LoginComponent,
    canActivate: [GuestGuard]
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard]
  },
  {
    path: '404',
    component: NotFoundComponent
  },
  {
    path: '403',
    component: ForbiddenComponent
  }
  ,{
    path:'discounts',
    component: AdminDiscountsPanelComponent
  }
  ,
  {
    path: 'orders',
    loadChildren: './orders/orders.module#OrdersModule',
  },
  {
    path: '**',
    redirectTo: '/404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
