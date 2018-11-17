package tools;

import java.util.Date;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Procedure {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleDoubleProperty charge;
	private SimpleStringProperty createdBy;
	
	
	
	
	public Procedure(int id, String name, String description, double charge, String createdBy) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.charge = new SimpleDoubleProperty(charge);
		this.createdBy = new SimpleStringProperty(createdBy);
		
	}
	
	
	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getDescription() {
		return description.get();
	}
	public double getCharge() {
		return charge.get();
	}
	public String getCreatedBy() {
		return createdBy.get();
	}
	
	
	
	
	

}
