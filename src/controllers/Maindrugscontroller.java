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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import logged.in.user.User;
import tools.Doctor;
import tools.Drug;

public class Maindrugscontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXTextField searchvalue;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TableView<Drug> table;

    @FXML
    private TableColumn<Drug, String> name;

    @FXML
    private TableColumn<Drug, Integer> quantity;

    @FXML
    private Button addNewDrug;

    @FXML
    private Button restockDrug;

    @FXML
    private ListView<String> druginfo;
    
    Db handler;
    
    LoggedinUser lu;
    
    static ObservableList<Drug> drugslist = FXCollections.observableArrayList();
    ObservableList<String> druginfomation = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	initCol();
    	
    	loadTable();
		
	}
    
    
    

    private void loadTable() {
    	
    	
    	
    	try {
    		ResultSet rs =handler.execQuery("SELECT * FROM drugs ORDER BY id DESC");
    		String addedBy = null;
    		drugslist.clear();
    		
			while(rs.next()) {
				int added = rs.getInt("addedby");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}

				drugslist.add(new Drug(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("measure"), 
						rs.getDouble("buyingprice"), rs.getDouble("sellingprice"), rs.getInt("quantity"), addedBy));
					
				
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
    	
    	
    	table.setItems(drugslist);
		
	}




	private void initCol() {
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	}
	
	
	
	
	
	@FXML
    void loadDrugInfo(MouseEvent event) {
		
		Drug selectedDrug = table.getSelectionModel().getSelectedItem();
		if(selectedDrug == null) {
			alertErrorMaker("No Drug selected");
		} else {
			druginfomation.clear();
			
			druginfomation.add(selectedDrug.getName() + " further Info");
			druginfomation.add("	Description : " + selectedDrug.getDescription());	
			druginfomation.add("	Available : " + selectedDrug.getQuantity() + " " + selectedDrug.getMeasure());
			druginfomation.add("	Buying Price : " + selectedDrug.getBuyingPice());	
			druginfomation.add("	Selling Price : " + selectedDrug.getSellingPrice());	
			
			
			
		}
		
		druginfo.getItems().setAll( druginfomation);

    }




	@FXML
    void addNew(ActionEvent event) {
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/adddrug.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add new Drug");
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
    void restock(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/restockdrug.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Restock Drug");
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
    void search(ActionEvent event) {
    	
    	
    	drugSearch();
    	
    }

    private void drugSearch() {
		
    	if(searchvalue.getText().isEmpty()) {
    		alertErrorMaker("Enter the keyword for searching");
    		loadTable();
    		return;
    	}

    	
    	
    	try {
    		ResultSet rs =handler.execQuery("SELECT * FROM drugs WHERE name LIKE '%"+searchvalue.getText()+"%' ORDER BY id DESC");
    		String addedBy = null;
    		drugslist.clear();
    		
			while(rs.next()) {
				int added = rs.getInt("addedby");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}

				drugslist.add(new Drug(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("measure"), 
						rs.getDouble("buyingprice"), rs.getDouble("sellingprice"), rs.getInt("quantity"), addedBy));
					
				
			}
		} catch (SQLException e) {
			alertErrorMaker(e.getMessage());
		}
    	table.setItems(drugslist);
		
	}



	@FXML
    void searchDrug(ActionEvent event) {
		drugSearch();
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
