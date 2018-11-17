package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;
import tools.Doctor;

public class Adddoctorcontroller implements Initializable {

    @FXML
    private AnchorPane root;
    
    

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXTextField speciality;

    @FXML
    private JFXComboBox<String> contract;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    
    String docName;
    
    
    
    Db handler;
    LoggedinUser lu;
    ObservableList<String> contractstypes = FXCollections.observableArrayList("Full Time", "Part Time", "Short Time", 
    		"Attachment");
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	handler = Db.getInstance();
		lu = handler.getLu();
		contract.setItems(contractstypes);
		
	}
    
    

    @FXML
    void cancelOperation(ActionEvent event) {
    	((Stage) name.getScene().getWindow()).close();
    }

    @FXML
    void saveDoctor(ActionEvent event) {
    	
    	if(fieldsNotEmpty()) {
    		
    			if(doctorAddedToDb()) {
    				
    				clear();
        			
        			close();
        			
        			ResultSet rs = handler.execQuery("SELECT * FROM doctors WHERE name = '"+docName+"'");
        			String username = null;
        	    	try {
        	    		
        	    		
        				while(rs.next()) {
        					int addedby = rs.getInt("created_by");
        					ResultSet user = handler.execQuery("SELECT * FROM users WHERE id = '"+addedby+"'");
        					if(user.next()) {
        						username = user.getString("name");
        					} 
        					
        					Maindoctorscontroller.list.add(new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getBoolean("active"),
        							String.valueOf(rs.getDate("created_at")), rs.getInt("patients"), username, rs.getString("contract") , rs.getString("email"),
        							rs.getString("contact")));
        				}
        			} catch (SQLException e) {
        				alertErrorMaker(e.getMessage());
        			}
        		}
    		
    		
    	}

    }
    
    
    
    
   
    

    
    
    private boolean fieldsNotEmpty() {
		if(name.getText().isEmpty() || email.getText().isEmpty() || contact.getText().isEmpty() || contract.getValue().isEmpty()
				|| speciality.getText().isEmpty()) {
			alertErrorMaker("Make sure there are no empty fields");
			return false;
		}
		return true;
	}



	private boolean doctorAddedToDb() {
		docName = name.getText().toUpperCase();
		String docEmail = email.getText().toLowerCase();
		String docContact = contact.getText().toLowerCase();
		String docContract = contract.getValue().toUpperCase();
		String docSpeciality = speciality.getText().toUpperCase();
		
		if(handler.execAction("INSERT INTO doctors (name,email,contact,contract,type,created_by) VALUES ("
				+ "'"+docName+"',"
				+ "'"+docEmail+"',"
				+ "'"+docContact+"',"
				+ "'"+docContract+"',"
				+ "'"+docSpeciality+"',"
				+ "'"+lu.getId()+"'"
				+ ")")) {
			
			if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added "+docName+" - new Doctor')")) {
				alertSuccessMaker("Doctor " + docName + "Added successfully to db");
				return true;
			}
			
			
			
			
			
			
			
		}
		
		
		return false;
	}



	private void close() {
		
		((Stage) name.getScene().getWindow()).close();
		
	}



	private void clear() {
		
		name.setText("");
		email.setText("");
		contact.setText("");
		contract.setValue("");
		speciality.setText("");
		
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
