package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.util.*;
import core.vo.Cliente;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroCliente extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jCedula, jNombre, jApellido, jDireccion, jNombreCiudad, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEditar, btnEliminar;
    public TableView<Cliente> tableCliente;
    public AnchorPane anchorPane;
    public TableColumn tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono, tbUsuario;

    private TableUtil<Cliente, String> table;
    private String[] columS = {"cedula", "nombres", "apellidos", "direccion", "telefono", "usuario_cedula"};
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Cliente> clientes = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private Cliente cliente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEditar.setDisable(false);
        setTable();
    }

    private void setTable() {
        tableCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Cliente.class, tableCliente);
        table.inicializarTabla(columS, tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono, tbUsuario);

        final ObservableList<Cliente> tablaSelecionada = tableCliente.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Cliente>) c -> table.seleccionarTabla(this));

        selectAllCliente();
        table.getListTable().addAll(clientes);
    }

    private void selectAllCliente() {
        clientes = clienteDAO.selectAll();
        clientes.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion, jNombreCiudad);
            Validar.entradaNumerica(jCedula);
            Validar.checkValor(jCedula.getText(), cedulas);
            // Validar.isLetterOptimo(jNombre.getText(), jApellido.getText());
            clienteDAO.insert(getClienteSeleccion());
            Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
            table.getListTable().add(cliente);
            tableCliente.refresh();
            Validar.limmpiarCampos(jNombre, jCedula, jNombreCiudad, jDireccion, jApellido, jTelefono);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion, jNombreCiudad);
            Validar.entradaNumerica(jCedula);
            clienteDAO.update(getClienteSeleccion());
            selectAllCliente();
            tableCliente.refresh();
            btnEditar.setDisable(false);
            btnAgregar.setDisable(true);
            Validar.limmpiarCampos(jNombre, jCedula, jNombreCiudad, jDireccion, jApellido, jTelefono);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private Cliente getClienteSeleccion() {
        cliente = new Cliente();
        cliente.setNombres(jNombre.getText());
        cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setUsuario_cedula(Storage.getUsuario().getCedula());
        return cliente;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        try {
            clienteDAO.delete(cliente.getCedula());
            table.getListTable().remove(cliente);
            tableCliente.refresh();
            Validar.limmpiarCampos(jCedula, jNombre, jApellido, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            cliente = table.getModel();
            btnEditar.setDisable(true);
            jNombre.setText(cliente.getNombres());
            jApellido.setText(cliente.getApellidos());
            jCedula.setText(String.valueOf(cliente.getCedula()));
            jDireccion.setText(cliente.getDireccion());
            jTelefono.setText(cliente.getTelefono());
            btnAgregar.setDisable(false);
        }
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jCedula, jNombre, jApellido, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
