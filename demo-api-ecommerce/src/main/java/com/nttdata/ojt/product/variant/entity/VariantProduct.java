package com.nttdata.ojt.product.variant.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name="variantProduct")
@IdClass(VariantProduct.class)
public class VariantProduct implements Serializable {
	//

	@Column
	private String pCode;	

	@Id
	@Column
	private String vCode;

	public VariantProduct() {}
	
	
		
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



		@Override
	public boolean equals(Object obj) {
		VariantProduct variant = (VariantProduct)obj;
		return variant.getpCode().equals(this.getpCode()) && variant.getvCode().equals(this.getvCode());
	}
	
	
}
