package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Button;
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
import tools.Drug;
import tools.Insurance;

public class Maininsurancecontroller implements Initializable{

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private Button newInsurance;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private TableView<Insurance> table;

    @FXML
    private TableColumn<Insurance, String> insuranceName;

    @FXML
    private JFXButton stats;

    @FXML
    private JFXButton stament;

    @FXML
    private JFXButton payments;

    @FXML
    private JFXButton remittance;

    @FXML
    private JFXButton recon;

    @FXML
    private JFXListView<String> insuranceInfo;

    @FXML
    private Button toPdf;

    @FXML
    private Button toExcel;
    
    
    Db handler;
    LoggedinUser lu;
    static ObservableList<Insurance> list = FXCollections.observableArrayList();
    
    static ObservableList<String> listViewInfo = FXCollections.observableArrayList();
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
		lu = handler.getLu();
		
		initCol();
		
		loadData();
		
	}
    
    

    private void loadData() {
    	
    	ResultSet rs = handler.execQuery("SELECT * FROM insurances");
    	String addedBy = null;
		list.clear();
		
		try {
		
			while(rs.next()) {
				int added = rs.getInt("created_by");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}
				list.add(new Insurance(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("contact"), 
						rs.getString("contact_person"), rs.getString("adress"), rs.getBoolean("active"), addedBy))	;
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
		table.setItems(list);
   	
	}



	private void initCol() {
		
		insuranceName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
	}



	

    @FXML
    void loadNewInsuranceWindow(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addinsurance.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Add a new Insurance");
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
    void loadInsuranceInfo(MouseEvent event) {
    	Insurance selectedIns = table.getSelectionModel().getSelectedItem();
		if(selectedIns == null) {
			Alertmaker.error("No Insurance selected");
		} else {
			
			listViewInfo.clear();
			listViewInfo.add(selectedIns.getName() + " further Info");
			listViewInfo.add("	Email : " + selectedIns.getEmail());
			listViewInfo.add("	Contact : " + selectedIns.getContact());
			listViewInfo.add("	Contact Person : " + selectedIns.getContactPerson());
			listViewInfo.add("	Adress : " + selectedIns.getAdress());
			listViewInfo.add("	Added By : " + selectedIns.getAddedBy());
			
			
			if(selectedIns.isActive()) {
				listViewInfo.add("	" + selectedIns.getName() + " is currently active");
			}
			
		}
		insuranceInfo.getItems().setAll(listViewInfo);
    }
    

    

    @FXML
    void searchInsurance(ActionEvent event) {
    	
    	if(searchValue.getText().isEmpty()) {
    		Alertmaker.error("Searh value is empty");
    		loadData();
    		return;
    	}
    	
    	try {
    		ResultSet rs =handler.execQuery("SELECT * FROM insurances WHERE name LIKE '%"+searchValue.getText()+"%' ORDER BY id DESC");
    		String addedBy = null;
    		list.clear();
    		
			while(rs.next()) {
				int added = rs.getInt("created_by");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}

				list.add(new Insurance(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("contact"), 
						rs.getString("contact_person"), rs.getString("adress"), rs.getBoolean("active"), addedBy));
					
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	table.setItems(list);
    	
    	

    }
    
    
    @FXML
    void loadPaymentsInfo(ActionEvent event) {

    }

    @FXML
    void loadReconInfo(ActionEvent event) {

    }

    @FXML
    void loadRemittancesInfo(ActionEvent event) {

    }

    @FXML
    void loadStatementInfo(ActionEvent event) {

    }

    @FXML
    void loadStatsInfo(ActionEvent event) {

    }
    
    
    
    @FXML
    void extractToExcel(ActionEvent event) {

    }

    @FXML
    void extractToPdf(ActionEvent event) {

    }

	

}
