import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserList } from '../user-list';
import { UserService } from '../user.service';
import {Router} from '@angular/router'
@Component({
  selector: 'app-registered-user-list',
  templateUrl: './registered-user-list.component.html',
  styleUrls: ['./registered-user-list.component.css']
})
export class RegisteredUserListComponent implements OnInit {

  constructor(private serv:UserService,private router:Router) { }

  isBool=false;
  searchTerm:string="";
  p:number=1;

  users:UserList[];

 

  ngOnInit(){
   this.getAllUsers();
  }

  

  getAllUsers()
  {
    this.serv.getAllUsersFromRemote().subscribe(
      data=>
      {
        console.log(data);
        // data.users=this.users;
        console.log(data.users);
        console.log(typeof(data.users));
        this.users=data.users;
        console.log(this.users);
        this.isBool=true;

      },
      error=>{
        console.log(error);
        // if(error instanceof HttpErrorResponse)
        // {
        //   if(error.status==401)
        //   {
        //      this.router.navigate(['']);
        //   }
        // }
      }
     )
  }


  trackByFunction(item,index)
  {
    return index;
  }

  

}


// =[{"id":1,"firstName":"Tushar","middleName":"Ashok","lastName":"Kharade","emailid":"tusharkharade14256@gmail.com","mobileNumber":9623968366},
//   {"id":2,"firstName":"Pratyush","middleName":"Shrawan","lastName":"Mishra","emailid":"pratyusham1999@gmail.com","mobileNumber":453672788585},
//   {"id":3,"firstName":"Shashank","middleName":"Raju","lastName":"Shende","emailid":"shashankshende8@gmail.com","mobileNumber":678678786},
//   {"id":4,"firstName":"Sushant","middleName":"Singh","lastName":"Patel","emailid":"sushant@gmail.com","mobileNumber":8327986986},
//   {"id":5,"firstName":"Sagar","middleName":"Krishna","lastName":"Deshmukh","emailid":"tusharkharade14256@gmail.com","mobileNumber":679869637},
//   {"id":6,"firstName":"Tushar","middleName":"Ashok","lastName":"Kharade","emailid":"tusharkharade14256@gmail.com","mobileNumber":5278969876}]