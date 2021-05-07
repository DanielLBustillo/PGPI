package com.example.application.data.entity;


public class Appuser {
	String id ;
	String rol ;
	String userinf ;
	
	public Appuser(String id, String rol, String userinf) {
		super();
		this.id = id;
		this.rol = rol;
		this.userinf = userinf;
	}
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id  = id;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getUserinf() {
		return userinf;
	}
	public void setUserinf(String userinf) {
		this.userinf = userinf;
	}
	
	

}
