package com.nttdata.ojt.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_user")
public class ProductUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String pCode;
	
	@Column 
	private String emailId;
	
	@Column
	private String vCode;
	
	@Column
	private int quantity;
	
	public ProductUser() {}


	public String getpCode() {
		return pCode;
	}


	public void setpCode(String pCode) {
		this.pCode = pCode;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	

	public ProductUser(String pCode, String emailId, String variantCode, int quantity) {
		super();
		this.pCode = pCode;
		this.emailId = emailId;
		this.vCode = variantCode;
		this.quantity = quantity;
	}


	


	public String getvCode() {
		return vCode;
	}


	public void setvCode(String vCode) {
		this.vCode = vCode;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getId() {return id;}
	
}
