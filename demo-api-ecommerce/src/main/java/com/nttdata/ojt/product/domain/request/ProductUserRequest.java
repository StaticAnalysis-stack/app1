package com.nttdata.ojt.product.domain.request;

import com.nttdata.ojt.global.domain.AbstractRequest;

public class ProductUserRequest extends AbstractRequest{
	private int id;
	
	private String pCode;
	
	private String emailId;
	
	private String vCode;
	
	private int quantity;
	
	public ProductUserRequest() {}


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


	

	public ProductUserRequest(String pCode, String emailId, String vCode, int quantity) {
		this.pCode = pCode;
		this.emailId = emailId;
		this.vCode = vCode;
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
	
	public boolean isNotEmpty() {
		if(pCode==null || pCode.equals("") 
				|| emailId==null || emailId.equals("") 
				|| vCode==null || vCode.equals("")
				|| quantity==0
				)
		{
			return false;
		}
		return true;
	}
}
