package com.nttdata.ojt.product.variant.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nttdata.ojt.product.variant.entity.Variant;
import com.nttdata.ojt.product.variant.entity.VariantProduct;

public interface VariantProductRepository extends CrudRepository<VariantProduct,VariantProduct> {

	public Iterable<VariantProduct> findBypCode(String pCode);

}

