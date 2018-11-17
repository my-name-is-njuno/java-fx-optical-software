package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;

public class Logincontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;
    
    Db handler;
    
    LoggedinUser lu;

   
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
		
	}
    
    
    @FXML
    void cancelLogin(ActionEvent event) {
    	
    	((Stage) root.getScene().getWindow()).close();	
    	
    }

    @FXML
    void loginUser(ActionEvent event) {
    	
    	if(fieldsNotEmpty()) {
    		if(nameExist()) {
    			if(nameAndPasswordMatch()) {
    				closeLoginWindow();
    				loadMainWindow();    				
    			}
    		}
    	}

    }


	


	private void closeLoginWindow() {
		((Stage) root.getScene().getWindow()).close();	
	}


	private void loadMainWindow() {
		
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/mainwindow.fxml"));
			Parent root = (Parent)fxmlloader.load();
			
			Stage stage = new Stage(StageStyle.DECORATED);
//			stage.setMaximized(true);
//			stage.setFullScreen(true);
			stage.setTitle("OSA: "+ lu.getUsername());
			stage.setScene(new Scene(root));
			stage.show();
			

			
		} catch (IOException e) {
			alertErrorMaker(e.getMessage());
		}
		
	}


	private boolean nameAndPasswordMatch() {
		
		String name = username.getText();
		String userpassword = DigestUtils.sha1Hex(password.getText());
		
		ResultSet rs = handler.execQuery("SELECT * FROM users WHERE name = '"+name+"' AND password = '" +userpassword+ "'");
		
//		ResultSet rs = handler.execQuery("SELECT * FROM users WHERE name = '" + username.getText() + "' AND password = '" + DigestUtils.sha1Hex(password.getText()) + "'");
		try {
			if (rs.next()) {
				lu = new LoggedinUser(rs.getInt("id"), rs.getString("name"), rs.getString("email"), password.getText(), rs.getBoolean("type"), 
						rs.getString("department"), rs.getString("position"), rs.getString("info"),
						rs.getBoolean("active"), rs.getDate("created_at"));
				handler.setLu(lu);
				return true;
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
		alertErrorMaker("Wrong username / Password combination");
		return false;
	}

	private boolean nameExist() {
		
		ResultSet rs = handler.execQuery("SELECT * FROM users WHERE name = '" + username.getText() + "'");
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
		alertErrorMaker("Username entered is wrong");
		return false;
		
	}

	private boolean fieldsNotEmpty() {
		
		if(username.getText().isEmpty() || password.getText().isEmpty()) {
			
			alertErrorMaker("Make sure all fileds are not empty");
			
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

	
