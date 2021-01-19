package com.nttdata.ojt.product.variant.domain.response;

import com.nttdata.ojt.global.domain.AbstractResponse;

public class VariantStockGetResponse extends AbstractResponse{
	
	private int vStock;

	public int getvStock() {
		return vStock;
	}

	public void setvStock(int vStock) {
		this.vStock = vStock;
	}
	
	

}
