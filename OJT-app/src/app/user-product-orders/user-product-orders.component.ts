import { Component, OnInit } from '@angular/core';
import { ProductList } from '../product-list';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-user-product-orders',
  templateUrl: './user-product-orders.component.html',
  styleUrls: ['./user-product-orders.component.css']
})
export class UserProductOrdersComponent implements OnInit {

  constructor(private _productServ:ProductService,private _userServ:UserService) { }

  userInfo:any;

  myOrders:ProductList[];

  isBool=false;
  searchTerm: string = "";
  p: number = 1;


  ngOnInit(): void {
    this.getOrders();
  }

  getOrders()
  {
    this.userInfo=this._userServ.returnLogInInfo();
    console.log(this.userInfo.emailid);
    this._productServ.getOrdersFromRemote(this.userInfo.emailid).subscribe(
      data=>{
        console.log(data);
        this.myOrders=data.products;
        this.isBool=true;
      },
      error=>console.log(error)
    )
  }

  trackByFunction(item, index) {
    return index;
  }

}
