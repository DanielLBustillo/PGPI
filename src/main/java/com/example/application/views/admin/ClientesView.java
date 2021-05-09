package com.example.application.views.admin;

import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.login.LoginView;
import com.example.application.data.entity.Cliente;

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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

@Route(value = "clientes", layout = MainViewAdmin.class)
@PageTitle("Clientes")
@CssImport("./views/clientes/clientes-view.css")

public class ClientesView extends Div implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	try {
        	if(!VaadinSession.getCurrent().getAttribute("role").toString().equals("ADMIN"))
        		event.rerouteTo(LoginView.class);
    	}catch(Exception ex) {
    		event.rerouteTo(LoginView.class);
    	}
        }
	
	private TextField userName;
	private TextField password;
	private TextField name;
	private TextField surname;
	private TextField telephone;
	private TextField address;
	

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
	
	Grid<Cliente> grid = new Grid<>();
	List<Cliente> clientes = new ArrayList<>();
	ListDataProvider<Cliente> data = new ListDataProvider<>(clientes);
	

    public ClientesView() {
    	setId("clientes-view");
        addClassName("clientes-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".clients");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Cliente cliente = new Cliente(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
	        	this.clientes.add(cliente);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}
        
        

        grid.setDataProvider(data);
        
        
        grid.addColumn(Cliente::getUserName).setHeader("Usuario").setAutoWidth(true);
        grid.addColumn(Cliente::getPassword).setHeader("Contraseña").setAutoWidth(true);
        grid.addColumn(Cliente::getName).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Cliente::getSurname).setHeader("Apellido").setAutoWidth(true);
        grid.addColumn(Cliente::getTelephone).setHeader("Telefono").setAutoWidth(true);
        grid.addColumn(Cliente::getAddress).setHeader("Direccion").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
        cancel.addClickListener(e -> {
        	
        	userName.clear();
        	password.clear();
        	name.clear();
        	surname.clear();
        	telephone.clear();
        	address.clear();
        
        });
        
        save.addClickListener(e -> {
        	
        	Cliente c = new Cliente(userName.getValue(),
        			password.getValue(),
        			name.getValue(),
        			surname.getValue(),
        			telephone.getValue(),
        			address.getValue());
        	//num1.setText("");
        	delete(userName.getValue());
        	userName.clear();
        	password.clear();
        	name.clear();
        	surname.clear();
        	telephone.clear();
        	address.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	
        	clientes.removeIf(cliente -> cliente.getUserName()==c.getUserName());
        	insert(c);
            clientes.add(c);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(userName.getValue());
        	clientes.removeIf(cliente -> cliente.getUserName()==userName.getValue());
        	userName.clear();
        	password.clear();
        	name.clear();
        	surname.clear();
        	telephone.clear();
        	address.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Cliente selected =  event.getValue();
        	userName.setValue(selected.getUserName());
        	password.setValue(selected.getPassword());
        	name.setValue(selected.getName());
        	surname.setValue(selected.getSurname());
        	telephone.setValue(selected.getTelephone());
        	address.setValue(selected.getAddress());
        	
        	
        });
        
    }
    
    public long insert(Cliente value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".clients(userName, \"Password\", \"Name\", surname, telephone, address) "
                + "VALUES (?,?,?,?,?,?)";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, value.getUserName());
            pstmt.setString(2, value.getPassword());
            pstmt.setString(3, value.getName());
            pstmt.setString(4, value.getSurname());
            pstmt.setString(5, value.getTelephone());
            pstmt.setString(6, value.getAddress());

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
        String SQL = "DELETE FROM \"NEWDDBB1\".clients WHERE username = ?";

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
        
        userName = new TextField("Usuario");
        password = new TextField("Contraseña");
        name = new TextField("Nombre");
        surname = new TextField("Apellido");
        telephone = new TextField("Telefono");
        address = new TextField("Direccion");
        Component[] fields = new Component[]{userName, password, name, surname, telephone, address};

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
