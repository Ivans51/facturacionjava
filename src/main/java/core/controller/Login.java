package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.*;
import core.vo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Login extends ManagerFXML implements Initializable {

    private final LoginUser loginUser = new LoginUser();
    public JFXButton btnIngresar, btnSalir;
    public TextField jUsuario;
    public PasswordField jPassword;
    public Label btnRecuperar, btnCambiarClave;

    private Usuario usuario;
    private String inicio = Route.Inicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionIngresar(ActionEvent actionEvent) {
        try {
            Validar.claveVacio(jPassword);
            usuario = loginUser.iniciarSession(jUsuario.getText(), jPassword.getText());
            Storage.setUsuario(usuario);
            validarStatus();
        } catch (Myexception ex) {
            System.out.println(ex.getMessage());
            new AlertUtil(Estado.ERROR, ex.getMessage());
        }
    }

    private void validarStatus() throws Myexception {
        String title = "Inversiones Todo Frio C.A.";
        switch (usuario.getStatus()) {
            case Estado.ASISTENTE:
                abrirStage(inicio, title, btnIngresar, null);
                new AuditoriaUtil().insertar("Usuario inicio sesion" + usuario.getNombre());
                break;
            case Estado.TECNICO:
                abrirStage(inicio, title, btnIngresar, null);
                new AuditoriaUtil().insertar("Usuario inicio sesion" + usuario.getNombre());
                break;
            case Estado.GERENTE:
                abrirStage(inicio, title, btnIngresar, null);
                new AuditoriaUtil().insertar("Usuario inicio sesion" + usuario.getNombre());
                break;
            default:
                throw new Myexception("Acceso denegado");
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }

    public void actionRecuperar(MouseEvent mouseEvent) {
        abrirStage(Route.RecuperarClave, "Recuperar clave", btnRecuperar, null);
    }

    public void actionCambiarClave(MouseEvent mouseEvent) {
        abrirStage(Route.CambiarClave, "Cambiar clave", btnRecuperar, null);
    }
}
