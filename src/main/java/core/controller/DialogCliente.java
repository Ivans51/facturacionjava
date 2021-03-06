package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.util.*;
import core.vo.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DialogCliente extends ManagerFXML implements Initializable {

    public TextField jCedula, jNombre, jApellido, jDireccion, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir;
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private List<String> cedulas = new ArrayList<>();
    private String[] campos = {"Nombre", "Cédula", "Apellido", "Dirección"};
    private String[] fieldString = {"Nombre", "Apellido"};
    private String[] fieldNumber = {"Cédula", "Teléfono"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Cliente> clientes = clienteDAO.selectAll();
        clientes.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
    }

    void setModel(String text) {
        jCedula.setEditable(false);
        jNombre.setFocusTraversable(true);
        jCedula.setText(text);
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(campos, jNombre, jCedula, jApellido, jDireccion);
            Validar.isNumber(fieldNumber, jCedula, jTelefono);
            Validar.isLetter(fieldString, jNombre.getText(), jApellido.getText());
            Validar.checkValor(jCedula.getText(), cedulas, "cédula");
            clienteDAO.insert(getClienteSeleccion());
            new AlertUtil(Estado.EXITOSA, "Cliente registrado", lblClose -> {
                cerrarStage(lblClose);
                cerrarStage(btnAgregar);
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
        Validar.limmpiarCampos(jNombre, jApellido, jTelefono, jDireccion);
    }

    public void actionSalir(ActionEvent actionEvent) {
        cerrarStage(btnSalir);
    }
}
