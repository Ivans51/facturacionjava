package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import core.util.Route;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioInfo extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public JFXButton btnAyuda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionAyuda(ActionEvent actionEvent) {
        abrirStage(Route.Ayuda, "Ayudas", btnAyuda, null);
    }
}
