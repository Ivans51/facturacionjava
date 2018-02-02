package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistroServicio extends ManagerFXML implements Initializable {
    public TextField jNombre, jPrecio,jFecha,jTiempoE;
    public ComboBox cSubservicio;
    public JFXButton btnAgregar, btnLimpiar,btnEditar,btnEliminar, btnSalir;
    public TableView table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAgregar(ActionEvent actionEvent) {

    }

    public void actionLimpiar(ActionEvent actionEvent) {

    }

    public void actionEditar(ActionEvent actionEvent) {

    }

    public void actionEliminar(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {

    }
}
