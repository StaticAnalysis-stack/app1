import { HttpClient, HttpHeaders,HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Buy } from './buy';
import { ProductList } from './product-list';
import { VarientEntry } from './varient-entry';

const headerOptions=
{
  headers:new HttpHeaders({'contemt-Type':'application/json'})
}


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  // productApi="http://localhost:8084/products";

  
  //For Product Entry
 
  public registerProductFromRemote(product: ProductList): Observable<any> {
    return this.http.post<any>(environment.api_url+"/products/create",product)
  }

 
// get all Products

  public getAllProductsFromRemote(): Observable<any>{
    return this.http.get<any>(environment.api_url+ "/products/getAll")
  }

   //For Variant Entry

   public registerVariantFromRemote(variant: VarientEntry): Observable<any> {
    return this.http.post<any>(environment.api_url+"/products/variant/create", variant)
  }

  //getProductVariantDetails

  public getProductVariantDetailsFromRemote(pCode:any):Observable<any>{
    console.log(pCode);
    let params1=new HttpParams().set('pCode',pCode);
    return this.http.get<any>(environment.api_url+ "/products/variant/get",{params:params1})
  }

  //Buy Products

  public buyProductFromRemote(assignProduct:Buy):Observable<any>
  {
    console.log('in buy Service');
    console.log(assignProduct);
    return this.http.post<any>(environment.api_url+"/products/assign",assignProduct)
  }

  //getOrders

  public getOrdersFromRemote(emailId:any):Observable<any>
  {
      let params2=new HttpParams().set('emailId',emailId)
      return this.http.get<any>(environment.api_url+"/products/getOrders",{params:params2})
  }
}
