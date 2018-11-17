package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;

public class Addsuppliercontroller implements Initializable {
	
//	supplier management
	
	
	
	@FXML
    private JFXTextField supplierName;

    @FXML
    private JFXTextField supplierContact;

    @FXML
    private JFXTextField supplierEmail;

    @FXML
    private JFXTextField supplierAdress;
    
    @FXML
    private JFXButton addsup;

    @FXML
    private JFXButton cancelsup;
    
    LoggedinUser lu;
    
    Db handler;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
	}

   

    @FXML
    void saveSupplier(ActionEvent event) {
    	if(supplierfieldsnotempty()) {
    		if(addsuppliertodb()) {
    			close();
    			alertSuccessMaker("Supplier Added Successfully");
    		}
    	}
    }

    
    private void close() {
    	((Stage) supplierName.getScene().getWindow()).close();
	}



	private boolean addsuppliertodb() {
    	
    	if(handler.execAction("INSERT INTO suppliers (name,contact,email,adress,created_by) VALUES ("
    			+ "'"+supplierName.getText()+"',"
    					+ "'"+supplierContact.getText()+"',"
    							+ "'"+supplierEmail.getText()+"',"
    									+ "'"+supplierAdress.getText()+"',"
    											+ "'"+lu.getId()+"'"
    			+ ")")) {
    		
    		
    		
    		Adddrugscontroller.supplierslist.add(supplierName.getText());
    		Mainequipmentscontroller.supplierslist.add(supplierName.getText());
    		
    		return true;
    		
    	}
		return false;
    	
    	
	}

	private boolean supplierfieldsnotempty() {
		if(supplierName.getText().isEmpty() || supplierContact.getText().isEmpty() || supplierEmail.getText().isEmpty()
				 || supplierAdress.getText().isEmpty()) {
			alertErrorMaker("Ensure all fields are not empty");
    		return false;
    	}
		return true;
	}
	
	
	
	
	
	@FXML
    void cancelsupoper(ActionEvent event) {
		((Stage) supplierName.getScene().getWindow()).close();
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
