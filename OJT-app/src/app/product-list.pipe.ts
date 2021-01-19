import { Pipe, PipeTransform } from '@angular/core';
import { ProductList } from './product-list';

@Pipe({
  name: 'productList'
})
export class ProductListPipe implements PipeTransform {

  transform(product:ProductList[],searchTerm:string):ProductList[]
  {
   if(!product||!searchTerm)
   {
     return product;
   }

   return product.filter(product=> 
     (product.pName.toLowerCase().indexOf(searchTerm.toLowerCase())!==-1));
 }
}
