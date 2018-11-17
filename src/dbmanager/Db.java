package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import logged.in.user.LoggedinUser;

public class Db {
	
	String dburl = "jdbc:mysql://localhost:3306/javaoptical";
	String user = "root";
	String password = "";
	
	Connection conn = null;
	Statement stmt = null;
	
	private static Db handler = null;
	
	private LoggedinUser lu;
	
	private Db() {
		createConn();
	}
	
	
	public static Db getInstance() {
		
		if(handler == null) {
			
			handler = new Db();
			
		}
		return handler;
		
	}

	private void createConn() {
		
		try {
			
			conn = DriverManager.getConnection(dburl, user, password);

		} catch (SQLException e) {
			
			alertMaker(e.getMessage());
			
		}
		
	}


	


	


	public LoggedinUser getLu() {
		return lu;
	}


	public void setLu(LoggedinUser lu) {
		this.lu = lu;
	}
	
	
	
	public boolean execAction(String qu) {
		
		try {
			stmt = conn.createStatement();
			stmt.execute(qu);
//			alertMaker("Query run Successfully");
			return true;
		} catch (SQLException e) {
			alertMaker(e.getMessage());
		}
		
		return false;
	}
	
	
	
	public ResultSet execQuery(String qu) {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(qu);
		} catch (SQLException e) {
			alertMaker(e.getMessage());
		}
		return rs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void alertMaker(String message) {
		
		JOptionPane.showMessageDialog(null, message);
		
	}
	
	
	
	
	
	

}
