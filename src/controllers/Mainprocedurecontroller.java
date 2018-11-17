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
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import tools.Doctor;
import tools.Procedure;

public class Mainprocedurecontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXTextField serachValue;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TableView<Procedure> table;

    @FXML
    private TableColumn<Procedure, String> procedure;

    @FXML
    private TableColumn<Procedure, String> procedureCost;

    @FXML
    private MenuItem edit;

    @FXML
    private MenuItem delete;

    @FXML
    private Button addNew;
    
    @FXML
    private ListView<String> procedureInfo;
    
    
    ObservableList<String> prInfo = FXCollections.observableArrayList();
    static ObservableList<Procedure> list = FXCollections.observableArrayList();
    
    LoggedinUser lu;
    
    Db handler;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	
    	initCol();
    	
    	
    	loadTableWithInfo();
    	
	}
    
    

    

    private void loadTableWithInfo() {
    	
    	list.clear();
		
    	ResultSet rs = handler.execQuery("SELECT * FROM procedures");
    	
    	try {
    		String addedBy = null;
    		
			while(rs.next()) {
				int addby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT name FROM users WHERE id = '"+addby+ "'");
				if(user.first()) {
					addedBy = user.getString("name");
				}
				list.add(new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
						rs.getDouble("charge"), addedBy));
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	table.setItems(list);
		
	}





	private void initCol() {
		
		procedure.setCellValueFactory(new PropertyValueFactory<>("name"));
		procedureCost.setCellValueFactory(new PropertyValueFactory<>("charge"));
	}





	@FXML
    void addNewProcedure(ActionEvent event) {
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addprocedure.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add Procedure");
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
    void deleteProcedure(ActionEvent event) {

    }

    @FXML
    void loadEditWindow(ActionEvent event) {

    }

    @FXML
    void searchProcedure(ActionEvent event) {
    	
    	if(serachValue.getText().isEmpty()) {
    		Alertmaker.error("Search value is Empty");
    		loadTableWithInfo() ;
    		return;
    	}
    	list.clear();
		
    	ResultSet rs = handler.execQuery("SELECT * FROM procedures WHERE name LIKE '%"  +serachValue.getText()+ "%'");
    	
    	try {
    		String addedBy = null;
    		
			while(rs.next()) {
				int addby = rs.getInt("created_by");
				ResultSet user = handler.execQuery("SELECT name FROM users WHERE id = '"+addby+ "'");
				if(user.first()) {
					addedBy = user.getString("name");
				}
				list.add(new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("description"), 
						rs.getDouble("charge"), addedBy));
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	table.setItems(list);		
    }
    
    
    
    
    
    
    @FXML
    void loadProcedureInfo(MouseEvent event) {
    	addProcedureInfoToListView();
    }





	private void addProcedureInfoToListView() {

		
		
		Procedure selectedProc = table.getSelectionModel().getSelectedItem();
		if(selectedProc == null)
		{
			Alertmaker.error("Procedure Selected");
			return;
		}
		prInfo.clear();
		prInfo.add(selectedProc.getName() + " further info");
		prInfo.add("	Procedure Cost: "+ selectedProc.getCharge());
		prInfo.add("	Procedure Description: "+ selectedProc.getDescription());
		prInfo.add("	Added By: "+ selectedProc.getCreatedBy());
		
		procedureInfo.setItems(prInfo);
		
		
	}

	

}
