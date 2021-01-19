package com.nttdata.ojt.product.domain.response;

import java.util.Date;
import java.util.List;

import com.nttdata.ojt.global.domain.AbstractResponse;
import com.nttdata.ojt.product.entity.Product;

public class ProductListResponse extends AbstractResponse{
	List<Product> products = null;
	Date timeRefreshed = null;
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Date getTimeRefreshed() {
		return timeRefreshed;
	}
	public void setTimeRefreshed(Date timeRefreshed) {
		this.timeRefreshed = timeRefreshed;
	}
	
	
	
	
}
