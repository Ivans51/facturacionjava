package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Myexception;
import core.util.Route;
import core.util.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroProducto extends ManagerFXML implements Initializable{

    public TextField jNombre, jDescripcion, jPrecioVenta, jPrecioCosto, jStock;
    public JFXButton btnAgregar, btnLimpiar, btnSalir,btnEditar, btnEliminar;
    public TableView tableProducto;
    public AnchorPane anchorPane;
    public ComboBox cProveedor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAgregar(ActionEvent actionEvent) {

    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jDescripcion, jPrecioVenta, jPrecioCosto, jStock);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionEditar(ActionEvent actionEvent) {

    }

    public void actionEliminar(ActionEvent actionEvent) {
    }
}
