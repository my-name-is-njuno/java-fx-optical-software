package tools;



import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Doctor {
	
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty email;
	private SimpleStringProperty contact;
	private SimpleStringProperty speciality;
	private SimpleBooleanProperty active;
	private SimpleStringProperty createdAt;
	private SimpleIntegerProperty  patients;
	private SimpleStringProperty addedby;
	private SimpleStringProperty contract;
	
	
	public Doctor(int id, String name, String speciality, boolean active, String createdAt, int patients,
			String addedby , String contract, String email, String contact) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.speciality = new SimpleStringProperty(speciality);
		this.active = new SimpleBooleanProperty(active);
		this.createdAt = new SimpleStringProperty(createdAt);
		this.patients = new SimpleIntegerProperty(patients);
		this.addedby = new SimpleStringProperty(addedby);
		this.contract = new SimpleStringProperty(contract);
		this.email = new SimpleStringProperty(email);
		this.contact = new SimpleStringProperty(contact);
	}


	


	public String getEmail() {
		return email.get();
	}





	public String getContact() {
		return contact.get();
	}





	public SimpleBooleanProperty getActive() {
		return active;
	}





	public String getContract() {
		return contract.get();
	}


	public int getId() {
		return id.get();
	}


	public String getName() {
		return name.get();
	}


	public String getSpeciality() {
		return speciality.get();
	}


	public boolean isActive() {
		return active.get();
	}


	public String getCreatedAt() {
		return createdAt.get();
	}


	public int getPatients() {
		return patients.get();
	}


	public String getAddedby() {
		return addedby.get();
	}
	
	

}
