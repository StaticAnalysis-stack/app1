package com.nttdata.ojt.product.variant.domain.request;

import java.util.Locale;

import javax.persistence.Column;

import com.nttdata.ojt.global.domain.AbstractRequest;

public class VariantRequest extends AbstractRequest{

	private String vCode;
	private String vDesc;
	private String vColor;
	private String vSize;
	private int vStock;
	public String pCode;
	
	
	
	
	
	public String getvCode() {
		return vCode;
	}


	public void setvCode(String vCode) {
		this.vCode = vCode;
	}


	public String getvDesc() {
		return vDesc;
	}


	public void setvDesc(String vDesc) {
		this.vDesc = vDesc;
	}


	public String getvColor() {
		return vColor;
	}


	public void setvColor(String vColor) {
		this.vColor = vColor;
	}


	public String getvSize() {
		return vSize;
	}


	public void setvSize(String vSize) {
		this.vSize = vSize;
	}


	public int getvStock() {
		return vStock;
	}


	public void setvStock(int vStock) {
		this.vStock = vStock;
	}


	public String getpCode() {
		return pCode;
	}


	public void setpCode(String pCode) {
		this.pCode = pCode;
	}


	public boolean isNotEmpty() {
		if(vCode==null || vCode.equals("") || vSize==null || vSize.equals("")) {
			return false;
		}
		return true;
	}
	
	
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
