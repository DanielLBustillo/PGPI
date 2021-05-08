package com.example.application.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.data.entity.Envio;
import com.vaadin.flow.router.PageTitle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

@Route(value = "envios", layout = MainViewAdmin.class)
@PageTitle("Envios")
@CssImport("./views/envios/envios-view.css")
public class EnviosView extends Div {
	
	private TextField idOrder;
	private TextField username;
	private TextField address;
	private TextField trackingNumber;
	private TextField price;
	private TextField state;
	
	
	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
	
	Grid<Envio> grid = new Grid<>();
	List<Envio> envios = new ArrayList<>();
	ListDataProvider<Envio> data = new ListDataProvider<>(envios);
	
	

    public EnviosView() {
    	setId("envios-view");
        addClassName("envios-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".\"Order\"");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Envio envio = new Envio(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6));
	        	this.envios.add(envio);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}
        
        grid.setDataProvider(data);
        
        
        grid.addColumn(Envio::getIdOrder).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Envio::getUsername).setHeader("Usuario").setAutoWidth(true);
        grid.addColumn(Envio::getAddress).setHeader("Direccion").setAutoWidth(true);
        grid.addColumn(Envio::getTrackingNumber).setHeader("Numero Seguimiento").setAutoWidth(true);
        grid.addColumn(Envio::getPrice).setHeader("Precio").setAutoWidth(true);
        grid.addColumn(Envio::getState).setHeader("Estado").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
        cancel.addClickListener(e -> {
        	
        	idOrder.clear();
        	username.clear();
        	address.clear();
        	trackingNumber.clear();
        	price.clear();
        	state.clear();

        });
        
        save.addClickListener(e -> {
        	
        	Envio s = new Envio(idOrder.getValue(),
					        			username.getValue(),
					        			address.getValue(),
					        			trackingNumber.getValue(),
					                	Integer.parseInt(price.getValue()),
					                	state.getValue());
        	//num1.setText("");
        	delete(idOrder.getValue());
        	idOrder.clear();
        	username.clear();
        	address.clear();
        	trackingNumber.clear();
        	price.clear();
        	state.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	envios.removeIf(envio -> envio.getIdOrder()==s.getIdOrder());
            envios.add(s);
            insert(s);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(idOrder.getValue());
        	envios.removeIf(envio -> envio.getIdOrder()==idOrder.getValue());
        	idOrder.clear();
        	username.clear();
        	address.clear();
        	trackingNumber.clear();
        	price.clear();
        	state.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Envio selected =  event.getValue();
        	idOrder.setValue(selected.getIdOrder());
        	username.setValue(selected.getUsername());
        	address.setValue(selected.getAddress());
        	trackingNumber.setValue(selected.getTrackingNumber());
        	price.setValue(String.valueOf(selected.getPrice()));
        	state.setValue(selected.getState());
        	
        	
        });
        
    }
    
    public long insert(Envio value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".\"Order\"(idorder, username, address, trackingnumber, price, state) "
                + "VALUES (?,?,?,?,?,?)";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, value.getIdOrder());
            pstmt.setString(2, value.getUsername());
            pstmt.setString(3, value.getAddress());
            pstmt.setString(4, value.getTrackingNumber());
            pstmt.setInt(5, value.getPrice());
            pstmt.setString(6, value.getState());

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows 
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }
    
    public int delete(String id) {
        String SQL = "DELETE FROM \"NEWDDBB1\".\"Order\" WHERE idorder = ?";

        int affectedrows = 0;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, id);

            affectedrows = pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
    
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        
        idOrder = new TextField("ID");
        username = new TextField("Usuario");
        address = new TextField("Direccion");
        trackingNumber = new TextField("Numero Seguimiento");
        price = new TextField("Precio");
        state = new TextField("Estado");
        Component[] fields = new Component[]{idOrder, username, address, trackingNumber, price, state};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

}
