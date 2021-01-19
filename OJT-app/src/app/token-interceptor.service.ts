import { Injectable,Injector} from '@angular/core';
import {HttpInterceptor} from '@angular/common/http'
import {UserService} from './user.service'

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  constructor(private _injector : Injector) { }



  intercept(req,next)
  {
    let authService=this._injector.get(UserService);

  
    
    let tokenizedReq=req.clone({
      
      setHeaders:{
        
        Authorization: `Bearer ${authService.getToken()}`,
        
      }
    })
    let nonTokenizedReq=req.clone({
      
      setHeaders:{
        
        // Authorization: `Bearer ${authService.getToken()}`,
        
      }
    })

    if (authService.loggedIn()) 
    {
      return next.handle(tokenizedReq);
    }
    else
    {
      return next.handle(nonTokenizedReq);
    }
   
  }
}
