package com.nttdata.ojt.product.repositories;



import org.springframework.data.repository.CrudRepository;

import com.nttdata.ojt.product.entity.Product;


public interface ProductRepository extends CrudRepository<Product,String>
{  
	
}  