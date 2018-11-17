package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import accessories.Alertmaker;
import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logged.in.user.LoggedinUser;
import tools.Patient;

public class Addpatientcontroller implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXTextField adress;

    @FXML
    private JFXTextField email;

    @FXML
    private DatePicker dob;

    @FXML
    private VBox header;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton cancel;
    
    LoggedinUser lu;
    Db handler;
    
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
		
    	((Stage) name.getScene().getWindow()).close();
		
	}





	@FXML
    void savePatient(ActionEvent event) {
		if(emptyFields()) {
			if(ifDobIsDate()) {
				
					if(newPatientAddedToDb()) {
						
						if(newVisitAdded()) {
							
							if(newUserActivityAdded()) {
								
								Alertmaker.success("New Patient added Successfully");
								
								updatePatientList();
								
								closee();
								
								
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





	private boolean newUserActivityAdded() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new "
				+ " patient: "+name.getText()+"')")) {
			return true;
		}
    	Alertmaker.error("There was an error in activity");
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
				|| adress.getText().isEmpty() ) {
			
			Alertmaker.error("Enter Values for all fields");
			
			return false;
			
		}
		return true;
	}

	

}
