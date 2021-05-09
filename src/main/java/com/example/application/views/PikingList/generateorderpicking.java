package com.example.application.views.PikingList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.example.application.data.entity.Envio;
import com.example.application.data.entity.Pedido;
import com.example.application.views.Generatepiking.Person;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;

@Route(value = "Entrantes", layout = pivkingview.class)
@PageTitle("Entrantes")
@CssImport("./views/pickinglist/pickinglist-view.css")
public class generateorderpicking extends Div implements BeforeEnterObserver {
	
    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "pgpi";
    
	private String idproduct;
	private String idorde;
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
	

	
	Grid<Pedido> grid = new Grid<>();
	List<Pedido> pedidos = new ArrayList<>();
	ListDataProvider<Pedido> data = new ListDataProvider<>(pedidos);
    
    public generateorderpicking() {
    	setId("pickinglist-view"); 
        addClassName("pickinglist-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".incomig");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Pedido pedido = new Pedido(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),rs.getDate(6).toLocalDate(),rs.getString(7));
	        	this.pedidos.add(pedido);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}
        
        grid.setDataProvider(data);
        
         
        
        grid.addColumn(Pedido::getIdIncoming).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Pedido::getPackageInfo).setHeader("Info").setAutoWidth(true);
        grid.addColumn(Pedido::getIdProduct).setHeader("ID Producto").setAutoWidth(true);
        grid.addColumn(Pedido::getQuantity).setHeader("Cantidad").setAutoWidth(true);
        grid.addColumn(Pedido::getDateOrder).setHeader("Fecha Pedido").setAutoWidth(true);
        grid.addColumn(Pedido::getDateReceive).setHeader("Fecha Recibido").setAutoWidth(true);
        grid.addColumn(Pedido::getProviderCif).setHeader("CIF").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
    	done.addClickListener(e -> {
    		
    		
    		
    		//String a = String.valueOf(cantidad.getValue());
    		update(idorde,cantidad);
    		delete(idproduct);	
    		
    		grid.getDataProvider().refreshAll();
		/**/
        
        });

        
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Pedido selected =  event.getValue();
        	
        	//idproduct.setValue(selected.getIdProduct());
        	//cantidad.setValue(String.valueOf(selected.getQuantity()));
        	System.out.println(selected.getIdProduct());
        	
        	idproduct = selected.getIdIncoming();
        	cantidad = selected.getQuantity();
        	
        	
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
    
    public long update(String idproduct, int quantity) {
    	long id = 0;
    	String SQL_update="UPDATE \"NEWDDBB1\".storage SET quantity = ?+quantity WHERE idincomig = ?";
    	
    	try (Connection conn =  DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(SQL_update,
                Statement.RETURN_GENERATED_KEYS)) {
    		
    		pstmt.setInt(1, quantity);
            pstmt.setString(2, idproduct);
            

            pstmt.executeUpdate();
            System.out.println("sss");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    	
    	return id;
    	
    }
    public int delete(String id) {
        String SQL = "DELETE FROM \"NEWDDBB1\".incomig WHERE idincomig = ?";

        int affectedrows = 0;

        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

}