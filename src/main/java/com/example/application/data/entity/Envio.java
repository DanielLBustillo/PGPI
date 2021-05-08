package com.example.application.data.entity;

public class Envio {
	
	private String idOrder;
    private String username;
    private String address;
    private String trackingNumber;
    private int price;
    private String state;
    
    
    
    
    public Envio(String idOrder, String username, String address, String trackingNumber, int price, String state) {
		super();
		this.idOrder = idOrder;
		this.username = username;
		this.address = address;
		this.trackingNumber = trackingNumber;
		this.price = price;
		this.state = state;
	}
	public String getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
