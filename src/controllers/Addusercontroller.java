package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;

public class Addusercontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXTextField userEmail;

    @FXML
    private JFXTextField userDerpartment;
    
    @FXML
    private JFXPasswordField confirmPassword;

    @FXML
    private JFXTextField userPosition;

    @FXML
    private JFXTextArea userInfo;

    @FXML
    private JFXPasswordField userPassword;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    @FXML
    private JFXComboBox<String> role;
    
    
    
    Db handler;
    LoggedinUser lu;
    ObservableList<String> roleList = FXCollections.observableArrayList("Reception", "Lab", "Pharmacy", "Operating Room");
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	lu = handler.getLu();
    	role.setItems(roleList);
		
	}

    @FXML
    void cancelOperation(ActionEvent event) {
    	closeee();	
    }

    private void closeee() {
    	((Stage) root.getScene().getWindow()).close();		
	}

	@FXML
    void saveUser(ActionEvent event) {
    	
    	if(fieldsNotEmpty()) {
    		if(passwordsMatch()) {
    			if(userAddedToDb()) {
        			alertSuccessMaker("User Successfully created");
        			clear();
        		}
    		}
    		
    	}

    }

	private boolean passwordsMatch() {
		if(userPassword.getText().equals(confirmPassword.getText())) {
			return true;
		}
		alertErrorMaker("Password do not match");
		return false;
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

	private boolean userAddedToDb() {
		
		String name = capitalizeWords(userName.getText());
		String email = capitalizeWords(userEmail.getText());
		String password = userPassword.getText();
		String derpartment = capitalizeWords(userDerpartment.getText());
		String position = capitalizeWords(userPosition.getText());
		String info = userInfo.getText();
		String urole = role.getValue();
		int addedby = lu.getId();
		if(handler.execAction("INSERT INTO users (name, email, password, department, position, info, addedby, role) VALUES ("
				+ "'"+name+"',"
				+ "'"+email+"',"
				+ "'"+DigestUtils.sha1Hex(password)+"',"
				+ "'"+derpartment+"',"
				+ "'"+position+"',"
				+ "'"+info+"',"
				+ "'"+addedby+"',"
				+ "'"+role+"'"
				+ ")")) {
			if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added "+name+" - new user')")) {
				return true;
			}
			
			
		}
		return false;
	}

	private boolean fieldsNotEmpty() {
		if(userName.getText().isEmpty() || userEmail.getText().isEmpty() || userPassword.getText().isEmpty() ||
				userDerpartment.getText().isEmpty() || userPosition.getText().isEmpty() || userInfo.getText().isEmpty() ||
				confirmPassword.getText().isEmpty() || role.getValue().isEmpty()) {
			alertErrorMaker("Ensure all fields are not empty");
			return false;
		}
		return true;
	}
	
	
	private String capitalizeWords(String str) {
		String newString = str.toLowerCase();
		return newString;
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