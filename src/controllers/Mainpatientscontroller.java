package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import tools.Patient;
import tools.Tool;

public class Mainpatientscontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXButton newVisit;

    @FXML
    private JFXListView<String> patientInfo;

    @FXML
    private JFXButton extract;

    @FXML
    private JFXListView<String> patientHistory;

    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> name;

    @FXML
    private TableColumn<Patient, String> dob;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private Button newPatientWithoutInsurance;

    @FXML
    private Button newPatientInsurance;
    
    
    
    
    Db handler;
    
    LoggedinUser lu;
    
    static ObservableList<Patient> list = FXCollections.observableArrayList();
    
    static ObservableList<String> pinfo = FXCollections.observableArrayList();
    
    static ObservableList<String> phistory = FXCollections.observableArrayList();
    
    
    
    
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	loadTable();
    	
    	initCol();
    	
    	loadPatientInfoOnListView();
    	
		
	}
    
    
    
    
    
    

    private void initCol() {
		
    	name.setCellValueFactory(new PropertyValueFactory<>("name"));
		dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
		
	}




    @FXML
    void loadlistviewinfo(MouseEvent event) {
    	loadPatientInfoOnListView();
    }


	private void loadTable() {
		
		
		try {
    		ResultSet rs =handler.execQuery("SELECT * FROM patients ORDER BY id DESC LIMIT 50");
    		String addedBy = null;
    		list.clear();
    		String strDate = null;
    		String insstatus = null;
    		
			while(rs.next()) {
				int added = rs.getInt("created_by");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}
				
				Date dt = rs.getDate("dob");
				SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy");
				strDate = fmt.format(dt);
				
				if(rs.getBoolean("withins")) {
					insstatus = "Insurance Patient";
				} else {
					insstatus = "No insurance";
				}
				

				list.add(new Patient(rs.getInt("id"), rs.getString("name"),  addedBy, rs.getString("contact"), 
						rs.getString("email"), strDate, rs.getString("adress"), insstatus));
					
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	
    	table.setItems(list);
		
	}







	@FXML
    void newWithInsurance(ActionEvent event) {
		
		try {
			
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addinsurancepatient.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("New Insurance Patient");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			Alertmaker.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    
    
    

    @FXML
    void newWithoutInsurance(ActionEvent event) {
    	try {
			
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addpatient.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("New Patient without Insurance");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(header.getScene().getWindow());
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (IOException e) {
			Alertmaker.error(e.getMessage());
			e.printStackTrace();
		}
    }

    @FXML
    void searchPatient(ActionEvent event) {
    	
    }

    @FXML
    void toPdf(ActionEvent event) {

    }

    @FXML
    void visitNew(ActionEvent event) {

    }
    
    
    
    
    
    
    
    
    
    
    
    private void loadPatientInfoOnListView() {
		Patient selectedPatient = table.getSelectionModel().getSelectedItem();
		if(selectedPatient == null) {
			pinfo.clear();
			pinfo.add("No Patient Selected");
		} else {
			pinfo.clear();
			
			pinfo.add(selectedPatient.getName() + " further Info");
			pinfo.add("	Full Name : " + selectedPatient.getName());	
//			pinfo.add("	Age : " + selectedPatient.calcualteAge());	
			pinfo.add("	From : " + selectedPatient.getAdress());
			pinfo.add("	Contact : " + selectedPatient.getContact());	
			pinfo.add("	Email : " + selectedPatient.getEmail());	
			pinfo.add("	Insurance Status : " + selectedPatient.getWithins());
			
			if(selectedPatient.getWithins() == "Insurance Patient") {
				ResultSet rs = handler.execQuery("SELECT patientinsurance.scheme, patientinsurance.member_number,"
						+ " insurances.name FROM patientinsurance inner join insurances on patientinsurance.insurance_id = insurances.id"
						+ " WHERE patientinsurance.patient_id = '"+selectedPatient.getId()+"'");
				try {
					if(rs.last()) {
						pinfo.add(selectedPatient.getName() + " Insurance Info");
						pinfo.add("	Insurance Name : " + rs.getString("name"));	
						pinfo.add("	Company / Scheme : " + rs.getString("scheme"));	
						pinfo.add("	Insurance Member Number : " + rs.getString("member_number"));	
					}
				} catch (SQLException e) {
					Alertmaker.error(e.getMessage());
				}
			}
			
			
		}
		
		patientInfo.getItems().setAll(pinfo);
	}
    
    
    
    
    
    
    private void loadPatientHistoryInfoOnListView() {
    	
    	Patient selectedPatient = table.getSelectionModel().getSelectedItem();
		if(selectedPatient == null) {
			pinfo.clear();
			pinfo.add("No Patient Selected");
		} else {
			pinfo.clear();
			
			pinfo.add(selectedPatient.getName() + " further Info");
			pinfo.add("	Full Name : " + selectedPatient.getName());	
			pinfo.add("	From : " + selectedPatient.getAdress());
			pinfo.add("	Contact : " + selectedPatient.getContact());	
			pinfo.add("	Email : " + selectedPatient.getEmail());	
			
			
			
		}
		
		patientHistory.getItems().setAll(pinfo);
		
		
	}





	

}
