package tools;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tool {
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty supplier;
	private SimpleStringProperty measure;
	private SimpleStringProperty description;
	private SimpleStringProperty addedby;
	private SimpleDoubleProperty quantity;
	private SimpleDoubleProperty bp;
	private SimpleStringProperty available;
	
	public Tool(int id, String name, String supplier, String measure, String description, String addedby, double quantity, double bp) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.supplier = new SimpleStringProperty(supplier);
		this.measure = new SimpleStringProperty(measure);
		this.description = new SimpleStringProperty(description);
		this.addedby = new SimpleStringProperty(addedby);
		this.quantity = new SimpleDoubleProperty(quantity);
		this.bp = new SimpleDoubleProperty(bp);
		this.available = new SimpleStringProperty(String.valueOf(quantity) + " " + measure );
	}
	
	
	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getAvailable() {
		return available.get();
	}
	public String getMeasure() {
		return measure.get();
	}
	public String getSupplier() {
		return supplier.get();
	}
	public String getDescription() {
		return description.get();
	}
	public String getAddedby() {
		return addedby.get();
	}
	public double getQuantity() {
		return quantity.get();
	}
	public double getBp() {
		return bp.get();
	}
	
	
	
}
