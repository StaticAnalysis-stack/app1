package com.nttdata.ojt.product.variant.entity;
import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="variant")
public class Variant  {	
	private static final long serialVersionUID = 1L;

@Id
@Column
private String vCode;
@Column
private String vDesc;
@Column
private String vColor;
@Column
private String vSize;
@Column
private int vStock;

public Variant() {
}





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





public static long getSerialversionuid() {
	return serialVersionUID;
}





public Variant(String vCode, String vDesc, String vColor, String vSize, int vStock) {
	super();
	this.vCode = vCode;
	this.vDesc = vDesc;
	this.vColor = vColor;
	this.vSize = vSize;
	this.vStock = vStock;
}


/*
@PrePersist
private void ensureId(){
    this.setVcode(UUID.randomUUID().toString());
}	
	*/
}

