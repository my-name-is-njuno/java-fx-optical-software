package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logged.in.user.User;

public class Edituserrole implements Initializable {

	@FXML
    private AnchorPane root;

    @FXML
    private JFXComboBox<String> role;

    @FXML
    private VBox header;

    @FXML
    private Label headerText;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;

    @FXML
    private Text currentRole;

    
    User usr;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	headerText.setText("Edit " + usr.getUsername() + " role");
    	
		
	}

    @FXML
    void cancelOperation(ActionEvent event) {
    	closee();
    }

    private void closee() {
		
    	((Stage) root.getScene().getWindow()).close();
		
	}

	@FXML
    void updateUserRole(ActionEvent event) {
		
    }

	

	public void setUserToEdit(User selectedUser) {
		
		usr = selectedUser;
		
	}

}
