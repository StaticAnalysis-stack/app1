package com.nttdata.ojt.product.variant.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.ojt.product.variant.entity.Variant;
import com.nttdata.ojt.product.variant.entity.VariantProduct;
public interface VariantRepository extends CrudRepository<Variant,String>{
	


}
