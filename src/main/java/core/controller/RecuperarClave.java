package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.*;
import core.vo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RecuperarClave extends ManagerFXML implements Initializable {

    public JFXButton btnIngresar, btnSalir;
    public TextField jCorreo;

    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private String[] field = {"Correo"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionEnviar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(field, jCorreo);
            Usuario usuario = new Usuario();
            usuario.setCorreo(jCorreo.getText());
            usuario = usuarioDAO.selectByCorreo(usuario.getCorreo());
            if (usuario == null)
                new AlertUtil(Estado.ERROR, "El correo no existe");
            else {
                String username = "inversionestodofrioca@gmail.com";
                String password = "todo19641960";
                SendEmail.doSendMail(username, password, usuario.getCorreo(),
                        "Inversiones Todo Frío C.A.",
                        "Su usuario es: " + usuario.getNombre() + " y su clave es: " + usuario.getClave());
                new AlertUtil(Estado.EXITOSA, "Contraseña Enviada", closeAlert -> {
                    cerrarStage(closeAlert);
                    abrirStage(Route.Login, "Inicio de Sesión", btnSalir, null);
                });
            }
        } catch (Myexception myexception) {
            myexception.printStackTrace();
            new AlertUtil(Estado.ERROR, myexception.getMessage());
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        abrirStage(Route.Login, "Inicio de Sesión", btnSalir, null);
    }
}
