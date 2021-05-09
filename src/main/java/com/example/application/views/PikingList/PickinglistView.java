package com.example.application.views.PikingList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.PageTitle;
import com.example.application.data.entity.Cliente;
import com.example.application.data.entity.Envio;
import com.example.application.data.entity.Pedido;
import com.example.application.views.Generatepiking.Person;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;

@Route(value = "pickinglist", layout = pivkingview.class)
@PageTitle("picking-list")
@CssImport("./views/pickinglist/pickinglist-view.css")
public class PickinglistView extends Div implements BeforeEnterObserver {
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
    
	private String idorder;
	private String id_storage;
	private String idproduct;
	private int cantidad;
    
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		    	try {
        	if(!VaadinSession.getCurrent().getAttribute("role").toString().equals("ROL"))
        		event.rerouteTo(LoginView.class);
    	}catch(Exception ex) {
    		event.rerouteTo(LoginView.class);
    	}
	}

	
	//private javax.swing.JTextField num1;
	private Button done = new Button("done");
	

	
	Grid<Envio> grid = new Grid<>();
	List<Envio> pedidos = new ArrayList<>();
	ListDataProvider<Envio> data = new ListDataProvider<>(pedidos);
    
    public PickinglistView() {
    	setId("pickinglist-view");
        addClassName("pickinglist-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".Order");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Envio pedido = new Envio(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6));
	        	this.pedidos.add(pedido);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}
       
        grid.setDataProvider(data);
        
         
        
        grid.addColumn(Envio::getIdOrder).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Envio::getAddress).setHeader("Direccion").setAutoWidth(true);
        grid.addColumn(Envio::getState).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(Envio::getTrackingNumber).setHeader("Num tracking").setAutoWidth(true);
        grid.addColumn(Envio::getUsername).setHeader("User").setAutoWidth(true);
        grid.addColumn(Envio::getPrice).setHeader("Precio").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
    	done.addClickListener(e -> {
    		
    		System.out.println("ss: "+id_storage+"\t"+cantidad);
    		System.out.println("actualizando");
    		update_picking(id_storage, cantidad);
    		System.out.println("insertando");
    		insert_picking(idorder, idproduct, cantidad);
    		System.out.println("sasss");
    		//delete_picking(id_storage);
    		grid.getDataProvider().refreshAll();
		/**/
        
        });

        
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Envio selected =  event.getValue();
        	
        	//idproduct.setValue(selected.getIdProduct());
        	//cantidad.setValue(String.valueOf(selected.getQuantity()));
        	System.out.println(selected.getIdOrder());
        	
        	idorder = selected.getIdOrder();
        	
        	String storage = null;
        	int cant_order = 0;
        	try {
	            PreparedStatement pst;
	        	Connection con = DriverManager.getConnection(url, user, pass);
				pst = con.prepareStatement("select a.\"idstorage\" from \"NEWDDBB1\".orderproduct a where a.\"idorder\"  = '"+idorder+"'");
	            ResultSet rs = pst.executeQuery();
		        while (rs.next()) {
		        	storage = rs.getString(1);
		        }
		        pst = con.prepareStatement("select a.\"quantity\" from \"NEWDDBB1\".orderproduct a where a.\"idorder\"  = '"+idorder+"'");
	            ResultSet rs2 = pst.executeQuery();
		        while (rs2.next()) {
		        	cant_order = rs2.getInt(1);
		        }
		        System.out.println(storage+"\t"+cant_order);
		        
			} catch (SQLException e1) {
				Notification.show(e1.getMessage());
			}
        	cantidad = cant_order;
        	id_storage = storage;
        }); 
    }
    
    
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv); 
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        done.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(done);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }
    
    public long update_picking(String idstorage, int quantity) {
    	long id = 0;
    	String SQL_update="UPDATE \"NEWDDBB1\".\"storage\" SET quantity = quantity - ? WHERE idstorage = ?";
    	
    	try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL_update,
                Statement.RETURN_GENERATED_KEYS)) {
    		
    		pstmt.setInt(1, quantity);
            pstmt.setString(2, idstorage);
            

            pstmt.executeUpdate();
            System.out.println("sss");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    	
    	return id;
    	
    }
    public int delete_picking(String id) {
    	System.out.println("sdf");
        String SQL = "DELETE FROM \"NEWDDBB1\".\"orderproduct\" WHERE idorder = ?";

        int affectedrows = 0;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        String SQL2 = "DELETE FROM \"NEWDDBB1\".\"order\" WHERE idorder = ?";

        int affectedrows2 = 0;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL2)) {

            pstmt.setString(1, id);

            affectedrows2 = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
    
    public long insert_picking(String order, String product, int quantity) {
    	
    	String SQL = "INSERT INTO \"NEWDDBB1\".orderhistory" + "VALUES (?,?,?,?)";
    	System.out.println(java.time.LocalDate.now());
        long id = 0;
        Date d= Date.valueOf(java.time.LocalDate.now());
		//Date.valueOf(d);
        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {
    		
            pstmt.setString(1, order);
            pstmt.setString(2, product);
            pstmt.setInt(3, quantity);
            pstmt.setDate(4, d);
            
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows 
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("wss");
        return id;
    }
    
    
}
