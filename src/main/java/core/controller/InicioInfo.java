package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Route;
import core.util.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioInfo extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public Label lblNombre;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombre.setText(Storage.getUsuario().getNombre());
    }
}
