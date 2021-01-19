import { Component, NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { RegisteredProductListComponent } from './registered-product-list/registered-product-list.component';
import { RegisteredUserListComponent } from './registered-user-list/registered-user-list.component';
import { RegistrationComponent } from './registration/registration.component';
import { UserProductOrdersComponent } from './user-product-orders/user-product-orders.component';

const routes: Routes = [
  {
    path:'',
    component:HomeComponent
  },
  
  {
    path:'home',
    component:HomeComponent
  },

{
  path:'user/login',
  component:LoginComponent
},

{
  path:'user/register',
  component:RegistrationComponent
},

{
  path:'user/login/success',
  component:NavbarComponent
},

{
  path:'register_user_list',
  component:RegisteredUserListComponent,
  canActivate:[AuthGuard]
},

{
  path:'register_product_list',
  component:RegisteredProductListComponent,
  canActivate:[AuthGuard]
},

{
  path:'myOrders',
  component:UserProductOrdersComponent,
  canActivate:[AuthGuard]
},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
