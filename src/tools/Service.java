package tools;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Service {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleStringProperty addedby;
	private SimpleDoubleProperty cost;
	public Service(int id, String name, String description, String addedby, double cost) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.addedby = new SimpleStringProperty(addedby);
		this.cost =  new SimpleDoubleProperty(cost);
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
	public String getAddedby() {
		return addedby.get();
	}
	public double getCost() {
		return cost.get();
	}
	
	
	

}
