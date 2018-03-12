package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.Estado;
import core.util.ManagerFXML;
import core.util.Route;
import core.util.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Inicio extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public Label lblNombreUsuario;
    public JFXButton btnSubServicio, btnCliente, btnAdministrador, btnCerrar, btnFacturacion, btnServicio, btnUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNombreUsuario.setText(Storage.getUsuario().getNombre());
        cambiarEscena(Route.InicioInfo, anchorPane);
        stateButton();
    }

    private void stateButton() {
        // Deshabilitar botones
        switch (Storage.getUsuario().getStatus()){
            case Estado.TECNICO:
                btnUsuario.setVisible(false);
                btnServicio.setVisible(false);
                btnSubServicio.setVisible(false);
                btnAdministrador.setVisible(false);
                btnCliente.setVisible(false);
                break;
            case Estado.GERENTE:
                break;
            case Estado.ASISTENTE:
                btnServicio.setVisible(false);
                btnSubServicio.setVisible(false);
                btnUsuario.setVisible(false);
                break;
        }
    }

    public void actionCliente(ActionEvent actionEvent) {
        cambiarEscena(Route.RegistroCliente, anchorPane);
    }

    public void actionServicio(ActionEvent actionEvent) {
        cambiarEscena(Route.RegistroServicio, anchorPane);
    }

    public void actionFacturacion(ActionEvent actionEvent) {
        cambiarEscena(Route.Facturas, anchorPane);
    }

    public void actionAdministrador(ActionEvent actionEvent) {
        cambiarEscena(Route.Administrador, anchorPane);
    }

    public void actionUsuario(ActionEvent actionEvent) {
        cambiarEscena(Route.RegistroUsuario, anchorPane);
    }

    public void actionCerrar(ActionEvent actionEvent) {
        abrirStage(Route.Login, "Inicio de Sesión", btnCerrar, null);
    }

    public void actionSubServicio(ActionEvent actionEvent) {
        cambiarEscena(Route.RegistroSubServicio, anchorPane);
    }
}
