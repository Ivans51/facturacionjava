package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.AlertUtil;
import core.util.Estado;
import core.util.ManagerFXML;
import core.util.Route;
import core.vo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Graciela on 18/03/2018.
 */
public class CambiarClave extends ManagerFXML implements Initializable {

    public TextField jCorreo;
    public PasswordField jClave, jNuevaClave;
    public Label lblNueva;
    public JFXButton btnNuevaClave, btnSalir;

    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private Usuario byClaveCorre;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionEnviar(ActionEvent actionEvent) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(jCorreo.getText());
        usuario.setClave(jClave.getText());
        byClaveCorre = usuarioDAO.selectByClaveCorre(usuario);
        if (byClaveCorre != null){
            lblNueva.setVisible(true);
            jNuevaClave.setVisible(true);
            btnNuevaClave.setVisible(true);
        } else {
            new AlertUtil(Estado.EXITOSA, "Datos incorrectos");
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        abrirStage(Route.Login, "Inicio de Sesión", btnSalir, null);
    }

    public void actionNueva(ActionEvent actionEvent) {
        Usuario usuario = new Usuario();
        usuario.setCedula(byClaveCorre.getCedula());
        usuario.setClave(jNuevaClave.getText());
        usuarioDAO.updateClave(usuario);
        new AlertUtil(Estado.EXITOSA, "Su contraseña ha sido cambiada", closeAlert -> {
            cerrarStage(closeAlert);
            abrirStage(Route.Login, "Inicio de Sesión", btnSalir, null);
        });
    }
}
