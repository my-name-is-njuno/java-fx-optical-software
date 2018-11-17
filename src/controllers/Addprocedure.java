package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import tools.Procedure;

public class Addprocedure implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField charge;

    @FXML
    private JFXTextArea description;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

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

    @FXML
    void saveProcedure(ActionEvent event) {
    	
    	if(fieldsNotEmpty()) {
    		if(chargeFieldIsDouble()) {
    			if(procedureIsDead()) {
    				if(userActivityAdded()) {
    					Alertmaker.success("Procedure Added Successfully");
    					ResultSet rs = handler.execQuery("SELECT * FROM procedures WHERE name = '" +name.getText()+ "'");
    					String addedby = null;
    					try {
							if(rs.first()) {
								int addby = rs.getInt("created_by");
								ResultSet user = handler.execQuery("SELECT name FROM users WHERE id = '"+addby+ "'");
								if(user.first()) {
									addedby = user.getString("name");
								}
								Mainprocedurecontroller.list.add(new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
										rs.getDouble("charge"), addedby));
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					
    					closee();
    				}
    			}
    		}
    	}

    }

	private void closee() {
		
		((Stage) root.getScene().getWindow()).close();
		
	}

	private boolean userActivityAdded() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new Procedure "+name.getText()+"')")) {
			return true;
		}
    	Alertmaker.error("There was an error in activity");
    	return false;
	}

	private boolean procedureIsDead() {
		if(handler.execAction("INSERT INTO procedures (name,description,charge,created_by) VALUES ("
				+ "'"+name.getText()+"',"
						+ "'"+description.getText()+"',"
								+ "'"+Double.parseDouble(charge.getText())+"',"
										+ "'"+lu.getId()+"'"
				+ ")")) {
			
			return true;
			
		}
		return false;
	}

	private boolean chargeFieldIsDouble() {
		try {
			
			Double.parseDouble(charge.getText());
			
			return true;
			
		} catch(Exception e) {
			
			Alertmaker.error("The charge field should be a number");
			
			return false;
		}
	}

	private boolean fieldsNotEmpty() {
		if(name.getText().isEmpty() || charge.getText().isEmpty() || description.getText().isEmpty()) {
			Alertmaker.error("Ensure all fields are filled");
			return false;
		}
		return true;
	}

	

}
