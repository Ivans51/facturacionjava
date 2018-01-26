package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Stock extends ManagerFXML implements Initializable{

    public AnchorPane anchorPane;
    public ComboBox cBuscar;
    public TextField jBuscar;
    public JFXButton btnBuscar;
    public TableView tableStock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionBuscar(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {

    }
}
