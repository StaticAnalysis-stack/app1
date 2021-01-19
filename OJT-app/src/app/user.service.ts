import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ProductList } from './product-list';
import { VarientEntry } from './varient-entry';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

const headerOptions=
{
  headers:new HttpHeaders({'contemt-Type':'application/json'})
}
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient , private _router:Router) { }

  // userApi="http://localhost:8084/users";

  logInUserInfo:User;

  // For user Registration

  public registerUserFromRemote(user: User): Observable<any> {
    console.log("inside user reg services");
    return this.http.post<any>(environment.api_url+"/users/create", user)
  }

  // For user Login

  public loginUserFromRemote(user: User): Observable<any> {
    console.log("env "+environment.api_url);
    return this.http.post<any>(environment.api_url+"/users/authenticate", user)
  }


  // get all users
  public getAllUsersFromRemote(): Observable<any>{
    return this.http.get<any>(environment.api_url+"/users/getAll")
  }

loggedIn()
{
  return !!localStorage.getItem('token')
}

getToken()
{
  return localStorage.getItem('token');
 
}
logOutUser()
{
  localStorage.removeItem('token');
  this._router.navigate([''])
}


logInInfo(user:User):any
{
  console.log(user);
  this.logInUserInfo=user;
}
  
returnLogInInfo()
{
  return this.logInUserInfo;
}
}
