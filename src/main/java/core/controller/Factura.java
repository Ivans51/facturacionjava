package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Route;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Factura extends ManagerFXML implements Initializable{

    public AnchorPane anchorPane;
    public ComboBox cProductos, cProductosAgregados;
    public TextField jStock, jCantidad, jCedula;
    public JFXButton btnAgregar, btnSalir, btnMostrarProducto,btnBuscar, btnImprimir;
    public Label lblCantidad, lblProducto, lblTotal, lblNombre, lblCiudad, lblTelefono;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAgregar(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.Inicio, anchorPane);
    }

    public void actionMostrar(ActionEvent actionEvent) {

    }

    public void actionBuscar(ActionEvent actionEvent) {

    }

    public void actionImprimir(ActionEvent actionEvent) {

    }
}
