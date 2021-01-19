package com.example.medicinal.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public class Medicine {
	private int no;
	@Size(min = 3, max=100, message="Brand name should have atleast three characters !")  
	private String brand_name;
	
	@Size(min = 3,max=100, message="Company name should have atleast three characters !")  
	private String company_name;
	
	private String expiry;
	
	@Size(min=3,max=20,message="Invalid Batch Number")
	private String batch_no;
	
	private String product_type;
	
	@Min(value=10,message="The product should be of 10Rs atleast")
	private double amount;
	
	public Medicine(){}
	
	public Medicine(String brand_name, String company_name, String expiry, String batch_no, String product_type,
			double amount) {
		
		super();
		this.brand_name = brand_name;
		this.company_name = company_name;
		this.expiry = expiry;
		this.batch_no = batch_no;
		this.product_type = product_type;
		this.amount = amount;
	}
	public Medicine(int no, String brand_name, String company_name, String expiry, String batch_no, String product_type,
			double amount) {
		super();
		this.no=no;
		this.brand_name = brand_name;
		this.company_name = company_name;
		this.expiry = expiry;
		this.batch_no = batch_no;
		this.product_type = product_type;
		this.amount = amount;
	}
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
