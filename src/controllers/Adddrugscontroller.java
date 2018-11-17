package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import tools.Drug;

public class Adddrugscontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXComboBox<String> measure;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXTextField bp;

    @FXML
    private JFXTextField totalCost;

    @FXML
    private JFXTextField totalOtherCost;

    @FXML
    private JFXTextField otherCost;

    @FXML
    private JFXComboBox<String> supplier;

    @FXML
    private JFXComboBox<String> modePayment;

    @FXML
    private Button addSuplpier;

    @FXML
    private VBox header;
    
    @FXML
    private JFXTextField sp;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


	LoggedinUser lu;
    
    Db handler;
    
    
    ObservableList<String> paymentsMode = FXCollections.observableArrayList("Credit", "Bank", "Cash");
    
    ObservableList<String> measurements = FXCollections.observableArrayList("Grams", "Kilograms", "Litres", "Packets", "Pills");
    
    static ObservableList<String> supplierslist = FXCollections.observableArrayList();
   
    
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	ResultSet rs = handler.execQuery("SELECT name FROM suppliers");
    	supplierslist.clear();
    	try {
			while(rs.next()) {
				supplierslist.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
    	
    	modePayment.setItems(paymentsMode);
    	
    	measure.setItems(measurements);
    	
    	supplier.setItems(supplierslist);
		
	}

    @FXML
    void addNewSupplier(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addsupplier.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("New Supplier");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			alertErrorMaker(e.getMessage());
			e.printStackTrace();
		}
    }

    
    
    @FXML
    void cancelOperation(ActionEvent event) {
    	
    	((Stage) name.getScene().getWindow()).close();
    	
    }
    
    
    
    

    @FXML
    void saveDrug(ActionEvent event) {
    	
    	if(fieldsNotEmpty()) {
    		
    		if(quantityValueIsInteger()) {
    			
    			if(bpValueIsDouble()) {
    				
    				if(totalOtherCostIsDouble()) {
    					
    					if(totalCostIsOkey()) {
    						
    						if(spIsDouble()) {
    							
    							if(drugIsSavedToDb()) {
    								
    								if(expenseIsAddedToDb()) {
    									
    									if(userActivityIsAddedToDb()) {
    										
    										alertSuccessMaker("Drug added successfully");
    										
    										closee();
    										
    										ResultSet rs = handler.execQuery("SELECT * FROM drugs WHERE name = '"+name.getText()+"'");
    										try {
    											if(rs.first()) {
    												Maindrugscontroller.drugslist.add(new Drug(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("measure"), 
    												rs.getDouble("buyingprice"), rs.getDouble("sellingprice"), rs.getInt("quantity"), lu.getUsername()));
    											}
    										} catch (SQLException e) {
    											alertErrorMaker(e.getMessage());
    										}

    										
    										
    									}
    									
    								}
    								
    							}
    							
    						}
    						
    					}
    					
    				}
    				
    			}
    			
    		}
    		
    	}

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void closee() {
		
    	((Stage) name.getScene().getWindow()).close();
		
	}

	private boolean userActivityIsAddedToDb() {
    	if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new drug "+name.getText()+"')")) {
			return true;
		}
    	alertErrorMaker("There was an error in activity");
    	return false;
	}

	private boolean expenseIsAddedToDb() {
		
		String drugName = name.getText();
		double drugQuantity = Double.parseDouble(quantity.getText());
		String drugMeasure = measure.getValue();
		double drugBp = Double.parseDouble(bp.getText());
		double totalBp = drugBp * drugQuantity;
		String drugOtherCost = otherCost.getText();
		double drugTotalOtherCost = Double.parseDouble(totalOtherCost.getText());
		double drugTotalCost = Double.parseDouble(totalCost.getText());
		String drugModeOfPayment = modePayment.getValue();
		int userid = lu.getId();
		
		
		String sql = "INSERT INTO `expenses`(`category`, `item`, `measure`, `quantity`, `bp`, `tbp`, `othercost`,"
				+ " `totalothercost`, `totalcost`, `modeofpayment`, `created_by`) VALUES ("
				+ "'drugs',"
				+ "'"+drugName+"',"
				+ "'"+drugMeasure+"',"
				+ "'"+drugQuantity+"',"
				+ "'"+drugBp+"',"
				+ "'"+totalBp+"',"
				+ "'"+drugOtherCost+"',"
				+ "'"+drugTotalOtherCost+"',"
				+ "'"+drugTotalCost+"',"	
				+ "'"+drugModeOfPayment+"',"
				+ "'"+userid+"'"
				+ ")";
		
		
		if(handler.execAction(sql)) {
			return true;
		}
		
		alertErrorMaker("Error, in expenses table");		
		return false;
	}

	private boolean drugIsSavedToDb() {
		
		String drugName = name.getText();
		String drugDescription = description.getText();
		double drugQuantity = Double.parseDouble(quantity.getText());
		String drugMeasure = measure.getValue();
		double drugSp = Double.parseDouble(sp.getText());
		double drugBp = Double.parseDouble(bp.getText());
		
		int userid = lu.getId();
		
		String sql = "INSERT INTO drugs (name,description,quantity,measure,sellingprice,buyingprice,addedby) VALUES ("
				+ "'"+drugName+"',"
				+ "'"+drugDescription+"',"
				+ "'"+drugQuantity+"',"
				+ "'"+drugMeasure+"',"
				+ "'"+drugSp+"',"
				+ "'"+drugBp+"',"
				+ "'"+userid+"'"
				+ ")";
		
		if(handler.execAction(sql)) {
			
			return true;
			
		}
		alertErrorMaker("Error, drugs table");
		return false;
	}

	private boolean spIsDouble() {
		try {
			Double.parseDouble(sp.getText());
			return true;
		} catch(Exception e) {
			alertErrorMaker("The value of total of selling price should be a number / Decimal");
			return false;
		}
	}

	private boolean totalCostIsOkey() {
		try {
			double byp = Double.parseDouble(bp.getText());
			int qn = Integer.parseInt(quantity.getText());
			double toc = Double.parseDouble(totalOtherCost.getText());
			
			double tc = (byp * qn) + toc;
			
			totalCost.setText(String.valueOf(tc));
			
			return true;
		} catch(Exception e) {
			alertErrorMaker("The value of total cost is not being evaluated successfully");
			return false;
		}
	}

	private boolean totalOtherCostIsDouble() {
		try {
			Double.parseDouble(totalOtherCost.getText());
			return true;
		} catch(Exception e) {
			alertErrorMaker("The value of total of other cost should be a number / Decimal");
			return false;
		}
	}

	private boolean bpValueIsDouble() {
		try {
			Double.parseDouble(bp.getText());
			return true;
		} catch(Exception e) {
			alertErrorMaker("The value of buying price should be a number / Decimal");
			return false;
		}
	}

	private boolean quantityValueIsInteger() {
		try {
			Double.parseDouble(quantity.getText());
			return true;
		} catch(Exception e) {
			alertErrorMaker("The value of quantity should be a number");
			return false;
		}
	}

	private boolean fieldsNotEmpty() {
		if(name.getText().isEmpty() || description.getText().isEmpty() || quantity.getText().isEmpty()
				|| measure.getSelectionModel().isEmpty() || bp.getText().isEmpty() || supplier.getSelectionModel().isEmpty() || 
				otherCost.getText().isEmpty() || totalOtherCost.getText().isEmpty() || modePayment.getSelectionModel().isEmpty()
				 || sp.getText().isEmpty()) {
			
			alertErrorMaker("Enter Values for all fields");
			
			return false;
			
		}
		return true;
	}
	
	
	
	
	
	
	

	private void alertErrorMaker(String message) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}


	private void alertSuccessMaker(String message) {
		
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
