package tools;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Drug {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleStringProperty measure;
	private SimpleDoubleProperty buyingPice;
	private SimpleDoubleProperty sellingPrice;
	private SimpleIntegerProperty quantity;
	private SimpleStringProperty addedBy;
	
	public Drug(int id, String name, String description, String measure, double buyingPice, double sellingPrice,
			int quantity, String addedBy) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.measure = new SimpleStringProperty(measure);
		this.buyingPice =  new SimpleDoubleProperty(buyingPice);
		this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.addedBy = new SimpleStringProperty(addedBy);
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
	public String getMeasure() {
		return measure.get();
	}
	public double getBuyingPice() {
		return buyingPice.get();
	}
	public double getSellingPrice() {
		return sellingPrice.get();
	}
	public int getQuantity() {
		return quantity.get();
	}
	public String getAddedBy() {
		return addedBy.get();
	}
	
	
	
	
	
	

}
