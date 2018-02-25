package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ServiciosDAO;
import core.dao.SubServiciosDAO;
import core.util.*;
import core.vo.Servicios;
import core.vo.SubServicios;
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
    public TextField jNombre, jPrecio, jTiempoE;
    public JFXButton btnAgregar, btnLimpiar, btnDesactivar, btnSalir;
    public TableView<Servicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<Servicios, String> table;
    private Servicios servicios = new Servicios();
    private String[] columS = {"nombre", "precio", "fecha", "tiempo_estimado"};
    private List<Servicios> serviciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private boolean stateEdit = false;

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
            Validar.campoVacio(jNombre, jPrecio, jTiempoE);
            Validar.isNumber(jPrecio, jTiempoE);
            if (!stateEdit) {
                Validar.checkValor(jNombre.getText(), nombres, "nombre");
                serviciosDAO.insert(getServicios());
                subServiciosDAO.insert(getSubServicios(serviciosDAO.selectLastID().getIdservicios()));
                int id = serviciosDAO.selectLastID().getIdservicios();
                table.getListTable().add(serviciosDAO.selectById(id));
            } else {
                serviciosDAO.update(getServicios());
                selectAllServicio();
                stateViewEdit(false);
            }
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (Myexception | ParseException myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private SubServicios getSubServicios(int idservicios) throws ParseException {
        SubServicios subServicios = new SubServicios();
        subServicios.setNombreSub(jNombre.getText());
        subServicios.setFechaSub(FechaUtil.getCurrentDate());
        subServicios.setPrecioSub(Double.valueOf(jPrecio.getText()));
        subServicios.setTiempo_estimadoSub(Integer.parseInt(jTiempoE.getText()));
        subServicios.setUsuario_cedula(Storage.getUsuario().getCedula());
        subServicios.setSubservicio_idsubservicio(idservicios);
        return subServicios;
    }

    public void actionDesactivar(ActionEvent actionEvent) {
        Servicios servicios = new Servicios();
        servicios.setIdservicios(this.servicios.getIdservicios());
        servicios.setEstado(this.servicios.isEstado() == 0 ? 1 : 0);
        serviciosDAO.updateEstado(servicios);
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
        btnDesactivar.setText(servicios.isEstado() == 1 ? "Desactivar" : "Activar");
    }

    private Servicios getServicios() throws ParseException {
        if (!stateEdit) servicios.setNombre(jNombre.getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getText());
        return servicios;
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            servicios = table.getModel();
            jNombre.setText(servicios.getNombre());
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jTiempoE.setText(servicios.getTiempo_estimado());
            btnDesactivar.setText(servicios.isEstado() == 1 ? "Desactivar" : "Activar");
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnDesactivar.setVisible(value);
        jNombre.setDisable(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            if (servicios.isEstado() == 1)
                stateViewEdit(false);
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
