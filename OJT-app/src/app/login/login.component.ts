
import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms'
import { Router } from '@angular/router';
import { User } from '../user';
import { UserService } from '../user.service';
import { ViewChild, ElementRef} from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private serv:UserService,private route:Router) { }

  user=new User();

  wrongCredentioanls:string;
  closeButton=false;

  // signInObject=null;

  modelDismiss;


  // displayresult()
  // {
  //   const var_jsonString=JSON.stringify(this.signInObject);
  //   console.log(typeof(var_jsonString));
  //   console.log(var_jsonString);
  //   const var_jsonParse=JSON.parse(var_jsonString);
  //   console.log(typeof(var_jsonParse));
  //   console.log(var_jsonParse);

  //   if(var_jsonParse["completelyServed"]==true)
  //   {
  //     alert("Successfully signed in");
  //   }


 // }

 @ViewChild('closeLoginModal') closeLoginModal: ElementRef;

  loginUser()
  {
    this.serv.loginUserFromRemote(this.user).subscribe(
      data=>
      {
        console.log("inside data")
            console.log(data);
            localStorage.setItem('token',data.token);
             this.closeLoginModal.nativeElement.click();
            // console.log(this.user.emailid);
            this.serv.logInInfo(this.user);
            this.route.navigate(['']);
            
      },
     error=>{
      console.log(error);
      if(error instanceof HttpErrorResponse)
      {
        if(error.status==401)
        {
           
           this.wrongCredentioanls="Email Id or Password is Wrong";
           console.log(this.wrongCredentioanls);
        }
      }
    }
    )
    
    
    //this.displayresult();
    
  }
  ngOnInit(): void {
  
  }



}


   // console.log("response received");
      // console.log(data);

      // if(data["completelyServed"]==true)
      // {
      //   alert("Successfully logged In");
      
      // }
      // this.closeLoginModal.nativeElement.click();
      // this.route.navigate(['/user/login/success'])