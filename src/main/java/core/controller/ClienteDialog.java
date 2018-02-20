package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.util.*;
import core.vo.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteDialog extends ManagerFXML implements Initializable {

    public TextField jCedula, jNombre, jApellido, jDireccion, jNombreCiudad, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir;
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private Label lblNombre, lblCiudad, lblTelefono;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void setModel(String text, Label lblNombre, Label lblCiudad, Label lblTelefono) {
        this.lblNombre = lblNombre;
        this.lblCiudad = lblCiudad;
        this.lblTelefono = lblTelefono;
        jCedula.setEditable(false);
        jNombre.setFocusTraversable(true);
        jCedula.setText(text);
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion, jNombreCiudad);
            Validar.entradaNumerica(jCedula, jTelefono);
            Validar.isLetter(jNombre.getText(), jApellido.getText());
            clienteDAO.insert(getClienteSeleccion());
            new AlertUtil(Estado.EXITOSA, "Usuario registrado", lblClose -> {
                cerrarStage(lblClose);
                cerrarStage(btnAgregar);
                lblNombre.setText(jNombre.getText());
                lblCiudad.setText(jNombreCiudad.getText());
                lblTelefono.setText(jTelefono.getText());
            });
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, "Hubo un error, asegurese que la información ingresada sea correcta");
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
            Validar.limmpiarCampos(jCedula, jNombre, jApellido, jTelefono, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }
}
