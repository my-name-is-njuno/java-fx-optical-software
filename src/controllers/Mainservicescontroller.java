package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import tools.Service;

public class Mainservicescontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TableView<Service> serviceTable;

    @FXML
    private TableColumn<Service, String> service;

    @FXML
    private TableColumn<Service, String> description;

    @FXML
    private TableColumn<Service, String> cost;

    @FXML
    private MenuItem editService;

    @FXML
    private MenuItem delete;

    @FXML
    private Button addNewService;
    
    
    Db handler;
    LoggedinUser lu;
    static ObservableList<Service> list = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	handler = Db.getInstance();
		lu = handler.getLu();
		initCol();
		loadTable();
	}


    private void loadTable() {
		
    	list.clear();
    	ResultSet rs = handler.execQuery("SELECT services.id, services.name, services.description, services.cost, "
    			+ " users.name as username FROM services INNER JOIN users ON services.created_by = users.id  ORDER BY id DESC");
    	
    	try {
			while(rs.next()) {
				
				list.add(new Service(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
						rs.getString("username"), rs.getDouble("cost")));
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	serviceTable.setItems(list);
		
	}


	private void initCol() {
		service.setCellValueFactory(new PropertyValueFactory<>("name"));
    	cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    	description.setCellValueFactory(new PropertyValueFactory<>("description"));
	}


	@FXML
    void deleteService(ActionEvent event) {

    }

    @FXML
    void loadEditWindow(ActionEvent event) {

    }

    @FXML
    void saveNewService(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addservices.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add New Service");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			Alertmaker.error(e.getMessage());
		}
    }

    @FXML
    void searchService(ActionEvent event) {
    	
    	if(searchValue.getText().isEmpty()) {
    		Alertmaker.error("Search value is empty");
    		loadTable();
    		return ;
    	}
    	list.clear();
    	ResultSet rs = handler.execQuery("SELECT services.id, services.name, services.description, services.cost, "
    			+ " users.name as username FROM services INNER JOIN users ON services.created_by = users.id WHERE "
    			+ " services.name LIKE '%"+searchValue.getText()+"%' ORDER BY services.id DESC");
    	
    	try {
			while(rs.next()) {
				
				list.add(new Service(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
						rs.getString("username"), rs.getDouble("cost")));
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	serviceTable.setItems(list);
    }

	

}
