package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Mainreceiptscontroller {

    @FXML
    private JFXTextField searchValue;

    @FXML
    private JFXButton search;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> receiptBy;

    @FXML
    private TableColumn<?, ?> receiptNo;

    @FXML
    void searchReceipt(ActionEvent event) {

    }

}
