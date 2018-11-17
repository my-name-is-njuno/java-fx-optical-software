package tools;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Insurance {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty email;
	private SimpleStringProperty contact;
	private SimpleStringProperty contactPerson;
	private SimpleStringProperty adress;
	private SimpleBooleanProperty active;
	private SimpleStringProperty addedBy;
	
	
	
	public Insurance(int id, String name, String email, String contact, String contactPerson, String adress,
			boolean active, String addedBy) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.email = new SimpleStringProperty(email);
		this.contact = new SimpleStringProperty(contact);
		this.contactPerson = new SimpleStringProperty(contactPerson);
		this.adress = new SimpleStringProperty(adress);
		this.active = new SimpleBooleanProperty(active);
		this.addedBy = new SimpleStringProperty(addedBy);
	}



	public int getId() {
		return id.get();
	}



	public String getName() {
		return name.get();
	}



	public String getEmail() {
		return email.get();
	}



	public String getContact() {
		return contact.get();
	}



	public String getContactPerson() {
		return contactPerson.get();
	}



	public String getAdress() {
		return adress.get();
	}



	public boolean isActive() {
		return active.get();
	}
	
	
	
	
	public String getAddedBy() {
		return addedBy.get();
	}
	
	
	
	
	

}
