package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.model.AlertModel;
import core.util.*;
import core.vo.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteDialog extends ManagerFXML implements Initializable{

    public TextField jCedula, jNombre, jApellido, jDireccion, jNombreCiudad, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir;
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());

    public ClienteDialog() {
        initData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initData() {
        abrirStageStyle(Route.ClienteDialog, "", Modality.NONE, null,
                false, StageStyle.TRANSPARENT, null);
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion, jNombreCiudad);
            Validar.entradaNumerica(jCedula);
            // Validar.isLetterOptimo(jNombre.getText(), jApellido.getText());
            clienteDAO.insert(getClienteSeleccion());
            cerrarStage(btnAgregar);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private Cliente getClienteSeleccion() {
        Cliente cliente = new Cliente();
        cliente.setNombres(jNombre.getText());
        cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setUsuario_cedula(Storage.getUsuario().getCedula());
        return cliente;
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jCedula, jNombre, jApellido, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }
}
