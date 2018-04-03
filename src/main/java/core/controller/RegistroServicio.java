package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Servicios;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombre, jPrecio, jBuscar;
    public JFXButton btnAgregar, btnLimpiar, btnEliminar, btnSalir;
    public TableView<Servicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;
    public Spinner<Integer> jTiempoE;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<Servicios, String> table;
    private Servicios servicios = new Servicios();
    private List<Servicios> serviciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private boolean stateEdit = false;
    private String[] columS = {"nombre", "precioEdit", "fechaEdit", "tiempo_estimado"};
    private String[] field = {"Nombre", "Precio", "Tiempo de Entrega"};
    private String[] fieldNumber = {"Precio", "Tiempo de Entrega"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jTiempoE.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000, 1));
        setTable();
        setBuscarTableCliente();
    }

    private void setTable() {
        // To adjust widt column
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnEliminar.setDisable(false);
        table = new TableUtil(Servicios.class, tableServicio);
        table.inicializarTabla(columS, tbNombre, tbPrecio, tbFecha, tbTiempoE);

        final ObservableList<Servicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> table.seleccionarTabla(this));

        selectAllServicio();
        table.getListTable().addAll(serviciosList);
    }

    private void setBuscarTableCliente() {
        ObservableList<Servicios> data = table.getData();
        jBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length()))
                tableServicio.setItems(data);
            table.searchMultiple(newValue.toLowerCase());
        });
    }

    private void selectAllServicio() {
        serviciosList = serviciosDAO.selectAll();
        for (Servicios servicios : serviciosList) {
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
            servicios.setPrecioEdit(String.format("%1$,.2f", servicios.getPrecio()) + " Bs");
        }
        serviciosList.forEach(it -> nombres.add(it.getNombre()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(field, jNombre, jPrecio);
            Validar.isNumber(fieldNumber, jPrecio);
            elegirConsulta();
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombre, jPrecio);
        } catch (Myexception | ParseException myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void elegirConsulta() throws Myexception, ParseException {
        if (!stateEdit) {
            Validar.checkValor(jNombre.getText(), nombres, "nombre");
            serviciosDAO.insert(getServiciosInsert());
            int id = serviciosDAO.selectLastID().getIdservicios();
            Servicios servicios = serviciosDAO.selectById(id);
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
            servicios.setPrecioEdit(String.format("%1$,.2f", servicios.getPrecio()) + " Bs");
            table.getListTable().add(servicios);
            new AlertUtil(Estado.EXITOSA, "Servicio creado correctamente");
            new AuditoriaUtil().insertar("Registro del servicio");
        } else {
            stateViewEdit(false);
            serviciosDAO.update(getServiciosUpdate());
            tableServicio.getItems().clear();
            selectAllServicio();
            table.getListTable().addAll(serviciosList);
            new AlertUtil(Estado.EXITOSA, "Servicio modificado correctamente");
            new AuditoriaUtil().insertar("Servicio actualizado");
        }
    }

    private Servicios getServiciosInsert() throws ParseException {
        Servicios servicios = new Servicios();
        servicios.setNombre(jNombre.getText());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getEditor().getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        servicios.setEstado(1);
        return servicios;
    }

    private Servicios getServiciosUpdate() throws ParseException {
        servicios.setNombre(jNombre.getText());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getEditor().getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        return servicios;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        Servicios servicios = new Servicios();
        servicios.setIdservicios(this.servicios.getIdservicios());
        servicios.setEstado(0);
        serviciosDAO.updateEstado(servicios);
        table.getListTable().remove(this.servicios);
        tableServicio.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            servicios = table.getModel();
            jNombre.setText(servicios.getNombre());
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jTiempoE.getEditor().setText(servicios.getTiempo_estimado());
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnEliminar.setVisible(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
        btnLimpiar.setText(value ? "Cancelar" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        if (servicios.isEstado() == 1 || stateEdit)
            stateViewEdit(false);
        Validar.limmpiarCampos(jNombre, jPrecio);
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
