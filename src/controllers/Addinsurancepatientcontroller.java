package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;
import tools.Insurance;
import tools.Patient;

public class Addinsurancepatientcontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXTextField adress;

    @FXML
    private JFXTextField insmemid;

    @FXML
    private JFXTextField company;

    @FXML
    private DatePicker dob;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXComboBox<String> insurance;

    @FXML
    private JFXCheckBox smartAvailable;

    @FXML
    private JFXTextField smartId;

    @FXML
    private Button newIns;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    Db handler;
    
    LoggedinUser lu;
    
    static ObservableList<String> inslist = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	handler = Db.getInstance();
    	lu = handler.getLu();
    	loadInsuranceList();
	}
    
    
    

    private void loadInsuranceList() {
		
    	ResultSet rs = handler.execQuery("SELECT * FROM insurances");
    	String addedBy = null;
		inslist.clear();
		
		try {
		
			while(rs.next()) {
				
				inslist.add(rs.getString("name"))	;
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
		
		
		insurance.setItems(inslist);
		
	}




	@FXML
    void addNewIns(ActionEvent event) {
		try {
			
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addinsurance.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("New Insurance");
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
    void cancelOperation(ActionEvent event) {
    	closee();
    }

    @FXML
    void savePatient(ActionEvent event) {
    	if(emptyFields()) {
			if(ifDobIsDate()) {
				
					if(newPatientAddedToDb()) {
						if(patientInsuranceDetailsAdded()) {
							if(newUserActivityAdded()) {
								
								if(newVisitAdded()) { 
									Alertmaker.success("New Patient added Successfully");
									
									updatePatientList();
									
									closee();
								}
								
							}
						}
					}
				
			}
		}
    }




	private boolean newVisitAdded() {
		
		int ipid = getInsertedPatientId();
		LocalDate dt = LocalDate.now();
		
		
		
		if(handler.execAction("INSERT INTO patientvisit (patient_id,  visit_date, created_by) VALUES ("
				
				+ "'"+ipid+"',"
				+ "'"+dt+"',"
				+ "'"+lu.getId()+"'"
				+ ")")) {
			
			return true;
			
		}
		Alertmaker.error("Error in saving new visit info in db");
		return false;
		
	}




	private void updatePatientList() {
		try {
			ResultSet pdetails = handler.execQuery("SELECT * FROM patients WHERE name = '" +name.getText()+ "'");
			
			if(pdetails.first()) {
				String strDate = null;
				String insstatus = null;
				Date dt = pdetails.getDate("dob");
				SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy");
				strDate = fmt.format(dt);
				
				
				if(pdetails.getBoolean("withins")) {
					insstatus = "Insurance Patient";
				} else {
					insstatus = "No insurance";
				}

				Mainpatientscontroller.list.add(new Patient(pdetails.getInt("id"), pdetails.getString("name"), lu.getUsername(),
						pdetails.getString("contact"), 
						pdetails.getString("email"), strDate, pdetails.getString("adress"), insstatus));
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
	}




	private void closee() {
		
		((Stage) name.getScene().getWindow()).close();
		
	}




	private boolean newUserActivityAdded() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new "+insurance.getValue()+""
				+ " patient "+name.getText()+"')")) {
			return true;
		}
    	Alertmaker.error("There was an error in activity");
    	return false;
	}




	private boolean patientInsuranceDetailsAdded() {
		
		int ipid = getInsertedPatientId();
		int iid = getSelectedInsuranceId();
		String imemid = insmemid.getText();
		String icompany = company.getText();
		int ismart =0;
		if(smartAvailable.isSelected() == true) {
			ismart = 1;
		}
		String ismartid = smartId.getText();
	
		
		
		if(handler.execAction("INSERT INTO patientinsurance (patient_id, insurance_id, member_number, scheme, smart, smart_id, created_by) VALUES ("
				
				+ "'"+ipid+"',"
				+ "'"+iid+"',"
				+ "'"+imemid+"',"
				+ "'"+icompany+"',"
				+ "'"+ismart+"',"
				+ "'"+ismartid+"',"
				+ "'"+lu.getId()+"'"
				+ ")")) {
			
			return true;
			
		}
		Alertmaker.error("Error in saving patient insurance info in db");
		return false;
		
	}
	
	
	
	int getSelectedInsuranceId() {
		int insId = 0;
		
		try {
			ResultSet insdetails = handler.execQuery("SELECT id FROM insurances WHERE name = '" +insurance.getValue()+ "'");
			if(insdetails.first()) {
				insId = insdetails.getInt("id");
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
		
		return insId;
	}
	
	
	
	int getInsertedPatientId() {
		int pId = 0;
		
		try {
			ResultSet pdetails = handler.execQuery("SELECT id FROM patients WHERE name = '" +name.getText()+ "'");
			
			if(pdetails.first()) {
				
				pId = pdetails.getInt("id");
				
			}
			
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
		
		return pId;
	}




	private boolean newPatientAddedToDb() {
		
		String pname = name.getText();
		LocalDate pdob = dob.getValue();
		String pcontact = contact.getText();
		String pemail = email.getText();
		String padress = adress.getText();
		
		
		if(handler.execAction("INSERT INTO patients (name, dob, contact, email, adress, created_by, withins) VALUES ("
				
				+ "'"+pname+"',"
				+ "'"+pdob+"',"
				+ "'"+pcontact+"',"
				+ "'"+pemail+"',"
				+ "'"+padress+"',"
				+ "'"+lu.getId()+"',"
				+ "1"
				+ ")")) {
			
			return true;
			
		}
		Alertmaker.error("Error in saving patient in db");
		return false;
	}




	private boolean ifDobIsDate() {
		LocalDate today = LocalDate.now();
		if(dob.getValue().isAfter(today)) {
			Alertmaker.error("Birth date can't be in the future");
			return false;
		}
		
		try {
			String dt = dob.getValue().toString();
			LocalDate.parse(dt);
		} catch (Exception e) {
			Alertmaker.error(e.getMessage());
			return false;
		}
		
		
		return true;
	}




	private boolean emptyFields() {
		if(name.getText().isEmpty() || (dob.getValue() == null) || contact.getText().isEmpty()
				|| adress.getText().isEmpty() || insurance.getSelectionModel().isEmpty() || 
				insmemid.getText().isEmpty() || company.getText().isEmpty() ) {
			
			Alertmaker.error("Enter Values for all fields");
			
			return false;
			
		}
		return true;
	}

	

}
