package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.GastosDAO;
import core.util.*;
import core.vo.Gastos;
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

public class RegistroGastos extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jMonto, jConcepto, jCuenta, jBuscar;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEliminar;
    public TableView<Gastos> tableGastos;
    public AnchorPane anchorPane;
    public TableColumn tbMonto, tbConcepto, tbTipoPago, tbNumeroCuenta, tbFecha;
    public ComboBox<String> cTipoPago;

    private TableUtil<Gastos, String> table;
    private GastosDAO gastosDAO = new GastosDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Gastos> gastosList = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private Gastos gastos;
    private boolean stateEdit = false;
    private String[] tiposPago = {"Efectivo", "Transferencia", "Cheque"};
    private String[] columS = {"monto", "concepto", "tipoPago", "ncuenta", "fecha"};
    private String[] field = {"Monto", "Concepto", "Nº Cuenta"};
    private String[] fieldNumber = {"Monto", "Nº Cuenta"};
    private String[] fieldString = {"Tipo de Pago"};
    private int[] rangeLimitCuenta = {11, 11};
    private String tipoPagoSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cTipoPago.getItems().addAll(tiposPago);
        cTipoPago.valueProperty().addListener((observable, oldValue, newValue) -> {
            tipoPagoSelected = newValue;
        });
        Validar.getValueLimit(cTipoPago, "");
        setTable();
        setBuscarTableGastos();
    }

    private void setTable() {
        tableGastos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Gastos.class, tableGastos);
        table.inicializarTabla(columS, tbMonto, tbConcepto, tbTipoPago, tbNumeroCuenta);

        final ObservableList<Gastos> tablaSelecionada = tableGastos.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Gastos>) c -> table.seleccionarTabla(this));

        selectAllGastos();
        table.getListTable().addAll(gastosList);
    }

    private void setBuscarTableGastos() {
        ObservableList<Gastos> data = table.getData();
        jBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length()))
                tableGastos.setItems(data);
            table.searchMultiple(newValue.toLowerCase());
        });
    }

    private void selectAllGastos() {
        gastosList = gastosDAO.selectAll();
        gastosList.forEach(it -> cedulas.add(String.valueOf(it.getIdgastos())));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.limitField(rangeLimitCuenta, "Nº cuenta", jCuenta);
            if (tipoPagoSelected.equals("Efectivo"))
                Validar.campoVacio(field, jMonto, jConcepto);
            else
                Validar.campoVacio(field, jMonto, jConcepto, jCuenta);
            Validar.stringVacio(fieldString, cTipoPago.getSelectionModel().getSelectedItem());
            Validar.isNumber(fieldNumber, jMonto, jCuenta);
            elegirConsulta();
            tableGastos.refresh();
            Validar.limmpiarCampos(jMonto, jConcepto, jCuenta, jBuscar);
        } catch (ParseException | Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void elegirConsulta() throws Myexception, ParseException {
        if (!stateEdit) {
            gastosDAO.insert(getGastosInsert());
            Gastos gastos = gastosDAO.selectById(gastosDAO.selectLastID().getIdgastos());
            table.getListTable().add(gastos);
            new AlertUtil(Estado.EXITOSA, "Gasto creado correctamente");
            new AuditoriaUtil().insertar("Registro del gastos");
        } else {
            stateViewEdit(false);
            gastosDAO.update(getGastosUpdate());
            selectAllGastos();
            new AlertUtil(Estado.EXITOSA, "Gasto modificado correctamente");
            new AuditoriaUtil().insertar("Gasto Actualizado");
        }
    }

    // Modelo que se envia a la BD
    private Gastos getGastosInsert() throws ParseException {
        Gastos gastos = new Gastos();
        gastos.setMonto(jMonto.getText());
        gastos.setConcepto(jConcepto.getText());
        gastos.setNcuenta(jCuenta.getText());
        gastos.setFecha(FechaUtil.getCurrentDate());
        gastos.setUsuario_cedula(Storage.getUsuario().getCedula());
        return gastos;
    }

    private Gastos getGastosUpdate() {
        gastos.setMonto(jMonto.getText());
        gastos.setConcepto(jConcepto.getText());
        gastos.setNcuenta(jCuenta.getText());
        gastos.setUsuario_cedula(Storage.getUsuario().getCedula());
        return gastos;
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            gastos = table.getModel();
            jMonto.setText(gastos.getMonto());
            jConcepto.setText(gastos.getConcepto());
            jCuenta.setText(gastos.getNcuenta());
            cTipoPago.getSelectionModel().select(gastos.getTipoPago());
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean edit) {
        stateEdit = edit;
        /*if (Storage.getUsuario().getStatus().equalsIgnoreCase(Estado.GERENTE))
            btnEliminar.setVisible(edit);*/
        /*jCedula.setDisable(edit);
        cNacionalidad.setDisable(edit);*/
        btnAgregar.setText(edit ? "Editar" : "Agregar");
        btnLimpiar.setText(edit ? "Cancelar" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        if (stateEdit) stateViewEdit(false);
        Validar.limmpiarCampos(jMonto, jConcepto, jCuenta, jBuscar);
        cTipoPago.getSelectionModel().clearSelection();
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionEliminar(ActionEvent actionEvent) {
        Gastos gastos = new Gastos();
        gastos.setIdgastos(this.gastos.getIdgastos());
        gastos.setEstado(0);
        gastosDAO.updateEstado(gastos);
        table.getListTable().remove(this.gastos);
        tableGastos.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
    }
}
