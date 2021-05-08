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
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.data.entity.Producto;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

@Route(value = "productos", layout = MainViewAdmin.class)
//@RouteAlias(value = "", layout = MainViewAdmin.class)
@PageTitle("Productos")
@CssImport("./views/productos/productos-view.css")



public class ProductosView extends Div {
	
	
	private TextField idProduct;
	private TextField name;
	private TextField price;
	private TextField minQuantity;
	private TextField orderQuantity;
	private TextField description;
	private TextField providerCif;
	//private javax.swing.JTextField num1;

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	private Button read = new Button("Leer Terminales");
	
	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String pass = "pgpi";
	
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
			pst = con.prepareStatement("SELECT * from \"NEWDDBB1\".product");
            ResultSet rs = pst.executeQuery();
            
	        while (rs.next()) {
	        	Producto producto = new Producto(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getString(7));
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
        grid.addColumn(Producto::getOrderQuantity).setHeader("Cantidad").setAutoWidth(true);
        grid.addColumn(Producto::getDescription).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(Producto::getProviderCif).setHeader("CIF").setAutoWidth(true);
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
        
        });
        
        save.addClickListener(e -> {
        	
        	Producto p = new Producto(idProduct.getValue(),
                	name.getValue(),
                	Integer.parseInt(price.getValue()),
                	Integer.parseInt(minQuantity.getValue()),
                	Integer.parseInt(orderQuantity.getValue()),
                	description.getValue(),
                	providerCif.getValue());
        	//num1.setText("");
        	delete(idProduct.getValue());
        	name.clear();
        	idProduct.clear();
        	price.clear();
        	minQuantity.clear();
        	orderQuantity.clear();
        	description.clear();
        	providerCif.clear();
        	
        	//Predicate<Producto> condition = employee -> employee.getName().startsWith("P");
        	
        	productos.removeIf(producto -> producto.getIdProduct()==p.getIdProduct());
            productos.add(p);
            insert(p);
            grid.getDataProvider().refreshAll();
            
        });
        
        delete.addClickListener(e -> {
        	delete(idProduct.getValue());
        	productos.removeIf(producto -> producto.getIdProduct()==idProduct.getValue());
        	name.clear();
        	idProduct.clear();
        	price.clear();
        	minQuantity.clear();
        	orderQuantity.clear();
        	description.clear();
        	providerCif.clear();
        	
        	
        	grid.getDataProvider().refreshAll();
        });
        
        read.addClickListener(e -> {
        	
        	        	
        	try  
        	{  
        	File file = new File("Ejemplo_Proov_Ref_2021.xlsx");   //creating a new file instance  
        	FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
        	//creating Workbook instance that refers to .xlsx file 
        	
        	XSSFWorkbook wb = new XSSFWorkbook(fis);  
        	
        	XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
        	Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
        	while (itr.hasNext())                 
        	{  
        	Row row = itr.next();  
        	Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
        	while (cellIterator.hasNext())   
        	{  
        	Cell cell = cellIterator.next();  
        	switch (cell.getCellType())               
        	{  
        	case Cell.CELL_TYPE_STRING:    //field that represents string cell type  
        	System.out.print(cell.getStringCellValue() + "\t\t\t");  
        	break;  
        	case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type  
        	System.out.print(cell.getNumericCellValue() + "\t\t\t");  
        	break;  
        	default:  
        	}  
        	}  
        	System.out.println("");  
        	}  
        	}  
        	catch(Exception ex)  
        	{  
        	ex.printStackTrace();  
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
        Component[] fields = new Component[]{name, idProduct, price, minQuantity, orderQuantity, description, providerCif};

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
        read.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        buttonLayout.add(save, cancel, delete, read);
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
