package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class Restockdrugscontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXTextField measure;

    @FXML
    private JFXTextField bp;

    @FXML
    private JFXTextField totalOtherCost;

    @FXML
    private JFXTextField otherCost;

    @FXML
    private JFXComboBox<String> modePayment;

    @FXML
    private Button addSuplpier;

    @FXML
    private JFXTextField sp;

    @FXML
    private JFXComboBox<String> drug;

    @FXML
    private JFXComboBox<String> supplier;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    LoggedinUser lu;
    
    Db handler;
    
    
    ObservableList<String> paymentsMode = FXCollections.observableArrayList("Credit", "Bank", "Cash");
    
    ObservableList<String> drugLists = FXCollections.observableArrayList();
    
    ObservableList<String> measurements = FXCollections.observableArrayList("Grams", "Kilograms", "Litres", "Packets", "Pills");
    
    static ObservableList<String> supplierslist = FXCollections.observableArrayList();
   
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	ResultSet rs = handler.execQuery("SELECT name FROM suppliers ORDER BY name");
    	ResultSet rss = handler.execQuery("SELECT name FROM drugs ORDER BY name");
    	supplierslist.clear();
    	drugLists.clear();
    	
    	try {
			while(rs.next()) {
				supplierslist.add(rs.getString("name"));
			}
			
			while(rss.next()) {
				drugLists.add(rss.getString("name"));
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
    	
    	modePayment.setItems(paymentsMode);
    	
    	
    	supplier.setItems(supplierslist);
    	
    	drug.setItems(drugLists);
    	
    	
    	
    	drug.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				
				ResultSet rs = handler.execQuery("SELECT * FROM drugs WHERE name = '"+drug.getValue()+"'");
				
				try {
					if(rs.first()) {
						measure.setText(rs.getString("measure"));
						bp.setText(String.valueOf(rs.getDouble("buyingprice")));
						sp.setText(String.valueOf(rs.getDouble("sellingprice")));
						
						Maindrugscontroller.drugslist.remove(new Drug(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("measure"), 
    												rs.getDouble("buyingprice"), rs.getDouble("sellingprice"), rs.getInt("quantity"), lu.getUsername()));
					}
				} catch (SQLException e) {
					alertErrorMaker(e.getMessage());
				}
				
			}
    		
    	})  ;		
    	
		
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
    	((Stage) drug.getScene().getWindow()).close();
    }

    @FXML
    void saveDrug(ActionEvent event) {
    	if(fieldsNotEmpty()) {
    		
    		if(quantityValueIsInteger()) {
    			
    			if(bpValueIsDouble()) {
    				
    				if(totalOtherCostIsDouble()) {
    					
    					
    						
    						if(spIsDouble()) {
    							
    							if(drugIsUpdatedToDb()) {
    								
    								if(expenseIsAddedToDb()) {
    									
    									if(userActivityIsAddedToDb()) {
    										
    										alertSuccessMaker("Drug Restocked Successfully");
    										
    										ResultSet rs = handler.execQuery("SELECT * FROM drugs WHERE name = '"+drug.getValue()+"'");
    										try {
    											if(rs.first()) {
    												Maindrugscontroller.drugslist.add(new Drug(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("measure"), 
    												rs.getDouble("buyingprice"), rs.getDouble("sellingprice"), rs.getInt("quantity"), lu.getUsername()));
    											}
    										} catch (SQLException e) {
    											alertErrorMaker(e.getMessage());
    										}
    										
    										closee();
    										
    										

    										
    										
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
		
    	((Stage) drug.getScene().getWindow()).close();
		
	}



	private boolean userActivityIsAddedToDb() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', "
				+ "'restocked drug -"+drug.getValue()+"')")) {
			return true;
		}
    	alertErrorMaker("There was an error in activity");
    	return false;
	}



	private boolean expenseIsAddedToDb() {
		String drugName = drug.getValue();
		double drugQuantity = Double.parseDouble(quantity.getText());
		String drugMeasure = measure.getText();
		double drugBp = Double.parseDouble(bp.getText());
		double totalBp = drugBp * drugQuantity;
		String drugOtherCost = otherCost.getText();
		double drugTotalOtherCost = Double.parseDouble(totalOtherCost.getText());
		double drugTotalCost = (drugQuantity * drugBp) + drugTotalOtherCost;
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



	private boolean drugIsUpdatedToDb() {
		
		ResultSet rs = handler.execQuery("SELECT * FROM drugs WHERE name = '"+drug.getValue()+"'");
		try {
			if(rs.first()) {
				
				String drugName = drug.getValue();
				double drugQuantity = Double.parseDouble(quantity.getText());
				double quan = drugQuantity + rs.getDouble("quantity");
				double drugSp = Double.parseDouble(sp.getText());
				double drugBp = Double.parseDouble(bp.getText());
				
				int userid = lu.getId();
				
				String sql = "UPDATE `drugs` SET `buyingprice`='"+drugBp+"',"
								+ "`sellingprice`='"+drugSp+"',"
						+ "`quantity`='"+quan+"' WHERE name = '"+drugName+"'";
				
				if(handler.execAction(sql)) {
					
					return true;
					
				}
				alertErrorMaker("Error, drugs table");
				
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
		
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
		if(drug.getSelectionModel().isEmpty() || quantity.getText().isEmpty()
				|| measure.getText().isEmpty() || bp.getText().isEmpty() || supplier.getSelectionModel().isEmpty() || 
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
