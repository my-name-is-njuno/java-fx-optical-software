package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
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
import tools.Drug;
import tools.Supplier;
import tools.Tool;

public class Mainequipmentscontroller implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private VBox header;

    @FXML
    private JFXButton buy;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXComboBox<String> measure;

    @FXML
    private JFXComboBox<String> supplier;

    @FXML
    private JFXTextField bp;

    @FXML
    private TableView<Tool> table;

    @FXML
    private TableColumn<Tool, String> tool;
    
    @FXML
    private TableColumn<Tool, String> cost;

    @FXML
    private JFXTextField searchValue;

    @FXML
    private ListView<String> toolInfo;
    
    @FXML
    private JFXComboBox<String> paymentMode;
    
    
    Db handler;
    
    LoggedinUser lu;
    
    static ObservableList<Tool> list = FXCollections.observableArrayList();
    ObservableList<String> toolinfomation = FXCollections.observableArrayList();
    
    ObservableList<String> paymentsMode = FXCollections.observableArrayList("Credit", "Bank", "Cash");
    
    ObservableList<String> measurements = FXCollections.observableArrayList("Grams", "Kilograms", "Litres", "Packets", "Pills");
    
    static ObservableList<String> supplierslist = FXCollections.observableArrayList();
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	handler = Db.getInstance();
    	
    	lu = handler.getLu();
    	
    	
    	
    	
    	paymentMode.setItems(paymentsMode);
    	
    	measure.setItems(measurements);
    	
    	supplier.setItems(supplierslist);
    	
    	loadSupplierList();
    	
    	initCol();
    	
    	loadTable();
     	
    	loadToolInfoOnListView();
		
	}
    
    
    
    void loadSupplierList() {
    	
    	ResultSet rs = handler.execQuery("SELECT name FROM suppliers");
    	supplierslist.clear();
    	try {
			while(rs.next()) {
				supplierslist.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	
    }

    private void loadTable() {
		
    	try {
    		ResultSet rs =handler.execQuery("SELECT * FROM tools ORDER BY id DESC");
    		String addedBy = null;
    		list.clear();
    		
			while(rs.next()) {
				int added = rs.getInt("created_by");
				ResultSet rsu =handler.execQuery("SELECT * FROM users WHERE id = '"+ added +"'");
				if(rsu.first()) {
					addedBy = rsu.getString("name");
				}

				list.add(new Tool(rs.getInt("id"), rs.getString("name"), rs.getString("supplier"), rs.getString("measure"), rs.getString("description"),
						 addedBy, rs.getDouble("quantity"), rs.getDouble("cost")));
					
				
			}
		} catch (SQLException e) {
			Alertmaker.error(e.getMessage());
		}
    	
    	
    	table.setItems(list);
		
	}

	private void initCol() {
		
		tool.setCellValueFactory(new PropertyValueFactory<>("name"));
		cost.setCellValueFactory(new PropertyValueFactory<>("available"));
		
	}

	@FXML
    void addNewTool(ActionEvent event) {
		if(emptyFields()) {
			if(quantityIsDouble()) {
				if(bpIsDouble()) {
					if(newToolAdded()) {
						if(newExpenseAdded()) {
							if(newUserActivityAdded()) {
								
								Alertmaker.success("Tool / Equipment added Successfully");
								clear();
								loadTable();
								
							}
						}
					}
				}
			}
		}
    }

    

	private void clear() {
		
		name.setText("");
		name.setEditable(true);
		description.setText("");
		quantity.setText("");
		bp.setText("");
		measure.setValue("");
		supplier.setValue("");
		paymentMode.setValue("");
		
		
		
	}

	private boolean newUserActivityAdded() {
		if(handler.execAction("INSERT INTO activities (user_id, activity) VALUES ('"+lu.getId()+"', 'added new Tool/Equipment "+name.getText()+"')")) {
			return true;
		}
    	Alertmaker.error("There was an error in activity");
    	return false;
	}

	private boolean newExpenseAdded() {
		String toolName = name.getText();
		double toolQuantity = Double.parseDouble(quantity.getText());
		String toolMeasure = measure.getValue();
		double toolBp = Double.parseDouble(bp.getText());
		double totalBp = toolBp * toolQuantity;
		double toolTotalCost = totalBp;
		String toolModeOfPayment = paymentMode.getValue();
		int userid = lu.getId();
		
		
		String sql = "INSERT INTO `expenses`(`category`, `item`, `measure`, `quantity`, `bp`, `tbp`, `othercost`,"
				+ " `totalothercost`, `totalcost`, `modeofpayment`, `created_by`) VALUES ("
				+ "'equipments and tools',"
				+ "'"+toolName+"',"
				+ "'"+toolMeasure+"',"
				+ "'"+toolQuantity+"',"
				+ "'"+toolBp+"',"
				+ "'"+totalBp+"',"
				+ "'"+0+"',"
				+ "'"+0+"',"
				+ "'"+toolTotalCost+"',"	
				+ "'"+toolModeOfPayment+"',"
				+ "'"+userid+"'"
				+ ")";
		
		
		if(handler.execAction(sql)) {
			return true;
		}
		
		Alertmaker.error("Error, in expenses table");		
		return false;
	}

	private boolean newToolAdded() {
		
		String toolName = name.getText();
		String toolDescription = description.getText();
		double toolQuantity = Double.parseDouble(quantity.getText());
		String toolMeasure = measure.getValue();
		String toolSupplier = supplier.getValue();
		double toolBp = Double.parseDouble(bp.getText());
		
		int userid = lu.getId();
		
		String sql = "INSERT INTO tools (name,description,quantity,measure,supplier,cost,created_by) VALUES ("
				+ "'"+toolName+"',"
				+ "'"+toolDescription+"',"
				+ "'"+toolQuantity+"',"
				+ "'"+toolMeasure+"',"
				+ "'"+toolSupplier+"',"
				+ "'"+toolBp+"',"
				+ "'"+userid+"'"
				+ ")";
		
		if(handler.execAction(sql)) {
			
			return true;
			
		}
		Alertmaker.error("Error, Tools table");
		return false;
	}

	private boolean bpIsDouble() {
		try {
			Double.parseDouble(bp.getText());
			return true;
		} catch(Exception e) {
			Alertmaker.error("The value of quantity should be a number");
			return false;
		}
	}

	private boolean quantityIsDouble() {
		try {
			Double.parseDouble(quantity.getText());
			return true;
		} catch(Exception e) {
			Alertmaker.error("The value of quantity should be a number");
			return false;
		}
	}

	private boolean emptyFields() {
		if(name.getText().isEmpty() || description.getText().isEmpty() || quantity.getText().isEmpty()
				|| measure.getSelectionModel().isEmpty() || bp.getText().isEmpty() || supplier.getSelectionModel().isEmpty()
				|| paymentMode.getSelectionModel().isEmpty() ) {
			
			Alertmaker.error("Enter Values for all fields");
			
			return false;
			
		}
		return true;
	}
	
	
	
	
	
	
	@FXML
    void getSelectedToolInfo(MouseEvent event) {
		loadToolInfoOnListView();
    }


	private void loadToolInfoOnListView() {
		Tool selectedTool = table.getSelectionModel().getSelectedItem();
		if(selectedTool == null) {
			toolinfomation.clear();
			toolinfomation.add("No tool Selected");
		} else {
			toolinfomation.clear();
			
			toolinfomation.add(selectedTool.getName() + " further Info");
			toolinfomation.add("	Description : " + selectedTool.getDescription());	
			toolinfomation.add("	Available : " + selectedTool.getQuantity() + " " + selectedTool.getMeasure());
			toolinfomation.add("	Buying Price : " + selectedTool.getBp());	
			toolinfomation.add("	Added By : " + selectedTool.getAddedby());	
			
			
			
		}
		
		toolInfo.getItems().setAll(toolinfomation);
	}

	@FXML
    void clearFields(ActionEvent event) {
		clear();
    }

    @FXML
    void editToolInfo(ActionEvent event) {
    	Tool selectedTool = table.getSelectionModel().getSelectedItem();
    	if(selectedTool == null) {
			toolinfomation.clear();
			toolinfomation.add("No tool Selected");
		}
    	name.setText(selectedTool.getName());
    	name.setEditable(false);
		description.setText(selectedTool.getDescription());
//		quantity.setText(String.valueOf(selectedTool.getQuantity()));
//		bp.setText(String.valueOf(selectedTool.getBp()));
		measure.setValue(selectedTool.getMeasure());
//		supplier.setValue(selectedTool.getSupplier());
		paymentMode.setValue("");
    }

    @FXML
    void loadDisposeWindow(ActionEvent event) {

    }

    @FXML
    void loadToolReturnWindow(ActionEvent event) {

    }

    @FXML
    void newSupplier(ActionEvent event) {
    	try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/addsupplier.fxml"));
			Parent root = (Parent)fxmlloader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("New Supplier");
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
    void searchTool(ActionEvent event) {

    }

    @FXML
    void updateExistingTool(ActionEvent event) {
    	Tool selectedTool = table.getSelectionModel().getSelectedItem();
    	if(selectedTool == null) {
			toolinfomation.clear();
			toolinfomation.add("No tool Selected");
			return;
		}
    	
    	if(emptyFields()) {
    		if(quantityIsDouble()) {
				if(bpIsDouble()) {
					if(newToolAdded()) {
						if(newExpenseAdded()) {
							if(newUserActivityAdded()) {
								
								Alertmaker.success("Tool / Equipment Restocked Successfully");
								clear();
								loadTable();
								
							}
						}
					}
				}
			}
    	}
    	
    	
    	
    	
    }

	
}
