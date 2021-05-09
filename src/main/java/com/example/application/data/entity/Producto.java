package com.example.application.data.entity;

public class Producto {
	
	private String idProduct;
    private String name;
    private int price;
    private int minQuantity;
    private int orderQuantity;
    private String description;
    private String providerCif;
    private int stock;
    

	public Producto(String idProduct, String name, int price, int minQuantity, int orderQuantity, String description,
			String providerCif, int stock) {
		super();
		this.idProduct = idProduct;
		this.name = name;
		this.price = price;
		this.minQuantity = minQuantity;
		this.orderQuantity = orderQuantity;
		this.description = description;
		this.providerCif = providerCif;
		this.stock=stock;
	}
    
    
	public String getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(int minQuantity) {
		this.minQuantity = minQuantity;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProviderCif() {
		return providerCif;
	}
	public void setProviderCif(String providerCif) {
		this.providerCif = providerCif;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

}
