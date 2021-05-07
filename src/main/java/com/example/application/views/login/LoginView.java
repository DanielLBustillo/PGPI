package com.example.application.views.login;

import com.example.application.views.admin.MainViewAdmin;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.dependency.CssImport;

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
@CssImport("./views/login/login-view.css")
public class LoginView extends HorizontalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField name;
    private Button sayHello;
     
    String url = "jdbc:postgresql://localhost:5432/PGPI";
    String user = "postgres";
    String password = "5766";

    public LoginView() {
        addClassName("login-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
			try {
	            PreparedStatement pst;
	        	Connection con = DriverManager.getConnection(url, user, password);
				pst = con.prepareStatement("SELECT * from \"DDBB\".appuser");
	            ResultSet rs = pst.executeQuery();

		        while (rs.next()) {
		        
		        	Notification.show(rs.getString(1));
		        }
			} catch (SQLException e1) {
				e1.printStackTrace();
				Notification.show(e1.getMessage());
			}
        });
    }
}
