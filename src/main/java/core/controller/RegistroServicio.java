package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Servicios;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombre, jPrecio, jTiempoE, jDescripcion;
    public JFXButton btnAgregar, btnLimpiar, btnDesactivar, btnSalir;
    public TableView<Servicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<Servicios, String> table;
    private Servicios servicios = new Servicios();
    private List<Servicios> serviciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private boolean stateEdit = false;
    private String[] columS = {"nombre", "precio", "fecha", "tiempo_estimado"};
    private String[] field = {"Nombre", "Precio", "Tiempo de Entrega"};
    private String[] fieldNumber = {"Precio", "Tiempo de Entrega"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();
    }

    private void setTable() {
        // To adjust widt column
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnDesactivar.setDisable(false);
        table = new TableUtil(Servicios.class, tableServicio);
        table.inicializarTabla(columS, tbNombre, tbPrecio, tbFecha, tbTiempoE);

        final ObservableList<Servicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> table.seleccionarTabla(this));

        selectAllServicio();
        table.getListTable().addAll(serviciosList);
    }

    private void selectAllServicio() {
        serviciosList = serviciosDAO.selectAll();
        serviciosList.forEach(it -> nombres.add(it.getNombre()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(field, jNombre, jPrecio, jTiempoE, jDescripcion);
            Validar.isNumber(fieldNumber, jPrecio, jTiempoE);
            elegirConsulta();
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE, jDescripcion);
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
            table.getListTable().add(serviciosDAO.selectById(id));
            new AuditoriaUtil().insertar("Registro del servicio");
        } else {
            serviciosDAO.update(getServiciosUpdate());
            selectAllServicio();
            stateViewEdit(false);
            new AuditoriaUtil().insertar("Servicio actualizado");
        }
        tableServicio.refresh();
    }

    private Servicios getServiciosInsert() throws ParseException {
        Servicios servicios = new Servicios();
        if (!stateEdit) servicios.setNombre(jNombre.getText());
        servicios.setDescripcion(jDescripcion.getText());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        servicios.setEstado("1");
        return servicios;
    }

    private Servicios getServiciosUpdate() throws ParseException {
        if (!stateEdit) servicios.setNombre(jNombre.getText());
        servicios.setDescripcion(jDescripcion.getText());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        return servicios;
    }

    public void actionDesactivar(ActionEvent actionEvent) {
        servicios.setIdservicios(servicios.getIdservicios());
        servicios.setEstado(servicios.isEstado().equals("0") ? "1" : "0");
        serviciosDAO.updateEstado(servicios);
        btnDesactivar.setText(servicios.isEstado().equals("1") ? "Desactivar" : "Activar");
        selectAllServicio();
        tableServicio.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            servicios = table.getModel();
            jNombre.setText(servicios.getNombre());
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jTiempoE.setText(servicios.getTiempo_estimado());
            btnDesactivar.setText(servicios.isEstado().equals("1") ? "Desactivar" : "Activar");
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnDesactivar.setVisible(value);
        jNombre.setDisable(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
        btnLimpiar.setText(value ? "Cancel E" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        if (servicios.isEstado().equals("1") || stateEdit)
            stateViewEdit(false);
        Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE, jDescripcion);
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
