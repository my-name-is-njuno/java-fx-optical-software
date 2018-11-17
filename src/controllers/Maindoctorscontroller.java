package controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import logged.in.user.User;
import tools.Doctor;

public class Maindoctorscontroller implements Initializable {

    @FXML
    private StackPane root;
    
    @FXML
    private JFXButton refreshbut;

    @FXML
    private VBox header;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TableView<Doctor> table;

    @FXML
    private TableColumn<Doctor, String> docName;

    @FXML
    private TableColumn<Doctor, String> socSpeciality;

    @FXML
    private JFXButton newDocButton;

    @FXML
    private ListView<String> docInfo;
    
    
    Db handler;
    LoggedinUser lu;
    static ObservableList<Doctor> list = FXCollections.observableArrayList();
    ObservableList<String> infodoc = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		handler = Db.getInstance();
		lu = handler.getLu();
		initCol();
		loadDocData();
	}
    
    

    private void loadDocData() {
		
		list.clear();
		String username = "";
		try {
			ResultSet rs = handler.execQuery("SELECT * FROM doctors");
			while(rs.next()) {
				int addedby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT * FROM users WHERE id = '"+addedby+"'");
				if(user.next()) {
					username = user.getString("name");
				} 
				
				list.add(new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getBoolean("active"),
						String.valueOf(rs.getDate("created_at")), rs.getInt("patients"), username, rs.getString("contract") , rs.getString("email"),
						rs.getString("contact")));
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
		table.setItems(list);
	}
    
    
    
    @FXML
    void loadDoctorInfo(MouseEvent event) {
    	
    	Doctor selectedDoc = table.getSelectionModel().getSelectedItem();
		if(selectedDoc == null) {
			alertErrorMaker("No Doc selected");
		} else {
			
			infodoc.clear();
			infodoc.add(selectedDoc.getName() + " further Info");
			infodoc.add("	Email : " + selectedDoc.getEmail());
			infodoc.add("	Contact : " + selectedDoc.getContact());
			infodoc.add("	Speciality : " + selectedDoc.getSpeciality());
			infodoc.add("	Job Type : " + selectedDoc.getContract());
			infodoc.add("	No. of Patients : " + selectedDoc.getPatients());
			if(selectedDoc.isActive()) {
				infodoc.add("	" + selectedDoc.getName() + " is currently active");
			}
			
		}
		docInfo.getItems().setAll(infodoc);
		

    }



	private void initCol() {
		
		docName.setCellValueFactory(new PropertyValueFactory<>("name"));
		socSpeciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));
		
	}



	@FXML
    void addNewDoctor(ActionEvent event) {
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/adddoctor.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add Doctor");
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
    void searchDoc(ActionEvent event) {
    	if(searchValue.getText().isEmpty()) {
    		alertErrorMaker("Enter a name to search with");
    		return;
    	}
    	String value = searchValue.getText();
    	
    	ResultSet rs = handler.execQuery("SELECT * FROM doctors WHERE name LIKE '%"+value+"%'");
    	String username = null;
    	try {
    		list.clear();
    		
			while(rs.next()) {
				int addedby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT * FROM users WHERE id = '"+addedby+"'");
				if(user.next()) {
					username = user.getString("name");
				} 
				
				list.add(new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getBoolean("active"),
						String.valueOf(rs.getDate("created_at")), rs.getInt("patients"), username, rs.getString("contract") , rs.getString("email"),
						rs.getString("contact")));
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
    	table.setItems(list);
    }
    
    
    
    
    
    @FXML
    void refresh(ActionEvent event) {
    	loadDocData();
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
