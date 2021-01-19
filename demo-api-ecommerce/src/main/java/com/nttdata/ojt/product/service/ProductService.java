package com.nttdata.ojt.product.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.ojt.product.entity.Product;
import com.nttdata.ojt.product.entity.ProductUser;
import com.nttdata.ojt.product.entity.ProductVariant;
import com.nttdata.ojt.product.exceptions.ProductAlreadyPresentException;
import com.nttdata.ojt.product.exceptions.ProductVariantAlreadyPresentException;
import com.nttdata.ojt.product.exceptions.ProductVariantNotPresentException;
import com.nttdata.ojt.product.repositories.ProductRepository;
import com.nttdata.ojt.product.repositories.ProductUserRepository;
import com.nttdata.ojt.product.repositories.ProductVariantRepository;


@Service
public class ProductService {
	 
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductUserRepository productUserRepo;
	
	@Autowired
	private ProductVariantRepository productVariantRepo;
	
	
	
	
	public boolean addProduct(Product product) throws ProductAlreadyPresentException{
		/**
		 * will add product and throw exception if it is aleady present in the database table
		*/
		//to check if the product is in the db already
		if(isProductPresent(product.getpCode()))  throw new ProductAlreadyPresentException();
		System.out.println(product.getpCode());
		Product p = this.productRepo.save(product);
		System.out.println(p.getpCode());
		//to check if the product was successfully added
		return isProductPresent(p.getpCode());
	}
	

	public boolean isProductPresent(String pCode) {
		return productRepo.findById(pCode).isPresent();
	}
	
	public List<Product> getAllProducts(){
		/**
		 * returns a list of all the products
		*/
		List<Product> products = new ArrayList<>();
		this.productRepo.findAll().forEach(products::add);
		products.forEach((Product a)->{
			
		});
		return products;
	}
	
	private Product getProductByCode(String code) {
		Optional<Product> prod = this.productRepo.findById(code);
		if( prod.isPresent() ) {
			return prod.get();
		}else {
			return null;
		}
		
	}
	
	public boolean assignProductToUser(ProductUser productUser) {
		/**
		 * code for checking the user service have recorded 
		 * the same in the respective database will go here
		 * if it have failed to do so we will return false
		 */
		
		if(isProductPresent(productUser.getpCode()))
			productUserRepo.save(productUser);
		return isProductUserPresent(productUser);
	}
	
	public boolean isProductUserPresent(ProductUser productUser) {
		return productRepo.findById(productUser.getpCode()).isPresent();
	}

	
	public List<Product> getProductsByEmailId(String emailId){
		/**
		 * returns the list of all the products
		 * that a user has bought i.e. is assigned
		 * to the email
		*/
		List<Product> products = new ArrayList<>();
		this.productUserRepo.findAll().forEach(productUser->{
			if(productUser.getEmailId().equals(emailId)) {
				Product prod = this.getProductByCode(productUser.getpCode());
				if(prod!=null)
				{
					products.add(prod);
				}
			}
		});
		return products;
	}
	
	public boolean saveProductVariant(ProductVariant pVariant) throws ProductVariantAlreadyPresentException {
		/**
		 * saves the product variant otherwise throws an exception if it is already present
		*/
		if(isProductVariantPresent(pVariant)) throw new ProductVariantAlreadyPresentException();
		if(getProductByCode(pVariant.getpCode())==null) {
			return false;
		}
		productVariantRepo.save(pVariant);
		return isProductVariantPresent(pVariant);
	}
	
	public boolean isProductVariantPresent(ProductVariant pVariant) {
		/**
		 * checks if the product variant is present already
		*/
		List<ProductVariant> variants = new ArrayList<>();
		this.productVariantRepo.findAll().forEach(variants::add);
		return variants.stream().filter((ProductVariant pr)->
			pr.equals(pVariant)
			).toArray().length>0;
	}
	
	public boolean deleteProductVariant(ProductVariant pVariant) throws ProductVariantNotPresentException {
		/**
		 * deletes the product variant otherwise throws an exception if it is already absent
		*/
		if(isProductVariantPresent(pVariant)) {
			this.productVariantRepo.delete(pVariant);
			return !this.isProductVariantPresent(pVariant);
		}
		else {
			throw new ProductVariantNotPresentException();
		}
		
	}
	
	
	
	
}
