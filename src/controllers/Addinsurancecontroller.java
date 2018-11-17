package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import accessories.Alertmaker;
import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;
import tools.Doctor;
import tools.Insurance;

public class Addinsurancecontroller implements Initializable {

		@FXML
	    private AnchorPane root;

	    @FXML
	    private JFXTextField email;

	    @FXML
	    private JFXTextField name;

	    @FXML
	    private JFXTextField contact;

	    @FXML
	    private JFXTextField contactPerson;

	    @FXML
	    private JFXTextField adress;

	    @FXML
	    private VBox header;

	    @FXML
	    private JFXButton addService;

	    @FXML
	    private JFXButton cancel;

    
    Db handler;
    LoggedinUser lu;
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
		lu = handler.getLu();
		
	}
    

    @FXML
    void cancelOperation(ActionEvent event) {
    	closee();
    }
    

    private void closee() {
    	
    	((Stage) header.getScene().getWindow()).close();
		
	}


	@FXML
    void saveInsurance(ActionEvent event) {
		if(fieldsNotEmpty()) {
			if(addInsurance()) {
				if(addUserActivity()) {
					
					Alertmaker.success("Insurance Added successfully");
					
					updateInsurancelist();
					
        	    	closee();
        	    	
        	    	
				}
			}
		}
    }
	
	
	
	void updateInsurancelist() {
		ResultSet rs = handler.execQuery("SELECT * FROM insurances WHERE name = '"+name.getText()+"'");
		String username = null;
    	try {
    		
    		
			while(rs.next()) {
				int addedby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT * FROM users WHERE id = '"+addedby+"'");
				if(user.next()) {
					username = user.getString("name");
				} 
				
				Maininsurancecontroller.list.add(new Insurance(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("contact"), 
						rs.getString("contact_person"), rs.getString("adress"), rs.getBoolean("active"), username));
				
				Addinsurancepatientcontroller.inslist.add(rs.getString("name"));
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
	}
	

	private boolean addUserActivity() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added Insurance: "+name.getText()+"')")) {
			return true;
		}
		Alertmaker.error("Error adding user Activity");
		return false;
	}


	private boolean addInsurance() {
		if(handler.execAction("INSERT INTO insurances (name, email, contact, contact_person, adress, created_by) VALUES ("
				+ "'"+name.getText()+"',"
						+ "'"+email.getText()+"',"
								+ "'"+contact.getText()+"',"
										+ "'"+contactPerson.getText()+"',"
												+ "'"+adress.getText()+"',"
														+ "'"+lu.getId()+"'"
				+ ")")) {
			return true;
		}
		Alertmaker.error("Error saving Insurance");
		return false;
	}


	private boolean fieldsNotEmpty() {
		if(name.getText().isEmpty() || email.getText().isEmpty() || contact.getText().isEmpty()
				|| contactPerson.getText().isEmpty() || adress.getText().isEmpty()) {
			Alertmaker.error("Make sure all fields are not empty");
			return false;
		}
		return true;
	}

	

}
