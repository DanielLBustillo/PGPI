package com.example.application.views.shop;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.UUID;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.PageTitle;

import com.example.application.data.entity.Envio;

import com.example.application.data.entity.Producto;

import com.example.application.views.Mainshop.MainViewShop;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;

@Route(value = "Shop", layout = MainViewShop.class)
@PageTitle("Shop")
@CssImport("./views/shop/shop-view.css")

public class ShopView extends Div implements AfterNavigationObserver {

	//Inicialización parametros conexión BBDD
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = " ";
	
    
	Grid<Producto> grid = new Grid<>();
	List<Producto> productos = new ArrayList<>();
	ListDataProvider<Producto> data = new ListDataProvider<>(productos);
	

    public ShopView() {
    	
    	setId("shop-view");
        addClassName("shop-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        
        createGridLayout(splitLayout);

        add(splitLayout);
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".product");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Producto producto = new Producto(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getString(7));
	        	this.productos.add(producto);
	        }
	        
		} catch (SQLException e1) {
			e1.printStackTrace();
			//Notification.show(e1.getMessage());
		}
        
        
        Producto p = new Producto("001", "Cepillo", 3,4,5,"Cepillo para pelo de mujer", "--");
        productos.add(p); 
        
        grid.setDataProvider(data);
        
        
        grid.addColumn(Producto::getIdProduct).setHeader("Nº Referencia").setAutoWidth(true);
        grid.addColumn(Producto::getName).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Producto::getDescription).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(Producto::getOrderQuantity).setHeader("Cantidad").setAutoWidth(true);
        grid.addColumn(Producto::getPrice).setHeader("Precio").setAutoWidth(true); 
        grid.addComponentColumn(item-> createBuyButton(grid, item)); 
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
    }  
    
    private Button createBuyButton(Grid<Producto>grid, Producto item) {
    	@SuppressWarnings("unchecked")
    	
    	//Primer formulario
    	FormLayout cliente = new FormLayout();
    	TextField nombre = new TextField();
    	nombre.setLabel("Nombre");    	
    	TextField apellidos = new TextField();
    	apellidos.setLabel("Apellidos");    	
    	TextField direccion = new TextField();
    	direccion.setLabel("Direccion");
    	cliente.add(nombre, apellidos, direccion);

    	//Segundo formulario
    	FormLayout articulo = new FormLayout(); 
    	articulo.add("Número de referencia del producto: " + item.getIdProduct());
    	
    	//Tercer formulario
    	FormLayout articulo2 = new FormLayout(); 
    	articulo2.add("Articulo: " + item.getName());
    	
    	//Cuarto formulario
    	//String precio = String.valueOf(item.getPrice());
    	FormLayout articulo3 = new FormLayout();
    	articulo3.add("Precio unitario: " + item.getPrice() + "€");
    	
    	TextField cantidad = new TextField(); 
    	cantidad.setLabel("Cantidad de articulos");
    	cantidad.setPlaceholder(item.getOrderQuantity() + " articulos en stock");
    	
    	articulo2.add(cantidad);
    	
    	//Boton pago
    	Button bPago = new Button("PAGO"); 
    	

    	//Dialogo compra producto
    	Dialog dialog = new Dialog();
    	
    	dialog.addComponentAtIndex(0, new Text("Información de cliente"));
    	dialog.addComponentAtIndex(1, cliente);
    	dialog.addComponentAtIndex(2, new Text("Información de compra del producto"));
    	dialog.addComponentAtIndex(3, articulo);
    	dialog.addComponentAtIndex(4, articulo2);
    	dialog.addComponentAtIndex(5, articulo3);
    	dialog.addComponentAtIndex(6, bPago); //apunta a pedidos y registra nuevo. 
    	
    	//Interación posterior
    	Notification notification = new Notification("Pedido Registrado Correctamente", 3000);
    	bPago.addClickListener(event -> {
    		 
    		
    		 
    		try {
    			
    		dialog.close();
    			
    		String user = VaadinSession.getCurrent().getAttribute("user").toString();  
    		
    		
    		String numPedido = UUID.randomUUID().toString();
    		String tracking = UUID.randomUUID().toString();
    			
    		Envio envio = new Envio(numPedido, user, direccion.toString(), tracking, item.getPrice(), "Preparando" );
    		insert(envio); 
    		String idStorage = getIdStorage(item); 
    		insertOrderProduct(envio, item, idStorage); 
    		
    		createInvoice(item); 
    		
    		
    		if (tracking.length() != 0) {
    			
    			notification.open(); 
            	
    		}
    		
    		}catch(Exception e) {
    			
    			UI.getCurrent().navigate("Login");
    		}

    	}); 
    	
    	

    	 Button button = new Button("Comprar", clickEvent -> {
    	        
				ListDataProvider<Producto> dataProvider = (ListDataProvider<Producto>) grid.getDataProvider();
				//-----------------------------------------------------------------------------------------------------------

				dialog.open();
				//-------------------------------------------------------------------------------------------------------------
    	        dataProvider.refreshAll();
    	    });
    	 
    	 
    	 return button; 
    

    }
    
    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        

       
    }
    
    private void createGridLayout(SplitLayout splitLayout) {
    	
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
        
    }
    
    public void insert(Envio value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".\"Order\"(idorder, username, address, trackingnumber, price, state) "
                + "VALUES (?,?,?,?,?,?)";


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
                        
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }}
        
        public void insertOrderProduct(Envio value, Producto product, String IDStorage) {
        	String SQL = "INSERT INTO \"NEWDDBB1\".\"orderproduct\"(idorder, idstorage, quantity) "
                    + "VALUES (?,?,?)";

            try (Connection conn =  DriverManager.getConnection(url, user, pass);
                    PreparedStatement pstmt = conn.prepareStatement(SQL,
                    Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, value.getIdOrder());
                pstmt.setString(2, IDStorage);
                pstmt.setInt(3, product.getOrderQuantity());
               

                int affectedRows = pstmt.executeUpdate();
                // check the affected rows 
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        
    }
        
        public String  getIdStorage(Producto product) {
        	String output = "";
            try {
            	
            	String idProducto = product.getIdProduct(); 
                PreparedStatement pst;
            	Connection con = DriverManager.getConnection(url, user, pass);
    			pst = con.prepareStatement("SELECT a.\"idstorage\" from \"NEWDDBB1\".storage WHERE a.\"idproduct\" = ?");
    			pst.setString(1, idProducto);
    			
                ResultSet rs = pst.executeQuery();
                output = rs.toString(); 
                return output; 
  
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    			
    		}
            
            return output;
     
    }
        
        public void createInvoice(Producto item) {
        	String path =  "hello.pdf";
        	
        	try {
        			
        		   Document doc = new Document();
        		   PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path));
        		   //setting font family, color
        		   Font font = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.RED);
        		   doc.open();
        		   Paragraph para = new Paragraph("\n\n Factura simplificada de compra: \n\n\n\n ", font);
        		   Paragraph compra = new Paragraph("Articulo: " + item.getName() + "\n" + "Descripcion:" + item.getDescription() + "\n" +  "Precio:" + item.getPrice() + "€");
        		   doc.add(para);
        		   doc.add(compra);
        		   doc.close();
        		   writer.close();
        		   
        		  } catch (DocumentException | FileNotFoundException e) {
        		   // TODO Auto-generated catch block
        		   e.printStackTrace();
        }
    
        }
}