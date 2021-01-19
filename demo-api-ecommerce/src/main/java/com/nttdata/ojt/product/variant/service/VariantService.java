package com.nttdata.ojt.product.variant.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.ojt.product.entity.Product;
import com.nttdata.ojt.product.exceptions.ProductAlreadyPresentException;
import com.nttdata.ojt.product.variant.entity.Variant;
import com.nttdata.ojt.product.variant.entity.VariantProduct;
import com.nttdata.ojt.product.variant.repositories.VariantProductRepository;
import com.nttdata.ojt.product.variant.repositories.VariantRepository;

/**
 * 
 * @author Sushant Patil
 *
 */
@Service
public class VariantService {

	@Autowired
	private VariantRepository variantRepository; 
	
	@Autowired 
	VariantProductRepository variantProductRepository;
	

	public boolean addVariant(Variant variant) throws ProductAlreadyPresentException{
		/**
		 * adding product to the variant table
		 */
		//Check if variant is already present
		if(isVariantPresent(variant) )  throw new ProductAlreadyPresentException();
		this.variantRepository.save(variant);
		return isVariantPresent(variant);
	}
	
	public boolean isVariantPresent(Variant variant) {
		/**
		 * Checking if the variant is already present in the variant table
		 */
		List<Variant> variants = new ArrayList<>();
		this.variantRepository.findAll().forEach(variants::add);
		return variants.stream().filter((Variant vr)->{return vr.getvCode().equals(variant.getvCode());}).toArray().length>0;

	}
	
	public boolean addVariantProduct(VariantProduct vp) throws ProductAlreadyPresentException{
		/**
		 * adding product variant to the variantProduct table
		 */
		this.variantProductRepository.save(vp);
		//Check if variant is already present
		return isVariantProductPresent(vp);
		
	}

	public boolean isVariantProductPresent(VariantProduct variant) {
		/**
		 * Checking if the product is already present
		 */
		List<VariantProduct> variants = new ArrayList<>();
		this.variantProductRepository.findAll().forEach(variants::add);
		return variants.stream().filter((VariantProduct vr)->{return vr.getpCode().equals(variant.getpCode());}).toArray().length>0;
	}
	
	public List<Variant> getAllVariants() {
		/**
		 * Retrieving all the available variants
		 */
		List<Variant> list2 = new ArrayList<>();
 		variantRepository.findAll().forEach(e -> list2.add((Variant) e));
 		return list2;
	}

	
	public List<Variant> getProductVariant( String pCode) {
		/**
		 * Retrieving the variants assigned to the given product code
		 */
		List<VariantProduct> list3 = new ArrayList<>();
		
		variantProductRepository.findBypCode(pCode).forEach(e -> list3.add( e));
		List<Variant> list4 = new ArrayList<>();
 		list3.forEach((VariantProduct f) -> {
			
			list4.add(variantRepository.findById(f.getvCode()).get()); 
		} );
		return list4;
	}

	public boolean updateVariantStock(String  vCode,int quantity) {

		Variant v = getVariant(vCode);
		
		if(v!=null) {
			int vStock = v.getvStock();
			if(vStock<quantity) {
				return false;
			}
			v.setvStock(v.getvStock()-quantity);
			variantRepository.save(v);
			return true;
		}
		return false;
	}
	
	
	public int getVariantStock(String  vCode) {

		Variant v = getVariant(vCode);
		if(v!=null) {
			return  v.getvStock();
		}
		return -1;
	}
	
	private Variant getVariant(String vCode)
	{
		Optional<Variant> var =  variantRepository.findById(vCode);
		if(var.isPresent()) {
			return var.get();
		}
		return null;
	}


	
	


}