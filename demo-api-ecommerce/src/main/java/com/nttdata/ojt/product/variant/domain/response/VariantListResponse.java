package com.nttdata.ojt.product.variant.domain.response;

import java.util.Date;
import java.util.List;

import com.nttdata.ojt.global.domain.AbstractResponse;
import com.nttdata.ojt.product.variant.entity.Variant;

public class VariantListResponse extends AbstractResponse{
	List<Variant> variants = null;
	Date timeRefreshed = null;

	public Date getTimeRefreshed() {
		return timeRefreshed;
	}

	public void setTimeRefreshed(Date timeRefreshed) {
		this.timeRefreshed = timeRefreshed;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

}
