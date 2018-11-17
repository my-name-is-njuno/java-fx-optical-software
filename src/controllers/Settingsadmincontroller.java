package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import accessories.Alertmaker;
import dbmanager.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import logged.in.user.User;
import tools.Insurance;

public class Settingsadmincontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private TableView<User> userTable;
    
    @FXML
    private JFXTextField searchValue;

    @FXML
    private TableColumn<User, String> usersNames;


    @FXML
    private ContextMenu contentMenu;

    @FXML
    private JFXListView<String> activityList;
    
    @FXML
    private JFXListView<String> userInfoo;
    
    LoggedinUser lu;
    
    Db handler;
    
    ObservableList<User> list = FXCollections.observableArrayList();
    ObservableList<String> activities = FXCollections.observableArrayList();
    ObservableList<String> userinfo = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	initCol();
    	
    	loadUserData();
    	
    	
		
	}
    
    
    

    private void initCol() {
		
    	usersNames.setCellValueFactory(new PropertyValueFactory<>("username"));
		
	}

	private void loadUserData() {
		
		ResultSet rs = handler.execQuery("SELECT * FROM users WHERE type != 1");
		ResultSet rsa = handler.execQuery("SELECT * FROM activities WHERE user_id = '"+lu.getId()+"' ORDER BY id DESC");
		try {
			while(rs.next()) {
				list.add(new User(rs.getString("name"), rs.getString("email"), rs.getString("password"), 
						rs.getString("department"), rs.getString("position"), rs.getString("info"),rs.getBoolean("active"),
						rs.getInt("addedby") , rs.getInt("id"), rs.getString("role")));
			}
			activities.clear();
			activities.add("Your Activities");
			while(rsa.next()){
				activities.add("	"+rsa.getString("activity"));
			}
			
			userinfo.clear();
			userinfo.add("Your info");
			userinfo.add("	Type : 	You are admin");
			userinfo.add("	Username : "+lu.getUsername());
			userinfo.add("	Your Eamil : "+lu.getEmail());
			
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
		
		
		userTable.setItems(list);
		activityList.getItems().setAll(activities);
		userInfoo.getItems().setAll(userinfo);
		
		
	}

	@FXML
    void activateUser(ActionEvent event) {
		
    }
	
	@FXML
    void checkIfUserHasBeenSelected(ContextMenuEvent event) {
			loadUserInfoToBothListviews();
    }
	
	
	
	private void loadUserInfoToBothListviews() {
		User selectedUser = userTable.getSelectionModel().getSelectedItem();
		if(selectedUser == null) {
			alertErrorMaker("No user selected");
		} else {
			activities.clear();
			userinfo.clear();
			userinfo.add(selectedUser.getUsername() + " further Info");
			userinfo.add("	Derpartment : " + selectedUser.getDerpartment());	
			userinfo.add("	Email : " + selectedUser.getEmail());
			userinfo.add("	Role : " + selectedUser.getRole());
			
			if(selectedUser.isActive()) {
				userinfo.add("	" + selectedUser.getUsername() + " is currently active");
			}
			
			
			try {
				ResultSet rsa = handler.execQuery("SELECT * FROM activities WHERE user_id = '"+selectedUser.getId()+"' ORDER BY id DESC");
				activities.add(selectedUser.getUsername() + " Activities");
				while(rsa.next()){
					activities.add("	"+rsa.getString("activity"));
				}
			} catch (SQLException e) {
				alertErrorMaker(e.getMessage());
			}
		}
		userInfoo.getItems().setAll(userinfo);
		activityList.getItems().setAll(activities);
		
	}




	@FXML
    void checkIfUserHasBeenSelectedd(MouseEvent event) {
		loadUserInfoToBothListviews();
    }
	
	
	
	
	
	@FXML
    void checkIfUserHasBeenSelecteddd(DragEvent event) {

    }

    @FXML
    void addNewUsers(ActionEvent event) {
    	
    	
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/adduser.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add new User");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			alertErrorMaker(e.getMessage());
		}
		
		

    }

    @FXML
    void editYourCreditials(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/editcrediatilas.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit your Crediatials");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			alertErrorMaker(e.getMessage());
		}
    }

    @FXML
    void loadUsers(ActionEvent event) {
    	list.clear();
    	loadUserData();
    }

    @FXML
    void viewUser(ActionEvent event) {

    }
    
    
    
    
    
    
    
    @FXML
    void searchUserByName(ActionEvent event) {
    	if(searchValue.getText().isEmpty()) {
    		Alertmaker.error("Searh value is empty");
    		loadUserData();
    		return;
    	}
    	
    	try {
    		ResultSet rs =handler.execQuery("SELECT * FROM users WHERE name LIKE '%"+searchValue.getText()+"%' ORDER BY id DESC");
    		
    		list.clear();
    		
			while(rs.next()) {
				

				list.add(new User(rs.getString("name"), rs.getString("email"), rs.getString("password"), 
						rs.getString("department"), rs.getString("position"), rs.getString("info"),rs.getBoolean("active"),
						rs.getInt("addedby") , rs.getInt("id"), rs.getString("role")));
					
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	userTable.setItems(list);
    }
    
    
    
    
    
    
    
    
    @FXML
    void changeRole(ActionEvent event) {
    	
    	User selectedUser = userTable.getSelectionModel().getSelectedItem();
		if(selectedUser == null) {
			alertErrorMaker("No user selected");
			return;
		}
		
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/edituserrole.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Edituserrole controller = fxmlloader.<Edituserrole>getController();
			controller.setUserToEdit(selectedUser);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit user role");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			alertErrorMaker(e.getMessage());
		}
    }
    
    
    
    @FXML
    void deactivate(ActionEvent event) {
    	
    }
    
    
    
    @FXML
    void makeadmin(ActionEvent event) {
    	
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

	public static void refresh() {
				
	}

	

}