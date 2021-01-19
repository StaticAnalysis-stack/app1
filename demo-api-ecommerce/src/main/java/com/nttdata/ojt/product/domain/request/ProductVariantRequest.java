package com.nttdata.ojt.product.domain.request;

import javax.persistence.Column;
import javax.persistence.Id;

import com.nttdata.ojt.global.domain.AbstractRequest;
import com.nttdata.ojt.product.entity.ProductVariant;

public class ProductVariantRequest extends AbstractRequest{

	private String pCode;
	private String vCode;

	public ProductVariantRequest() {
	}

	public ProductVariantRequest(String pCode, String variantCode) {

		this.pCode = pCode;
		this.vCode = variantCode;
	}

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

	public boolean isNotEmpty() {
		if (pCode == null || pCode.equals("") || vCode == null || vCode.equals("")) {
			return false;
		}
		return true;
	}

}
