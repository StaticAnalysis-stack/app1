import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from './user.service';
// import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  
  constructor(private _authService: UserService,
    private _router:Router)
    {

    }
 canActivate():boolean{
  if (this._authService.loggedIn()) 
  {
    return true;
  }
  else
  {
    alert("Login is Required for this Option");
    this._router.navigate(['']);
    return false;
  }
 }

   
}
  