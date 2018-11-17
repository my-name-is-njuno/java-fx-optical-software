package logged.in.user;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
	
	
	private SimpleStringProperty username;
	private SimpleStringProperty email;
	private SimpleStringProperty password;
	private SimpleStringProperty derpartment;
	private SimpleStringProperty position;
	private SimpleStringProperty info;
	private SimpleBooleanProperty active;
	private SimpleIntegerProperty addedby;
	private SimpleStringProperty role;
	private SimpleIntegerProperty id;

	public User(String username, String email, String password, String derpartment,  String position, String info,
			boolean active, int addedby , int id, String role) {
		
		super();
		this.username = new SimpleStringProperty(username);
		this.email = new SimpleStringProperty(email);
		this.password = new SimpleStringProperty(password);
		this.derpartment = new SimpleStringProperty(derpartment);
		this.position = new SimpleStringProperty(position);
		this.info = new SimpleStringProperty(info);
		this.role = new SimpleStringProperty(role);
		this.active = new SimpleBooleanProperty(active);
		this.addedby = new SimpleIntegerProperty(addedby);
		this.id = new SimpleIntegerProperty(id);
		
	}

	public String getUsername() {
		return username.get();
	}

	public String getEmail() {
		return email.get();
	}
	
	public String getRole() {
		return role.get();
	}

	public String getPassword() {
		return password.get();
	}

	public String getDerpartment() {
		return derpartment.get();
	}

	public String getPosition() {
		return position.get();
	}

	public String getInfo() {
		return info.get();
	}

	public boolean isActive() {
		return active.get();
	}

	public int getAddedby() {
		return addedby.get();
	}
	
	
	public int getId() {
		return id.get();
	}
	
	
	
	

}
