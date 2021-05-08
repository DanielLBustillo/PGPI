package test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

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
		if (role == "ADMIN")
		fail("Not yet implemented");
	}
	

}
