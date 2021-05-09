package com.example.application.data.entity;

public class Proveedor {
	
	private String providerCif;
    private String name;
    private String info;
    
	public Proveedor(String providerCif, String name, String info) {
		super();
		this.providerCif = providerCif;
		this.name = name;
		this.info = info;
	}

	public String getProviderCif() {
		return providerCif;
	}

	public void setProviderCif(String providerCif) {
		this.providerCif = providerCif;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	

}
