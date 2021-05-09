package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.example.application.data.entity.Cliente;
import com.example.application.data.entity.Envio;
import com.example.application.data.entity.Producto;
import com.example.application.data.entity.Proveedor;
import com.example.application.views.admin.ProductosView;
import com.example.application.views.login.LoginView;

public class tests {

	@Test
	public void LOGIN_test() {
		LoginView web = new LoginView();
		String role = "";
		try {
			role = web.check_login("ADMIN","ADMIN");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (role == "ADMIN") {
		fail("singn in not successfull");
		}
	}
	
	@Test
	public void delete_test() {
		try {
		ProductosView prod = new ProductosView(); 
		
		int test2= prod.delete("3");
		if(test2!=0) {
			fail("Error delete");
		}
		}catch(Exception e) {}
	}
	
	@Test
	public void insert_test() {
		try {
		ProductosView prod = new ProductosView(); 
				long test= prod.insert(new Producto("1","2",3,4,5,"6","7"));
		
		if(test!=0) {
			fail("Error insertion");
		}
		}catch(Exception e) {}
	}
	
	@Test
	public void producto_test() {
		Producto prod = new Producto("1","2",3,4,5,"6","7");
		prod.setName("0");
		if(prod.getName()!="0") {
			fail("Error change");
		}
	}
	
	@Test
	public void envio_test() {
		Envio e = new Envio("1","2","3","4",5,"6");
		e.setUsername("0");
		if(e.getUsername()!="0") {
			fail("Error change");
		}
	}
	
	@Test
	public void proveedor_test() {
		Proveedor prov = new Proveedor("1","2","3");
		prov.setName("0");
		if(prov.getName()!="0") {
			fail("Error change");
		}
	}
	
	@Test
	public void cliente_test() {
		Cliente c = new Cliente("1","2","3","4","5","6");
		c.setName("0");
		if(c.getName()!="0") {
			fail("Error change");
		}
	}
	
	

}