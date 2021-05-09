


package com.example.application.views.order;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.application.data.entity.Envio;
import com.example.application.data.entity.Producto;
import com.example.application.views.Mainshop.MainViewShop;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "TotalOrders", layout = MainViewShop.class)
@PageTitle("Total Orders")
@CssImport("./views/orders/totalorders-view.css")

public class TotalOrdersView extends Div {
	  
	//Inicialización parametros conexión BBDD
		String url = "jdbc:postgresql://localhost:5432/postgres";
	    String user = "postgres";
	    String pass = "1234";
		
	    
		Grid<Envio> grid = new Grid<>();
		List<Envio> totalOrders = new ArrayList<>();
		ListDataProvider<Envio> data = new ListDataProvider<>(totalOrders);
		
		public TotalOrdersView() {
			
			setId("totalorders-view");
	        addClassName("totalorders-view");
	        
	        SplitLayout splitLayout = new SplitLayout();
	        splitLayout.setSizeFull();
	        
	        createGridLayout(splitLayout);

	        add(splitLayout);
	        
	        try {
			    
	            PreparedStatement pst;
	        	Connection con = DriverManager.getConnection(url, user, pass);
				pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".\"Order\" o WHERE o.username = '" +  VaadinSession.getCurrent().getAttribute("user") + "'" );
	            ResultSet rs = pst.executeQuery();
	            
		        while (rs.next()) {
		        	Envio envio= new Envio(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6));
		        	this.totalOrders.add(envio);
		        }
		        
			} catch (SQLException e1) {
				e1.printStackTrace();
				//Notification.show(e1.getMessage());
			}
	        

	        grid.setDataProvider(data);
	        grid.addColumn(Envio::getIdOrder).setHeader("Nº Pedido").setAutoWidth(true);
	        grid.addColumn(Envio::getUsername).setHeader("Usuario").setAutoWidth(true);
	        grid.addColumn(Envio::getAddress).setHeader("Direccion").setAutoWidth(true);
	        grid.addColumn(Envio::getTrackingNumber).setHeader("Nº Seguimiento").setAutoWidth(true);
	        grid.addColumn(Envio::getPrice).setHeader("Precio").setAutoWidth(true); 
	        grid.addColumn(Envio::getState).setHeader("Estado").setAutoWidth(true); 
	        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
	        grid.setHeightFull();
	        
			
		}
		
		private void createGridLayout(SplitLayout splitLayout) {
	    	
	        Div wrapper = new Div();
	        wrapper.setId("grid-wrapper");
	        wrapper.setWidthFull();
	        splitLayout.addToPrimary(wrapper);
	        wrapper.add(grid);
	        
	    }

	    
	    

}
