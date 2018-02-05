package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
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

public class RegistroServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombre, jPrecio, jFecha, jTiempoE;
    public ComboBox cSubservicio;
    public JFXButton btnAgregar, btnLimpiar, btnEditar, btnEliminar, btnSalir;
    public TableView<Servicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<Servicios, String> table;
    private Servicios servicios;
    private String[] columS = {"Nombre", "Precio", "Fecha", "TiempoE"};
    private List<Servicios> serviciosList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEditar.setDisable(false);
        table = new TableUtil(Servicios.class, tableServicio);
        table.inicializarTabla(columS, tbNombre, tbPrecio, tbFecha, tbTiempoE);

        final ObservableList<Servicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> table.seleccionarTabla(this));

        selectAllServicio();
        table.getListTable().addAll(serviciosList);
    }

    private void selectAllServicio() {
        serviciosList = serviciosDAO.selectAll();
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jFecha, jTiempoE);
            Validar.entradaNumerica(jPrecio);
            // Validar.isLetterOptimo(jNombre.getText(), jApellido.getText());
            int id = serviciosDAO.insert(getServicios());
            Servicios servicios = serviciosDAO.selectById(id);
            table.getListTable().add(servicios );
            tableServicio.refresh();
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    public void actionEditar(ActionEvent actionEvent) {
        serviciosDAO.update(getServicios());
        selectAllServicio();
        tableServicio.refresh();
        btnEditar.setDisable(false);
        btnAgregar.setDisable(true);
    }

    private Servicios getServicios() {
        servicios.setNombre(jNombre.getText());
        servicios.setFecha(jFecha.getText());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getText());
        return servicios;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        serviciosDAO.delete(servicios.getIdservicios());
        table.getListTable().remove(servicios);
        tableServicio.refresh();
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            servicios = table.getModel();
            btnEditar.setDisable(true);
            jNombre.setText(servicios.getNombre());
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jFecha.setText(servicios.getFecha());
            jTiempoE.setText(servicios.getTiempo_estimado());
            btnAgregar.setDisable(false);
        }
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jPrecio, jFecha, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
