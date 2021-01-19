package com.nttdata.ojt.product.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="productVariant")
@IdClass(ProductVariant.class)
public class ProductVariant implements Serializable {
	@Id
	@Column
	private String pCode;	

	@Id
	@Column
	private String vCode;

	public ProductVariant() {}
	
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
		 if (obj == null)
			    return false;

		 if (this.getClass() != obj.getClass())
			    return false;
		 
		ProductVariant variant = (ProductVariant)obj;
		return variant.getpCode().equals(this.getpCode()) && variant.getvCode().equals(this.getvCode());
	}
	
	
}
