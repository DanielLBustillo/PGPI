package com.example.application.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.login.LoginView;
import com.example.application.data.entity.Pedido;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

@Route(value = "pedidos", layout = MainViewAdmin.class)
@PageTitle("Pedidos")
@CssImport("./views/pedidos/pedidos-view.css")
public class PedidosView extends Div implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	try {
        	if(!VaadinSession.getCurrent().getAttribute("role").toString().equals("ADMIN"))
        		event.rerouteTo(LoginView.class);
    	}catch(Exception ex) {
    		event.rerouteTo(LoginView.class);
    	}
        }
	
	private TextField idIncoming;
	private TextField providerCif;
	private TextField idProduct;
	private TextField quantity;
	private DatePicker dateReceive;
	private DatePicker dateOrder;
	private TextField packageInfo;
	
	
	//private javax.swing.JTextField num1;

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
	
	Grid<Pedido> grid = new Grid<>();
	List<Pedido> pedidos = new ArrayList<>();
	ListDataProvider<Pedido> data = new ListDataProvider<>(pedidos);
	
	

    public PedidosView() {
    	setId("pedidos-view");
        addClassName("pedidos-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
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
        
        
cancel.addClickListener(e -> {
        	
			idIncoming.clear();
			providerCif.clear();
			idProduct.clear();
			quantity.clear();
			dateReceive.clear();
			dateOrder.clear();
			packageInfo.clear();
        
        });
        
        save.addClickListener(e -> {
        	
        	Pedido p = new Pedido(idIncoming.getValue(),
					        			providerCif.getValue(),
					                	idProduct.getValue(),
					                	Integer.parseInt(quantity.getValue()),
					                	dateReceive.getValue(),
        								dateOrder.getValue(),
					                	packageInfo.getValue());
        	//num1.setText("");
        	delete(idIncoming.getValue());
        	idIncoming.clear();
			providerCif.clear();
			idProduct.clear();
			quantity.clear();
			dateReceive.clear();
			dateOrder.clear();
			packageInfo.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	pedidos.removeIf(pedido -> pedido.getIdIncoming()==p.getIdIncoming());
            pedidos.add(p);
            insert(p);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(idIncoming.getValue());
        	pedidos.removeIf(pedido -> pedido.getIdIncoming()==idIncoming.getValue());
        	
        	idIncoming.clear();
			providerCif.clear();
			idProduct.clear();
			quantity.clear();
			dateReceive.clear();
			dateOrder.clear();
			packageInfo.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Pedido selected =  event.getValue();
        	idIncoming.setValue(selected.getIdIncoming());
        	providerCif.setValue(selected.getProviderCif());
        	idProduct.setValue(selected.getIdProduct());
        	quantity.setValue(String.valueOf(selected.getQuantity()));
        	dateReceive.setValue(selected.getDateReceive());
        	dateOrder.setValue(selected.getDateOrder());
        	packageInfo.setValue(selected.getPackageInfo());
        	
        });
        
    }
    
    public long insert(Pedido value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".incomig(idincomig, providercif, idproduct, quantity, datereceive, dateorder, pakageinfo) "
                + "VALUES (?,?,?,?,?,?,?)";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, value.getIdIncoming());
            pstmt.setString(2, value.getProviderCif());
            pstmt.setString(3, value.getIdProduct());
            pstmt.setInt(4, value.getQuantity());
            pstmt.setDate(5, Date.valueOf(value.getDateReceive()));
            pstmt.setDate(6, Date.valueOf(value.getDateOrder()));
            pstmt.setString(7, value.getPackageInfo());

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
        String SQL = "DELETE FROM \"NEWDDBB1\".incomig WHERE idincomig = ?";

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
        
        idIncoming = new TextField("ID");
        providerCif = new TextField("CIF Proveedor");
        idProduct = new TextField("ID Producto");
        quantity = new TextField("Cantidad");
        dateReceive = new DatePicker("Fecha Recibido");
        dateOrder = new DatePicker("Fecha Pedido");
        packageInfo = new TextField("Info");
        Component[] fields = new Component[]{idIncoming, providerCif, idProduct, quantity, dateReceive, dateOrder, packageInfo};

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
