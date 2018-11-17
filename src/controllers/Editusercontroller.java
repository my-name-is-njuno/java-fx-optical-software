package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;

public class Editusercontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXTextField userEmail;

    @FXML
    private JFXTextField userDerpartment;

    @FXML
    private JFXTextField userPosition;

    @FXML
    private JFXTextArea userInfo;

    @FXML
    private JFXPasswordField userPassword;

    @FXML
    private JFXPasswordField confirmPassword;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    LoggedinUser lu;
    Db handler;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	userName.setText(lu.getUsername());
    	userEmail.setText(lu.getEmail());
    	userDerpartment.setText(lu.getDerpartment());
    	userPosition.setText(lu.getPosition());
    	userPassword.setText(lu.getPassword());
    	confirmPassword.setText(lu.getPassword());
    	userInfo.setText(lu.getInfo());
		
	}

    @FXML
    void cancelOperation(ActionEvent event) {
    	((Stage) cancel.getScene().getWindow()).close();
    }

    @FXML
    void saveUser(ActionEvent event) {
    	
    	
    	if(filedsChanged()) {
    		if(fieldsNotEmpty()) {
    			
    			if(passwordsMatch()) {
    				
    				if(updateUser()) {
    					
    					alertSuccessMaker("Your Details updated Successfully");
    					clear();
    	    			
    	    		}
    			}
    			
    		}
    		
    	}
    	
    	
    }
    
    
    
    
    
    
    
    private void clear() {
		
		userName.setText("");
		userEmail.setText("");
		userPosition.setText("");
		userDerpartment.setText("");
		userPassword.setText("");
		userInfo.setText("");
		
		((Stage) userName.getScene().getWindow()).close();
		
		
		
	}

	private boolean updateUser() {
		String name = capitalizeWords(userName.getText());
		String email = capitalizeWords(userEmail.getText());
		String password = userPassword.getText();
		String derpartment = capitalizeWords(userDerpartment.getText());
		String position = capitalizeWords(userPosition.getText());
		String info = userInfo.getText();
		int addedby = lu.getId();
		if(handler.execAction("UPDATE users SET name = '"+name+"', email = '" +email+ "',"
				+ " password = '"+DigestUtils.sha1Hex(password)+"', department = '"+derpartment+"', position = '"+position+"',"
						+ " info = '"+info+"' WHERE id = '"+lu.getId()+"'" )) {
			
			
//			"UPDATE users SET name = '"+username.getText()+"', email = '"+ email.getText()+"',"
//		+ " password = '"+ DigestUtils.sha1Hex(password.getText()) +"' WHERE id = '"+lu.getId()+"'"
			
			
			lu.setDerpartment(derpartment);
			lu.setEmail(email);
			lu.setInfo(info);
			lu.setPassword(password);
			lu.setPosition(position);
			lu.setUsername(name);
			
			handler.setLu(lu);
			
			return true;
			
			
		}
		return false;
	}
	
	
	private String capitalizeWords(String str) {
		String newString = str.toLowerCase();
		return newString;
	}
	
	
	private boolean passwordsMatch() {
		if(userPassword.getText().equals(confirmPassword.getText())) {
			return true;
		}
		alertErrorMaker("Password do not match");
		return false;
	}

	private boolean filedsChanged() {
		if(userName.getText().equals(lu.getUsername()) && userEmail.getText().equals(lu.getEmail())
				&& userDerpartment.getText().equals(lu.getDerpartment()) && userPosition.getText().equals(lu.getPosition()) &&
				userPassword.getText().equals(lu.getPassword()) &&
				confirmPassword.getText().equals(lu.getPassword()) &&
				userInfo.getText().equals(lu.getInfo())
				) {
			alertErrorMaker("Nothing to update");
			return false;
		}
		return true;
	}
	
	
	private boolean fieldsNotEmpty() {
		if(userName.getText().isEmpty() || userEmail.getText().isEmpty() || userPassword.getText().isEmpty() ||
				userDerpartment.getText().isEmpty() || userPosition.getText().isEmpty() || userInfo.getText().isEmpty() ||
				confirmPassword.getText().isEmpty()) {
			alertErrorMaker("Ensure all fields are not empty");
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

