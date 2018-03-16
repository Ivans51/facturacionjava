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

    private TableUtil<Cliente, String> table;
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Cliente> clientes = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private Cliente cliente;
    private boolean stateEdit = false;
    private String[] nacionalidades = {"V", "E", "J"};
    private String[] columS = {"nacionalidad", "cedula", "nombres", "apellidos", "direccion", "telefono"};
    private String[] field = {"Nombre", "Cédula", "Apellido", "Dirección", "Teléfono"};
    private String[] fieldNumber = {"Cédula", "Teléfono"};
    private String[] fieldString = {"Nacionalidad"};
    private String[] fieldLimit = {"Cédula", "Teléfono"};
    private int[] rangeLimit = {7, 11};

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
            Validar.campoVacio(field, jNombre, jCedula, jApellido, jDireccion, jTelefono);
            Validar.stringVacio(fieldString, cNacionalidad.getSelectionModel().getSelectedItem());
            Validar.isNumber(fieldNumber, jCedula, jTelefono);
            Validar.limitField(rangeLimit, fieldLimit, jCedula, jTelefono);
            elegirConsulta();
            tableCliente.refresh();
            Validar.limmpiarCampos(jNombre, jCedula, jDireccion, jApellido, jTelefono);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void elegirConsulta() throws Myexception {
        if (!stateEdit) {
            Validar.checkValor(jCedula.getText(), cedulas, "cédula");
            clienteDAO.insert(getClienteInsert());
            Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
            table.getListTable().add(cliente);
            new AuditoriaUtil().insertar("Registro del cliente");
        } else {
            stateViewEdit(false);
            clienteDAO.update(getClienteUpdate());
            selectAllCliente();
            new AuditoriaUtil().insertar("Cliente Actualizado");
        }
    }

    // Modelo que se envia a la BD
    private Cliente getClienteInsert() {
        Cliente cliente = new Cliente();
        if (!stateEdit) cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setNombres(jNombre.getText());
        cliente.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setUsuario_cedula(Storage.getUsuario().getCedula());
        return cliente;
    }

    private Cliente getClienteUpdate() {
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
        stateViewEdit(false);
        Validar.limmpiarCampos(jNombre, jCedula, jDireccion, jApellido, jTelefono);
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

    private void stateViewEdit(boolean edit) {
        stateEdit = edit;
        btnEditarCancel.setVisible(edit);
        jCedula.setDisable(edit);
        cNacionalidad.setDisable(edit);
        btnAgregar.setText(edit ? "Editar" : "Agregar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        if (stateEdit) stateViewEdit(false);
        Validar.limmpiarCampos(jCedula, jNombre, jTelefono, jApellido, jDireccion);
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
