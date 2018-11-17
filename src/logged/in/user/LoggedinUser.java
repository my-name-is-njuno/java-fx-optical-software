package logged.in.user;

import java.util.Date;

public class LoggedinUser {
	
	private int id;
	private String username;
	private String email;
	private String password;
	private boolean type;
	private String derpartment;
	private String position;
	private String info;
	private boolean active;
	private Date createdAt;
	
	
	
	public LoggedinUser(int id, String username, String email, String password, boolean type, String derpartment,
			String position, String info, boolean active, Date createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.type = type;
		this.derpartment = derpartment;
		this.position = position;
		this.info = info;
		this.active = active;
		this.createdAt = createdAt;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public String getDerpartment() {
		return derpartment;
	}
	public void setDerpartment(String derpartment) {
		this.derpartment = derpartment;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	

	
	
	
	
	
	

}
