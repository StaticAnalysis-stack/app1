package com.nttdata.ojt.product.variant.domain.request;

import com.nttdata.ojt.global.domain.AbstractRequest;

public class VariantProductRequest extends AbstractRequest {

	private String pCode;
	private String vCode;
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public VariantProductRequest(String locale, String pCode, String vCode) {
		super(locale);
		this.pCode = pCode;
		this.vCode = vCode;
	}
	
	public VariantProductRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VariantProductRequest(String locale) {
		super(locale);
		// TODO Auto-generated constructor stub
	}

	public boolean isNotEmpty() {
		if(pCode==null || pCode.equals("") 
				|| vCode==null || vCode.equals("") 
				
				)
		{
			return false;
		}
		return true;
	}
	
	
}
