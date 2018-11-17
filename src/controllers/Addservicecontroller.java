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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;
import tools.Procedure;
import tools.Service;

public class Addservicecontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField cost;

    @FXML
    private JFXTextArea description;

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
		
		((Stage) root.getScene().getWindow()).close();
		
	}

    @FXML
    void saveService(ActionEvent event) {
    	if(emptyFields()) {
    		if(costIsDouble()) {
    			if(saveNewService()) {
    				if(addedNewUserActivity()) {
    					if(updateServiceList()) {
    						Alertmaker.success("Service added successfully");
    						closee();
    					}
    				}
    			}
    		}
    	}
    }
    
    




	private boolean updateServiceList() {
		ResultSet rs = handler.execQuery("SELECT * FROM services WHERE name = '" +name.getText()+ "'");
		String addedby = null;
		try {
			if(rs.first()) {
				int addby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT name FROM users WHERE id = '"+addby+ "'");
				if(user.first()) {
					addedby = user.getString("name");
				}
				Mainservicescontroller.list.add(new Service(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
						addedby, rs.getDouble("cost")));
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}




	private boolean addedNewUserActivity() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new Service "+name.getText()+"')")) {
			return true;
		}
    	Alertmaker.error("There was an error in recording user activity");
    	return false;
	}




	private boolean saveNewService() {
		
		if (handler.execAction("INSERT INTO SERVICES (name,description,cost,created_by) VALUES ("
				+ "'"+name.getText()+"',"
				+ "'"+description.getText()+"',"
						+ "'"+Double.parseDouble(cost.getText())+"',"
								+ "'"+lu.getId()+"'"
				+ ")")) {
			
			return true;
		}
		Alertmaker.error("Error in saving service, try again");
		return false;
	}




	private boolean costIsDouble() {
		try {
			
			Double.parseDouble(cost.getText());
			return true;
					
			
		} catch(Exception e) {
			Alertmaker.error("Cost must be a number");
			return false;
			
		}
		
	}




	private boolean emptyFields() {
		if(name.getText().isEmpty() || description.getText().isEmpty() || cost.getText().isEmpty()) {
			Alertmaker.error("Ensure all fields are filled");
			return false;
		}
		return true;
	}

	

}
