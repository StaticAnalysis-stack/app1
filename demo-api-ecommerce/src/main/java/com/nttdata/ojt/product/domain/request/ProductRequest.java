package com.nttdata.ojt.product.domain.request;

import java.util.UUID;

import com.nttdata.ojt.global.domain.AbstractRequest;

public class ProductRequest extends AbstractRequest{
	private String pCode;
	private String pName;
	private String pDesc;
	private String imageUrl = "";
	public ProductRequest(String pCode, String pName, String pDesc) {
		super();
		this.pCode = pCode;
		this.pName = pName;
		this.pDesc = pDesc;
	}
	
	

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
	
	public boolean isNotEmpty() {
		if(pName==null || pName.equals("") || imageUrl.isEmpty() ) {
			return false;
		}
		return true;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public ProductRequest() {
		super();
	}



	public ProductRequest(String locale) {
		super(locale);
	}
}
