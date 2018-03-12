package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ServiciosDAO;
import core.dao.SubServiciosDAO;
import core.util.*;
import core.vo.Servicios;
import core.vo.SubServicios;
import javafx.collections.FXCollections;
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

public class RegistroSubServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombreSub, jPrecio, jTiempoE, jServicios;
    public JFXButton btnAgregar, btnLimpiar, btnDesactivar, btnSalir, btnAgregarServicio;
    public TableView<SubServicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;
    public ComboBox<String> cServicio;

    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<SubServicios, String> table;
    private SubServicios subServicios = new SubServicios();
    private String[] columS = {"nombreSub", "precioSub", "fechaSub", "tiempo_estimadoSub"};
    private List<SubServicios> subServiciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private boolean stateEdit = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCombo();
        setTable();
    }

    private void setCombo() {
        List<Servicios> subServicios = serviciosDAO.selectAllFilter();
        String[] cargos = new String[subServicios.size()];
        for (int i = 0; i < subServicios.size(); i++)
            cargos[i] = subServicios.get(i).getNombre();
        cServicio.setItems(FXCollections.observableArrayList(cargos));
    }

    private void setTable() {
        // To adjust widt column
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnDesactivar.setDisable(false);
        table = new TableUtil(SubServicios.class, tableServicio);
        table.inicializarTabla(columS, tbNombre, tbPrecio, tbFecha, tbTiempoE);

        final ObservableList<SubServicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<SubServicios>) c -> table.seleccionarTabla(this));

        selectAllServicio();
        table.getListTable().addAll(subServiciosList);
    }

    private void selectAllServicio() {
        nombres.clear();
        subServiciosList = subServiciosDAO.selectAll();
        subServiciosList.forEach(it -> nombres.add(it.getNombreSub()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombreSub, jPrecio, jTiempoE);
            Validar.isNumber(jPrecio, jTiempoE);
            eligirConsulta();
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
            cServicio.getSelectionModel().clearSelection();
        } catch (Myexception | ParseException myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void eligirConsulta() throws Myexception, ParseException {
        if (!stateEdit) {
            Validar.checkValor(jNombreSub.getText(), nombres, "nombre");
            subServiciosDAO.insert(getSubServiciosInsert());
            int id = subServiciosDAO.selectLastID().getIdsubservicio();
            table.getListTable().add(subServiciosDAO.selectById(id));
            new AuditoriaUtil().insertar("Registro del subServicio");
        } else {
            subServiciosDAO.update(getSubServicios());
            selectAllServicio();
            stateViewEdit(false);
            new AuditoriaUtil().insertar("subServicio Actualizado");
        }
        tableServicio.refresh();
    }

    private SubServicios getSubServiciosInsert() throws ParseException {
        SubServicios subServicios = new SubServicios();
        if (!stateEdit)
            subServicios.setIdsubservicio(this.subServicios.getIdsubservicio());
        subServicios.setNombreSub(jNombreSub.getText());
        subServicios.setFechaSub(FechaUtil.getCurrentDate());
        subServicios.setPrecioSub(Double.valueOf(jPrecio.getText()));
        subServicios.setTiempo_estimadoSub(Integer.parseInt(jTiempoE.getText()));
        subServicios.setEstado("1");
        subServicios.setUsuario_cedula(Storage.getUsuario().getCedula());
        Servicios servicios = serviciosDAO.selectByNombre(cServicio.getSelectionModel().getSelectedItem());
        subServicios.setSubservicio_idsubservicio(servicios.getIdservicios());
        return subServicios;
    }

    private SubServicios getSubServicios() throws ParseException {
        if (!stateEdit)
            subServicios.setIdsubservicio(subServicios.getIdsubservicio());
        subServicios.setNombreSub(jNombreSub.getText());
        subServicios.setFechaSub(FechaUtil.getCurrentDate());
        subServicios.setPrecioSub(Double.valueOf(jPrecio.getText()));
        subServicios.setTiempo_estimadoSub(Integer.parseInt(jTiempoE.getText()));
        subServicios.setUsuario_cedula(Storage.getUsuario().getCedula());
        Servicios servicios = serviciosDAO.selectByNombre(cServicio.getSelectionModel().getSelectedItem());
        subServicios.setSubservicio_idsubservicio(servicios.getIdservicios());
        return subServicios;
    }
    public void actionDesactivar(ActionEvent actionEvent) {
        subServicios.setIdsubservicio(subServicios.getIdsubservicio());
        subServicios.setEstado(subServicios.isEstado().equals("0") ? "1" : "0");
        subServiciosDAO.updateEstado(subServicios);
        btnDesactivar.setText(subServicios.isEstado().equals("1") ? "Desactivar" : "Activar");
        cServicio.getSelectionModel().clearSelection();
        selectAllServicio();
        tableServicio.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio", null);
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            subServicios = table.getModel();
            jNombreSub.setText(subServicios.getNombreSub());
            jPrecio.setText(String.valueOf(subServicios.getPrecioSub()));
            jTiempoE.setText(String.valueOf(subServicios.getTiempo_estimadoSub()));
            btnDesactivar.setText(subServicios.isEstado().equals("1") ? "Desactivar" : "Activar");
            Servicios servicios = serviciosDAO.selectByNombre(subServicios.getNombreSub());
            if (servicios != null)
                cServicio.getSelectionModel().select(servicios.getNombre());
            else
                cServicio.getSelectionModel().clearSelection();
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnDesactivar.setVisible(value);
        jNombreSub.setDisable(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
        btnLimpiar.setText(value ? "Cancel E" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            if (subServicios.isEstado().equals("1") || stateEdit)
                stateViewEdit(false);
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
            cServicio.getSelectionModel().clearSelection();
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

}
