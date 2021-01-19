import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { UserListPipe } from './user-list.pipe';
import {NgxPaginationModule} from 'ngx-pagination';
import { RegisteredUserListComponent } from './registered-user-list/registered-user-list.component';
import { RegisteredProductListComponent } from './registered-product-list/registered-product-list.component';
import { ProductListPipe } from './product-list.pipe'
import { AuthGuard } from './auth.guard';
import { TokenInterceptorService } from './token-interceptor.service';
import { UserService } from './user.service';
import { HomeComponent } from './home/home.component';
import { ProductService } from './product.service';
import { UserProductOrdersComponent } from './user-product-orders/user-product-orders.component';




@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegistrationComponent,
    UserListPipe,
    RegisteredUserListComponent,
    RegisteredProductListComponent,
    ProductListPipe,
    HomeComponent,
    UserProductOrdersComponent,

    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
 
  
  
  ],
  providers: [AuthGuard,UserService,ProductService,
  {
    provide:HTTP_INTERCEPTORS,
    useClass:TokenInterceptorService,
    multi:true
  }
],
  bootstrap: [AppComponent]
})
export class AppModule { }
