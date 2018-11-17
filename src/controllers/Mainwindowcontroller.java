package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import dbmanager.Db;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logged.in.user.LoggedinUser;

public class Mainwindowcontroller implements Initializable  {

    @FXML
    private BorderPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXButton doctors;

    @FXML
    private JFXButton patients;

    @FXML
    private JFXButton drugs;

    @FXML
    private JFXButton equipments;

    @FXML
    private JFXButton insurances;

    @FXML
    private JFXButton invoices;

    @FXML
    private JFXButton receipts;

    @FXML
    private JFXButton refunds;

    @FXML
    private JFXButton spending;

    @FXML
    private JFXButton revenues;

    @FXML
    private HBox bottom;

    @FXML
    private Text footerInfo;

    @FXML
    private Button edit;

    @FXML
    private Button view;
    
    
    @FXML
    private JFXButton procedures;

    @FXML
    private JFXButton services;
    
    
    Db handler;
    
    LoggedinUser lu;
    
    
    
    
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	lu = handler.getLu();
		
	}
    
    
    

    
    
    
    @FXML
    void loadProceduresWindow(ActionEvent event) {
    	loadWindow("/fxml/mainprocedures.fxml", "Manage Procedures Carried out");
    }
    
    @FXML
    void loadServicesrsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainservices.fxml", "Optical Services offered");
    }

    @FXML
    void loadDoctorsWindow(ActionEvent event) {
    	loadWindow("/fxml/maindoctor.fxml", "Doctors Management");
    }

    @FXML
    void loadDrugsWindow(ActionEvent event) {
    	loadWindow("/fxml/maindrugs.fxml", "Drugs Management");
    }

    @FXML
    void loadEquipmentsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainequipments.fxml", "Equipments and Tools Management");
    }

    @FXML
    void loadInsurancesWindow(ActionEvent event) {
    	loadWindow("/fxml/mainnsurances.fxml", "Insurance Clients");
    }

    @FXML
    void loadInvoicesWindow(ActionEvent event) {
    	loadWindow("/fxml/maininvoices.fxml", "Outstanding Invoices");
    }

    @FXML
    void loadPatientsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainpatients.fxml", "Patients Management");
    }

    @FXML
    void loadReceiptsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainreceipt.fxml", "Receipts Management");
    }

    @FXML
    void loadRefundsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainrefunds.fxml", "Refunds Management");
    }

    @FXML
    void loadRevenuesWindow(ActionEvent event) {
    	loadWindow("/fxml/mainrevenues.fxml", "Revenue Management");
    }

    @FXML
    void loadSpendingsWindow(ActionEvent event) {
    	loadWindow("/fxml/mainspendings.fxml", "Expenses Management");
    }

    
    
    
    
    
    
    @FXML
    void viewUserInfo(ActionEvent event) {
//    	is admin
    	if(lu.isType()) {
    		loadWindow("/fxml/settingsadmin.fxml", "Admin Settings");
    	} else {
    		loadWindow("/fxml/editcrediatilas.fxml", "User Settings");
    	}
    }
    
    @FXML
    void editUserSettings(ActionEvent event) {

    }
    
    
    
    
    
    
    
    
    
    
    void loadWindow(String loc, String title) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(loc));
			Parent root = (Parent)fxmlloader.load();
			
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(doctors.getScene().getWindow());
			stage.setScene(new Scene(root));
			stage.show();
			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	

}

