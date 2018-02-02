package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Administrador extends ManagerFXML implements Initializable{

    public AnchorPane anchorPane;
    public JFXButton btnAuditoria, btnReportes, btnSalir, btnImprimir;
    public ComboBox cReportes;
    public TableView table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAuditoria(ActionEvent actionEvent) {

    }

    public void actionReportes(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {

    }

    public void actionImprimir(ActionEvent actionEvent) {

    }
}
