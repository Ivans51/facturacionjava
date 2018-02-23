package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.SubServiciosDAO;
import core.util.*;
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

public class RegistroSubServicio extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public TextField jNombreSub, jPrecio, jTiempoE;
    public JFXButton btnAgregar, btnLimpiar, btnEditar, btnEliminar, btnSalir;
    public TableView<SubServicios> tableServicio;
    public TableColumn tbNombre, tbPrecio, tbFecha, tbTiempoE;

    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private TableUtil<SubServicios, String> table;
    private SubServicios subServicios = new SubServicios();
    private String[] columS = {"Nombre", "Costo", "Fecha", "tiempo_estimado"};
    private List<SubServicios> subServiciosList = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private int count;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();
    }

    private void setTable() {
        // To adjust widt column
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnEditar.setDisable(false);
        table = new TableUtil(SubServicios.class, tableServicio);
        table.inicializarTabla(columS, tbNombre, tbPrecio, tbFecha, tbTiempoE);

        final ObservableList<SubServicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<SubServicios>) c -> table.seleccionarTabla(this));

        selectAllServicio();
        table.getListTable().addAll(subServiciosList);
        count = subServiciosList.get(subServiciosList.size() - 1).getIdsubservicio() + 1;
    }

    private void selectAllServicio() {
        subServiciosList.forEach(it -> nombres.add(it.getNombreSub()));
        subServiciosList = subServiciosDAO.selectAll();
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombreSub, jTiempoE);
            Validar.entradaNumerica(jPrecio, jTiempoE);
            Validar.checkValor(jNombreSub.getText(), nombres);
            int id = subServiciosDAO.insert(getServicios());
            table.getListTable().add(subServiciosDAO.selectById(count - 1));
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
        } catch (Myexception | ParseException myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombreSub, jTiempoE);
            Validar.entradaNumerica(jPrecio, jTiempoE);
            Validar.checkValor(jNombreSub.getText(), nombres);
            subServiciosDAO.update(getServicios());
            selectAllServicio();
            tableServicio.refresh();
            btnEditar.setDisable(false);
            btnAgregar.setDisable(true);
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
        } catch (ParseException | Myexception e) {
            new AlertUtil(Estado.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    private SubServicios getServicios() throws ParseException {
        // TODO: 2/13/2018 Delete
        subServicios.setIdsubservicio(count++);
        subServicios.setNombreSub(jNombreSub.getText());
        subServicios.setFechaSub(FechaUtil.getCurrentDate());
        subServicios.setPrecioSub(Double.valueOf(jPrecio.getText()));
        subServicios.setTiempo_estimadoSub(Integer.parseInt(jTiempoE.getText()));
        return subServicios;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        try {
            subServiciosDAO.delete(subServicios.getIdsubservicio());
            table.getListTable().remove(subServicios);
            tableServicio.refresh();
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            subServicios = table.getModel();
            btnEditar.setDisable(true);
            jNombreSub.setText(subServicios.getNombreSub());
            jPrecio.setText(String.valueOf(subServicios.getPrecioSub()));
            jTiempoE.setText(String.valueOf(subServicios.getTiempo_estimadoSub()));
            btnAgregar.setDisable(false);
        }
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombreSub, jPrecio, jTiempoE);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
