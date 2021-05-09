package com.example.application.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.login.LoginView;
import com.example.application.data.entity.Producto;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

@Route(value = "productos", layout = MainViewAdmin.class)
//@RouteAlias(value = "", layout = MainViewAdmin.class)
@PageTitle("Productos")
@CssImport("./views/productos/productos-view.css")



public class ProductosView extends Div implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	try {
        	if(!VaadinSession.getCurrent().getAttribute("role").toString().equals("ADMIN"))
        		event.rerouteTo(LoginView.class);
    	}catch(Exception ex) {
    		event.rerouteTo(LoginView.class);
    	}
        }
	
	
	private TextField idProduct;
	private TextField name;
	private TextField price;
	private TextField minQuantity;
	private TextField orderQuantity;
	private TextField description;
	private TextField providerCif;
	private TextField stock;
	//private javax.swing.JTextField num1;

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	MemoryBuffer buffer = new MemoryBuffer();
	Upload upload = new Upload(buffer);
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
    public static final String SAMPLE_XLSX_FILE_PATH = "Ejemplo_Proov_Ref_2021.xlsx";
	
	Grid<Producto> grid = new Grid<>();
	List<Producto> productos = new ArrayList<>();
	ListDataProvider<Producto> data = new ListDataProvider<>(productos);

    public ProductosView() {
    	setId("productos-view");
        addClassName("productos-view");
        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        
        
        try {
		    
            PreparedStatement pst;
        	Connection con = DriverManager.getConnection(url, user, pass);
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".product p LEFT JOIN \"NEWDDBB1\".newview n on n.idproduct =p.idproduct ");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Producto producto = new Producto(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getString(7),rs.getInt(9));
	        	this.productos.add(producto);
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
			Notification.show(e1.getMessage());
		}

        
        
        //grid.setItems(productos);
        grid.setDataProvider(data);
        //grid.setColumnOrder("idProduct","name","price","minQuantity","orderQuantity","description","providerCif");
        grid.addColumn(Producto::getName).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Producto::getIdProduct).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Producto::getPrice).setHeader("Precio").setAutoWidth(true);
        grid.addColumn(Producto::getMinQuantity).setHeader("Min").setAutoWidth(true);
        grid.addColumn(Producto::getOrderQuantity).setHeader("Cantidad a pedir").setAutoWidth(true);
        grid.addColumn(Producto::getDescription).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(Producto::getProviderCif).setHeader("CIF").setAutoWidth(true);
        grid.addColumn(Producto::getStock).setHeader("Stock").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
        
        
        cancel.addClickListener(e -> {
        	
        	name.clear();
        	idProduct.clear();
        	price.clear();
        	minQuantity.clear();
        	orderQuantity.clear();
        	description.clear();
        	providerCif.clear();
        	stock.clear();
        
        });
        
        save.addClickListener(e -> {
        	
        	Producto p = new Producto(idProduct.getValue(),
                	name.getValue(),
                	Integer.parseInt(price.getValue()),
                	Integer.parseInt(minQuantity.getValue()),
                	Integer.parseInt(orderQuantity.getValue()),
                	description.getValue(),
                	providerCif.getValue(),
                	Integer.parseInt(stock.getValue()));
        	//num1.setText("");
        	delete(idProduct.getValue());
        	delete2(idProduct.getValue());
        	name.clear();
        	idProduct.clear();
        	price.clear();
        	minQuantity.clear();
        	orderQuantity.clear();
        	description.clear();
        	providerCif.clear();
        	stock.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	productos.removeIf(producto -> producto.getIdProduct()==p.getIdProduct());
            productos.add(p);
            insert(p);
            insert2(p);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(idProduct.getValue());
        	delete2(idProduct.getValue());
        	productos.removeIf(producto -> producto.getIdProduct()==idProduct.getValue());
        	name.clear();
        	idProduct.clear();
        	price.clear();
        	minQuantity.clear();
        	orderQuantity.clear();
        	description.clear();
        	providerCif.clear();
        	stock.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        upload.addSucceededListener(event -> {
    		Notification.show("done");
    		Workbook workbook;
    		File excel =new File(event.getFileName());
    		String nameProv=null;
    		String nameProd=null;
    		int min=0;
			try {
				workbook = WorkbookFactory.create(buffer.getInputStream());
	    		//Notification.show("got here");
	
	            // Getting the Sheet at index zero
	            Sheet sheet = workbook.getSheetAt(0);

	            // Create a DataFormatter to format and get each cell's value as String
	            DataFormatter dataFormatter = new DataFormatter();        // 1. You can obtain a rowIterator and columnIterator and iterate over them
	            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
	            Iterator<Row> rowIterator = sheet.rowIterator();
	            int aux2=0;
	            while (rowIterator.hasNext()) {
	                Row row = rowIterator.next();

	                // Now let's iterate over the columns of the current row
	                Iterator<Cell> cellIterator = row.cellIterator();
	                int aux =0;
	                if(aux2!=0) {
	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next();
	                    String cellValue = dataFormatter.formatCellValue(cell);
	                    if(aux==0) {
	                    	try {
    	                    	PreparedStatement pst;
    	                     	Connection con = DriverManager.getConnection(url, user, pass);
    	             			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".provider");
    	                         ResultSet rs = pst.executeQuery();
    	                         
    	             	        while (rs.next()) {
    	             	        	
    	             	        	
    	             	        	if(cellValue.equals(rs.getString(2)))
    	             	        		nameProv = rs.getString(1);
    	             	        	
    	             	        }
    	                    	}catch (SQLException e1) {
    	             			e1.printStackTrace();
    	             			Notification.show(e1.getMessage());
    	             		}
	                    }
	                    if(aux==1) {
	                    	
	                    	nameProd = cellValue;
	                    	
	                    }
	                    if(aux==2) {
	                    	min = Integer.parseInt(cellValue);
	                    }
	                    //this.add(new TextField(cellValue));
	                    aux++;
	                }
	                Producto p = new Producto (UUID.randomUUID().toString(),nameProd,0,min,0,"",nameProv,0);
	                productos.add(p);
	                insert(p);
	                insert2(p);
	                
	                }
	                aux2++;
	            }grid.getDataProvider().refreshAll();
			} catch (Exception ex) {

				Notification.show(ex.getMessage());
			}
    	});
        
        
        grid.asSingleSelect().addValueChangeListener(event ->{
        	
        	Producto selected =  event.getValue();
        	name.setValue(selected.getName());
        	idProduct.setValue(selected.getIdProduct());
        	price.setValue(String.valueOf(selected.getPrice()));
        	minQuantity.setValue(String.valueOf(selected.getMinQuantity()));
        	orderQuantity.setValue(String.valueOf(selected.getOrderQuantity()));
        	description.setValue(selected.getDescription());
        	providerCif.setValue(selected.getProviderCif());
        	stock.setValue(String.valueOf(selected.getStock()));
        	
        });
        
        
        
        
    }
    

    
    public long insert(Producto value) {
    	String SQL = "INSERT INTO \"NEWDDBB1\".product(idproduct, \"name\", price, minquantity, orderquantity, description, providercif) "
                + "VALUES (?,?,?,?,?,?,?)";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, value.getIdProduct());
            pstmt.setString(2, value.getName());
            pstmt.setInt(3, value.getPrice());
            pstmt.setInt(4, value.getMinQuantity());
            pstmt.setInt(5, value.getOrderQuantity());
            pstmt.setString(6, value.getDescription());
            pstmt.setString(7, value.getProviderCif());

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
    
    public long insert2(Producto value) {
    	String SQL = "UPDATE \"NEWDDBB1\".storage SET idproduct=?, quantity=? WHERE idstorage = (select min(idstorage) from \"NEWDDBB1\".storage where quantity = 0 )";
               // + "VALUES (?,?) WHERE quantity=0";

        long id = 0;

        try (Connection conn =  DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

           
            pstmt.setString(1, value.getIdProduct());
            //pstmt.setString(2, value.getIdProduct());
            //pstmt.setString(3, value.getIdProduct());
            pstmt.setInt(2, value.getStock());
            

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
        String SQL = "DELETE FROM \"NEWDDBB1\".product WHERE idproduct = ?";

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
    public int delete2(String id) {
        String SQL = "UPDATE \"NEWDDBB1\".storage SET quantity=0 WHERE idproduct = ?";

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
        
        name = new TextField("Nombre");
        idProduct = new TextField("ID");
        price = new TextField("Precio");
        minQuantity = new TextField("Cantidad minima");
        orderQuantity = new TextField("Cantidad a pedir");
        description = new TextField("Descripcion");
        providerCif = new TextField("CIF Proveedor");
        stock = new TextField("Stock");
        Component[] fields = new Component[]{name, idProduct, price, minQuantity, orderQuantity, description, providerCif, stock};

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
        
        buttonLayout.add(save, cancel, delete, upload);
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
