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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DialogCliente extends ManagerFXML implements Initializable {

    public TextField jCedula, jNombre, jApellido, jDireccion, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir;
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private Label lblNombre, lblCiudad, lblTelefono;
    private List<String> cedulas = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Cliente> clientes = clienteDAO.selectAll();
        clientes.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
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
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion);
            Validar.isNumber(jCedula, jTelefono);
            Validar.isLetter(jNombre.getText(), jApellido.getText());
            Validar.checkValor(jCedula.getText(), cedulas, "cédula");
            clienteDAO.insert(getClienteSeleccion());
            new AlertUtil(Estado.EXITOSA, "Cliente registrado", lblClose -> {
                cerrarStage(lblClose);
                cerrarStage(btnAgregar);
                lblNombre.setText(jNombre.getText());
                lblCiudad.setText(jDireccion.getText());
                lblTelefono.setText(jTelefono.getText());
            });
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, "Hubo un error, asegurese que la información ingresada sea correcta");
            myexception.printStackTrace();
        }
    }

    private Cliente getClienteSeleccion() {
        Cliente cliente = new Cliente();
        cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setNombres(jNombre.getText());
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setUsuario_cedula(Storage.getUsuario().getCedula());
        return cliente;
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jApellido, jTelefono, jDireccion);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }
}
