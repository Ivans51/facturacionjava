package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
import javafx.beans.value.ObservableValue;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroCliente extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jCedula, jNombre, jApellido, jDireccion, jTelefono, jBuscar;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEliminar;
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
    private int[] rangeLimitCedula = {7, 8};
    private int[] rangeLimitTelefono = {11, 11};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cNacionalidad.getItems().addAll(nacionalidades);
        Validar.getValueLimit(cNacionalidad, "");
        setTable();
        setBuscarTableCliente();
    }

    private void setBuscarTableCliente() {
        ObservableList<Cliente> data = table.getData();
        jBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length()))
                tableCliente.setItems(data);
            table.searchMultiple(newValue.toLowerCase());
        });
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
            Validar.limitField(rangeLimitCedula, "Cédula", jCedula);
            Validar.limitField(rangeLimitTelefono, "Telefono", jTelefono);
            elegirConsulta();
            tableCliente.refresh();
            Validar.limmpiarCampos(jNombre, jCedula, jDireccion, jApellido, jTelefono);
        } catch (ParseException | Myexception myexception){
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void elegirConsulta() throws Myexception, ParseException {
        if (!stateEdit) {
            Validar.checkValor(jCedula.getText(), cedulas, "cédula");
            clienteDAO.insert(getClienteInsert());
            Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
            table.getListTable().add(cliente);
            new AlertUtil(Estado.EXITOSA, "Cliente creado correctamente");
            new AuditoriaUtil().insertar("Registro del cliente");
        } else {
            stateViewEdit(false);
            clienteDAO.update(getClienteUpdate());
            selectAllCliente();
            new AlertUtil(Estado.EXITOSA, "Cliente modificado correctamente");
            new AuditoriaUtil().insertar("Cliente Actualizado");
        }
    }

    // Modelo que se envia a la BD
    private Cliente getClienteInsert() throws ParseException {
        Cliente cliente = new Cliente();
        if (!stateEdit) cliente.setCedula(Integer.parseInt(jCedula.getText()));
        cliente.setNombres(jNombre.getText());
        cliente.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        cliente.setDireccion(jDireccion.getText());
        cliente.setApellidos(jApellido.getText());
        cliente.setTelefono(jTelefono.getText());
        cliente.setFecha(FechaUtil.getCurrentDate());
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
        btnEliminar.setVisible(edit);
        jCedula.setDisable(edit);
        cNacionalidad.setDisable(edit);
        btnAgregar.setText(edit ? "Editar" : "Agregar");
        btnLimpiar.setText(edit ? "Cancelar" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        if (stateEdit) stateViewEdit(false);
        Validar.limmpiarCampos(jCedula, jNombre, jTelefono, jApellido, jDireccion);
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionEliminar(ActionEvent actionEvent) {
        Cliente servicios = new Cliente();
        servicios.setCedula(cliente.getCedula());
        servicios.setEstado(0);
        clienteDAO.updateEstado(servicios);
        table.getListTable().remove(this.cliente);
        tableCliente.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
    }
}
