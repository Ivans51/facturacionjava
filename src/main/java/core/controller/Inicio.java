package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Route;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Inicio extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public Label lblNombreUsuario;
    public JFXButton btnCliente, btnProducto, btnStock, btnFacturacion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cambiarEscena(Route.AlertDialog, anchorPane);
    }

    public void actionCliente(ActionEvent actionEvent) {

    }

    public void actionProducto(ActionEvent actionEvent) {

    }

    public void actionStock(ActionEvent actionEvent) {

    }

    public void actionFacturacion(ActionEvent actionEvent) {

    }
}
