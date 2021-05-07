package com.example.application.data.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.vaadin.flow.component.notification.Notification;


public class Appusers {
	List<Appuser> personList;
	
	public Appusers() {
		this.personList =  new ArrayList<>();
		updatePersonList();
	}

	public List<Appuser> getPersonList() {
		return personList;
	}

	public void updatePersonList() {
		try {
		    String url = "jdbc:postgresql://localhost:5432/PGPI";
		    String user = "postgres";
		    String password = "5766";
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("SELECT * from \"DDBB\".appuser");
            ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	        	Appuser newUser = new Appuser(rs.getString(1),rs.getString(3),rs.getString(4));
	        	this.personList.add(newUser);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		} 
	}

}