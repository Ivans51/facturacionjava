package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Myexception;
import core.util.Route;
import core.util.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistroUsuario extends ManagerFXML implements Initializable{

    public TextField jNombre, jCedula, jApellido, jDireccion, jNombreCiudad;
    public JFXButton btnAgregar, btnLimpiar, btnSalir,btnEditar, btnEliminar;
    public TableView tableCliente;
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAgregar(ActionEvent actionEvent) {

    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jCedula, jApellido, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(MouseEvent mouseEvent) {
        cambiarEscena(Route.Inicio, anchorPane);
    }

    public void actionEditar(ActionEvent actionEvent) {

    }

    public void actionEliminar(ActionEvent actionEvent) {
    }
}
