import { Component, OnInit } from '@angular/core';
import { ProductList } from '../product-list';
import { NgForm } from '@angular/forms'
import { VarientEntry } from '../varient-entry';
import { UserService } from '../user.service';
import { ProductService } from '../product.service';
import { ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import {Buy} from '../buy'

@Component({
  selector: 'app-registered-product-list',
  templateUrl: './registered-product-list.component.html',
  styleUrls: ['./registered-product-list.component.css']
})
export class RegisteredProductListComponent implements OnInit {

  constructor(private serv: ProductService, private _router:Router,private _userServ:UserService) { }

  //declaring Variables

  isBool=false;
  VariantCategory=""
  searchTerm: string = "";
  p: number = 1;
  productCode:string;
  productName: string;
  productDesp: string;
  productImg: string;
  userInfo:any;


  //****Object Creation****
  //for product entry
  product = new ProductList();

  //for variant entry
  variant = new VarientEntry();

  //for retrving all varinats
  getvariants:VarientEntry;

  //for Buying a Product
  assignProduct=new Buy();
//******************************

  
//****For Html Purpose Variables****
  productDetails:VarientEntry[];
  products: ProductList[];
//****************************** */


  getAllproducts() {
    this.serv.getAllProductsFromRemote().subscribe(
      data => {
       console.log(data);
       this.products=data.products;
       this.isBool=true;
      },
      error => console.log(error)
    )
  }


  addProduct() {
    this.serv.registerProductFromRemote(this.product).subscribe(
      data =>
      {
        if(data.hasError==false && data.completelyServed==true)
        {
        console.log(data);
        alert("Product Added Successfully");
        }

      } ,
      error => console.log(error)

    )
  }

  addVariant() {
    console.log(this.variant);
    this.serv.registerVariantFromRemote(this.variant).subscribe(
      data =>
      {
        if(data.hasError==false && data.completelyServed==true)
        {
        console.log(data);
        alert("Variant Added Successfully");
        }

      } ,
      error => console.log(error)
    )
  }




  @ViewChild('modelGetDetails') modelGetDetails: ElementRef;

  getVariantDetails(pCode, pName, pDesc, pimageUrl) {

    this.serv.getProductVariantDetailsFromRemote(pCode).subscribe(
      data => {
        // this.productDetails=data;
        console.log(data);
        console.log(data.variants);
        this.getvariants=data.variants;

      },
      error=>console.log(error)
    )
    // console.log(pCode);
    // console.log(pName);
    // console.log(pDesc);
    // console.log(pimageUrl);
    this.productName = pName;
    this.productDesp = pDesc;
    this.productImg = pimageUrl;
    this.productCode=pCode
    this.modelGetDetails.nativeElement.click();

  }

  buyProduct()
  {
    this.userInfo=this._userServ.returnLogInInfo();
    console.log(this.userInfo.emailid);
    this.assignProduct.emailId =this.userInfo.emailid;
    this.assignProduct.pCode =this.productCode;

    
    console.log(this.assignProduct);

    this.serv.buyProductFromRemote(this.assignProduct).subscribe(
      data=>{
        console.log(data);
        if(data.hasError==false && data.completelyServed==true)
        {
          alert("Product Successfully Orderd");
        }
      },
      error=>console.log(error)
    )
  }

  
  ngOnInit(): void {
    this.getAllproducts();

  }

  trackByFunction(item, index) {
    return index;
  }

}


// = [{ imageUrl: "assets/samsung_a31.jpg", pCode: "101", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "https://static.toiimg.com/photo/72975551.cms", pCode: "101", pName: "Nokia", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "102", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "101", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "103", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "104", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "101", pName: "Samsung", pDesc: "Nice Mobile" },
//   { imageUrl: "assets/samsung_a31.jpg", pCode: "101", pName: "Samsung", pDesc: "Nice Mobile" }
//   ]https://www.google.com/url?sa=i&url=https%3A%2F%2Fithemes.com%2Fpurchase%2Fmobile%2F&psig=AOvVaw2uFCsr7xLIpV1Jak0rn0Tn&ust=1606313232159000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCPDP8butm-0CFQAAAAAdAAAAABAD