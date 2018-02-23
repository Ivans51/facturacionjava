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

public class RegistroServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombre, jPrecio, jTiempoE;
    public ComboBox<String> cSubservicio;
    public JFXButton btnAgregar, btnLimpiar, btnEditar, btnEliminar, btnSalir;
    public TableView<Servicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<Servicios, String> table;
    private Servicios servicios = new Servicios();
    private String[] columS = {"nombre", "precio", "fecha", "tiempo_estimado"};
    private List<Servicios> serviciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private int idservicios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCombo();
        setTable();
        idservicios = serviciosList.get(serviciosList.size() - 1).getIdservicios();
    }

    private void setCombo() {
        List<SubServicios> subServicios = subServiciosDAO.selectAll();
        String[] cargos = new String[subServicios.size()];
        for (int i = 0; i < subServicios.size(); i++)
            cargos[i] = subServicios.get(i).getNombreSub();
        cSubservicio.setItems(FXCollections.observableArrayList(cargos));
        /*cSubservicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            contratacion = contratacionDAO.selectByCargo(newValue.toString());
        });*/
    }

    private void setTable() {
        // To adjust widt column
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        serviciosList.forEach(it -> nombres.add(it.getNombre()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jTiempoE);
            Validar.entradaNumerica(jPrecio, jTiempoE);
            Validar.checkValor(jNombre.getText(), nombres);
            // Validar.isLetterOptimo(jNombre.getText(), jApellido.getText());
            serviciosDAO.insert(getServicios());
            table.getListTable().add(serviciosDAO.selectById(++idservicios));
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (Myexception | ParseException myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jTiempoE);
            Validar.entradaNumerica(jPrecio, jTiempoE);
            Validar.checkValor(jNombre.getText(), nombres);
            serviciosDAO.update(getServicios());
            selectAllServicio();
            tableServicio.refresh();
            btnEditar.setDisable(false);
            btnAgregar.setDisable(true);
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (ParseException | Myexception e) {
            new AlertUtil(Estado.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    private Servicios getServicios() throws ParseException {
        servicios.setNombre(jNombre.getText());
        servicios.setFecha(FechaUtil.getCurrentDate());
        servicios.setPrecio(Double.valueOf(jPrecio.getText()));
        servicios.setTiempo_estimado(jTiempoE.getText());
        servicios.setUsuario_cedula(Storage.getUsuario().getCedula());
        int id = subServiciosDAO.selectByNombre(cSubservicio.getSelectionModel().getSelectedItem()).getIdsubservicio();
        servicios.setSubservicio_idsubservicio(id);
        return servicios;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        try {
            serviciosDAO.delete(servicios.getIdservicios());
            table.getListTable().remove(servicios);
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            servicios = table.getModel();
            btnEditar.setDisable(true);
            jNombre.setText(servicios.getNombre());
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jTiempoE.setText(servicios.getTiempo_estimado());
            btnAgregar.setDisable(false);
        }
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jPrecio, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
