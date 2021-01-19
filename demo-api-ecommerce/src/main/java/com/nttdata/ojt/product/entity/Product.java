package com.nttdata.ojt.product.entity;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Id;

@Entity
@Table(name="product")
public class Product {
	
	@Id
	@Column
	private String pCode;
	@Column
	private String pName;
	@Column 
	private String pDesc;
	@Column
	private String imageUrl;
	
	

	public Product() {}
	
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpDesc() {
		return pDesc;
	}
	public void setpDesc(String pDesc) {
		this.pDesc = pDesc;
	}
	public Product(String pCode, String pName, String pDesc) {
		super();
		this.pCode = pCode;
		this.pName = pName;
		this.pDesc = pDesc;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	
	
	
	
}