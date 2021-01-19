import { analyzeAndValidateNgModules } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(public _userService:UserService) { }

  navbarBrand:String="assets/ntt-data_logo.png"
  logInUserDetails:User;

  ngOnInit(): void {
  
 this.logInUserDetails=this._userService.returnLogInInfo();

//  console.log(this.logInUserDetails)
  }

  
}
