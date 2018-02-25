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

    public JFXButton btnIngresar, btnSalir;
    public TextField jUsuario;
    public PasswordField jPassword;
    public Label btnRecuperar;

    private final LoginUser loginUser = new LoginUser();
    private Usuario usuario;
    private String inicio = Route.Inicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionIngresar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jUsuario);
            usuario = loginUser.iniciarSession(jUsuario.getText(), jPassword.getText());
            Storage.setUsuario(usuario);
            validarStatus();
        } catch (Myexception ex) {
            System.out.println(ex.getMessage());
            new AlertUtil(Estado.ERROR, ex.getMessage());
        }
    }

    private void validarStatus() throws Myexception {
        switch (usuario.getStatus()) {
            case Estado.USUARIO:
                abrirStage(inicio, "Todo Frio C.A.", btnIngresar, null);
                auditoria();
                break;
            case Estado.ADMINISTRADOR:
                abrirStage(inicio, "Todo Frio C.A.", btnIngresar, null);
                auditoria();
                break;
            case Estado.GERENTE:
                abrirStage(inicio, "Todo Frio C.A.", btnIngresar, null);
                auditoria();
                break;
            default:
                throw new Myexception("Acceso denegado, \n Seleccione Empleado en Gestión de sessión");
        }
    }

    private void auditoria() throws Myexception {
        AuditoriaUtil auditoriaUtil = new AuditoriaUtil(usuario.getNombre(), usuario.getCedula());
        auditoriaUtil.insertar("Registro usuario " + usuario.getNombre());
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }

    public void actionRecuperar(MouseEvent mouseEvent) {
        abrirStage(Route.RecuperarClave, "Recuperar clave", btnRecuperar, null);
    }
}
