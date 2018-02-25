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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroCliente extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jCedula, jNombre, jApellido, jDireccion, jTelefono;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEditarCancel;
    public TableView<Cliente> tableCliente;
    public AnchorPane anchorPane;
    public TableColumn tbNacionalidad, tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono;
    public ComboBox<String> cNacionalidad;

    private String[] nacionalidades = {"V", "E"};
    private TableUtil<Cliente, String> table;
    private String[] columS = {"nacionalidad", "cedula", "nombres", "apellidos", "direccion", "telefono"};
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Cliente> clientes = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private Cliente cliente;
    private boolean stateEdit = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cNacionalidad.getItems().addAll(nacionalidades);
        setTable();
    }

    private void setTable() {
        tableCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Cliente.class, tableCliente);
        table.inicializarTabla(columS, tbNacionalidad, tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono);

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
            Validar.campoVacio(jNombre, jCedula, jApellido, jDireccion);
            Validar.stringVacio(cNacionalidad.getSelectionModel().getSelectedItem());
            Validar.isNumber(jCedula, jTelefono);
            if (!stateEdit) {
                Validar.checkValor(jCedula.getText(), cedulas, "cédula");
                clienteDAO.insert(getClienteSeleccion());
                Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
                table.getListTable().add(cliente);
            } else {
                stateViewEdit(false);
                clienteDAO.update(getClienteSeleccion());
                selectAllCliente();
            }
            tableCliente.refresh();
            Validar.limmpiarCampos(jNombre, jCedula, jDireccion, jApellido, jTelefono);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private Cliente getClienteSeleccion() {
        cliente = new Cliente();
        if (!stateEdit) cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setNombres(jNombre.getText());
        cliente.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setUsuario_cedula(Storage.getUsuario().getCedula());
        return cliente;
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            stateViewEdit(false);
            Validar.limmpiarCampos(jNombre, jCedula, jDireccion, jApellido, jTelefono);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            cliente = table.getModel();
            jCedula.setText(String.valueOf(cliente.getCedula()));
            jNombre.setText(cliente.getNombres());
            jApellido.setText(cliente.getApellidos());
            jDireccion.setText(cliente.getDireccion());
            jTelefono.setText(cliente.getTelefono());
            cNacionalidad.getSelectionModel().select(cliente.getNacionalidad());
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnEditarCancel.setVisible(value);
        jCedula.setDisable(value);
        cNacionalidad.setDisable(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            if (stateEdit) stateViewEdit(false);
            Validar.limmpiarCampos(jCedula, jNombre, jTelefono, jApellido, jDireccion);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
