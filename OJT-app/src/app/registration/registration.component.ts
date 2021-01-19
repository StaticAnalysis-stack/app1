import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms'
import { FormsModule } from '@angular/forms';
import { User } from '../user';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { ViewChild, ElementRef} from '@angular/core';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  constructor(private serv:UserService,private route:Router) { }

  user=new User()


  @ViewChild('closeRegistrationModal') closeRegistrationModal: ElementRef;
  
 

  
  ngOnInit(): void {

  }




  userRegistration()
  {
    console.log("inside userregistration");
    this.serv.registerUserFromRemote(this.user).subscribe(
      data=>
      {
        console.log(data);
       if(data.hasError==false && data.completelyServed==true)
       {
        alert("Registration Successful");
        this.closeRegistrationModal.nativeElement.click();
        this.route.navigate([''])
       }
        
      },
      error=>console.log(error)
    )
  }
}
