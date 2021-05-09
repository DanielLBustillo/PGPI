package com.example.application.views.login;

import com.example.application.views.MainAdmin.MainViewAdmin;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "Login")
@RouteAlias(value = "")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField name;
	private TextField pass;
    private Button sayHello;
     
    String url = "jdbc:postgresql://localhost:5432/PGPI";
    String user = "postgres";
    String password = "5766";
    

    String role = "";

    public LoginView() {
        addClassName("login-view");
        
        name = new TextField("Your name");
        pass = new TextField("Your password");
        sayHello = new Button("login");
        add(name, pass, sayHello);
        
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        sayHello.addClickListener(e -> {

			try {
		        role =  check_login(name.getValue(), pass.getValue());
	            PreparedStatement pst;
	        	Connection con = DriverManager.getConnection(url, user, password);
				pst = con.prepareStatement("select a.\"Role\" from \"DDBB\".Appuser a where a.\"iduser\"  = '"+name.getValue()+"' and a.\"Password\" = '"+pass.getValue()+"'");
	            ResultSet rs = pst.executeQuery();
		        while (rs.next()) {
		        
		        	role = rs.getString(1);
		        }
		        
		        if (role == "") {
					Notification.show("CREDENCIALES INCORRECTOS");
		        }else {
		        	VaadinSession.getCurrent().setAttribute("user", name.getValue());
		        	VaadinSession.getCurrent().setAttribute("role", role);
		        	
		        	if (role.equals("ROL")) {
		        		UI.getCurrent().navigate("Salientes");
			        }
			        
			        if (role.equals("ROL2")) {
			        	UI.getCurrent().navigate("Shop");	        
			        }
		        	if (role.equals("ADMIN")) {	
		        		UI.getCurrent().navigate("Appusers");
			        }
		        }
			} catch (SQLException e1) {
				Notification.show(e1.getMessage());
			}
        });
    }
    public String check_login( String n, String p) throws SQLException {
    	String rol ="";
    	PreparedStatement pst;
    	Connection con = DriverManager.getConnection(url, user, password);
		pst = con.prepareStatement("select a.\"Role\" from \"DDBB\".appuser a where a.\"iduser\"  = '"+ n +"' and a.\"Password\" = '"+ p +"'");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
        
        	rol = rs.getString(1);
        }
        return rol;
    }
}
