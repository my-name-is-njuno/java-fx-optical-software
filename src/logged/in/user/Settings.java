package logged.in.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

import dbmanager.Db;

public class Settings {
	
	
	
	public static void initUserSettings() {
		Db handler = Db.getInstance();
		ResultSet rs = handler.execQuery("SELECT * FROM users WHERE type = 1");
		
		try {
			if(!rs.next()) {
				if(handler.execAction("INSERT INTO users (name, email, password, type, department, position, info) values ("
						+ "'admin', 'admin@ops.com', '"+ DigestUtils.sha1Hex("admin")+"', 1, 'admin', 'System Admin', 'System administrator: OSA') ")) {
					handler.alertMaker("First Install, admin user created");
				}
			} else {
//				handler.alertMaker("Admin User already created");
			}
		} catch (SQLException e) {
			handler.alertMaker(e.getMessage());
		}
		
	}
	
	
	
	
	
	

}
