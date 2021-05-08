package com.example.application.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;


import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.data.entity.Proveedor;
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

@Route(value = "terminales", layout = MainViewAdmin.class)
@PageTitle("Proveedores")
@CssImport("./views/terminales/terminales-view.css")
public class ProveedoresView extends Div {
	
	private TextField providerCif;
	private TextField name;
	private TextField info;
	

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
	
	Grid<Proveedor> grid = new Grid<>();
	List<Proveedor> proveedores = new ArrayList<>();
	ListDataProvider<Proveedor> data = new ListDataProvider<>(proveedores);
	
	

    public ProveedoresView() {
    	setId("proveedores-view");
        addClassName("proveedores-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".provider");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Proveedor proveedor = new Proveedor(rs.getString(1),rs.getString(2),rs.getString(3));
	        	this.proveedores.add(proveedor);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}
        
        

        grid.setDataProvider(data);
        
        
        grid.addColumn(Proveedor::getProviderCif).setHeader("CIF").setAutoWidth(true);
        grid.addColumn(Proveedor::getName).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Proveedor::getInfo).setHeader("Info").setAutoWidth(true);
;
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
        cancel.addClickListener(e -> {
        	
        	providerCif.clear();
        	name.clear();
        	info.clear();
        
        });
        
        save.addClickListener(e -> {
        	
        	Proveedor p = new Proveedor(providerCif.getValue(),
        			name.getValue(),
        			info.getValue());
        	//num1.setText("");
        	delete(providerCif.getValue());
        	providerCif.clear();
        	name.clear();
        	info.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	
        	proveedores.removeIf(proveedor -> proveedor.getProviderCif()==p.getProviderCif());
        	insert(p);
        	proveedores.add(p);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(providerCif.getValue());
        	proveedores.removeIf(proveedor -> proveedor.getProviderCif()==providerCif.getValue());
        	providerCif.clear();
        	name.clear();
        	info.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Proveedor selected =  event.getValue();
        	providerCif.setValue(selected.getProviderCif());
        	name.setValue(selected.getName());
        	info.setValue(selected.getInfo());
        	
        	
        });
        
    }
    
    public long insert(Proveedor value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".provider(providercif, \"name\", info) "
                + "VALUES (?,?,?)";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, value.getProviderCif());
            pstmt.setString(2, value.getName());
            pstmt.setString(3, value.getInfo());
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
        String SQL = "DELETE FROM \"NEWDDBB1\".provider WHERE providercif = ?";

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
        
        providerCif = new TextField("CIF");
        name = new TextField("Nombre");
        info = new TextField("Info");
        Component[] fields = new Component[]{providerCif, name, info};

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