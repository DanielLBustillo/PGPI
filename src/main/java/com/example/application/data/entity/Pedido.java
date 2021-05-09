package com.example.application.data.entity;

import java.time.LocalDate;

public class Pedido {
	
	private String idIncoming;
    private String providerCif;
    private String idProduct;
    private int quantity;
    private LocalDate dateReceive;
    private LocalDate dateOrder;
    private String packageInfo;
    
    
    
	public Pedido(String idIncoming, String providerCif, String idProduct, int quantity, LocalDate dateReceive,
			LocalDate dateOrder, String packageInfo) {
		super();
		this.idIncoming = idIncoming;
		this.providerCif = providerCif;
		this.idProduct = idProduct;
		this.quantity = quantity;
		this.dateReceive = dateReceive;
		this.dateOrder = dateOrder;
		this.packageInfo = packageInfo;
	}
	
	
	public String getIdIncoming() {
		return idIncoming;
	}
	public void setIdIncoming(String idIncoming) {
		this.idIncoming = idIncoming;
	}
	public String getProviderCif() {
		return providerCif;
	}
	public void setProviderCif(String providerCif) {
		this.providerCif = providerCif;
	}
	public String getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDate getDateReceive() {
		return dateReceive;
	}
	public void setDateReceive(LocalDate dateReceive) {
		this.dateReceive = dateReceive;
	}
	public LocalDate getDateOrder() {
		return dateOrder;
	}
	public void setDateOrder(LocalDate dateOrder) {
		this.dateOrder = dateOrder;
	}
	public String getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
	}

}
