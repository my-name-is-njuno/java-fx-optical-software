package accessories;



import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alertmaker {
	
	
	public static void error(String message) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}


	public static void success(String message) {
		
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}

}
