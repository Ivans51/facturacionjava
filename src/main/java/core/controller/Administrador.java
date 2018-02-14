package core.controller;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.AuditoriaDAO;
import core.dao.ServiciosDAO;
import core.util.ManagerFXML;
import core.util.PDFCreator;
import core.util.Route;
import core.util.TableUtil;
import core.vo.Auditoria;
import core.vo.Servicios;
import core.vo.SubServicios;
import core.vo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Administrador extends ManagerFXML implements Initializable, TableUtil.StatusControles{

    public AnchorPane anchorPane;
    public JFXButton btnAuditoria, btnReportes, btnSalir, btnImprimir;
    public ComboBox<String> cReportes;
    public TableView tableReport;
    public TableColumn tbId, tbFecha, tbHora, tbAcion, tbUsuario;

    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCombo();
        setTableAuditoria();
    }

    private void setCombo() {
        List<String> tipos = new ArrayList<>();
        tipos.add("Servicios");
        tipos.add("Subservicios");
        tipos.add("Clliente");
        tipos.add("Factura");
        tipos.add("Usuario");
        cReportes.setItems(FXCollections.observableArrayList(tipos));
        cReportes.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue){
                case "Servicios":
                    setTableServicios();
                    break;
                case "Subservicios":
                    break;
                case "Clliente":
                    break;
                case "Factura":
                    break;
                case "Usuario":
                    break;
            }
        });
    }

    private void setTableServicios() {
        String[] columA = {"idservicios", "Nombre", "Precio", "Fecha", "tiempo_estimado"};
        TableUtil<Servicios, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Servicios.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Servicios> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> table.seleccionarTabla(this));

        table.getListTable().addAll(serviciosDAO.selectAll());
    }

    private void setTableAuditoria() {
        String[] columA = {"Id", "Fecha", "Hora", "Accion", "Usuario"};
        TableUtil<Auditoria, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Auditoria.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Auditoria> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Auditoria>) c -> table.seleccionarTabla(this));

        table.getListTable().addAll(auditoriaDAO.selectAll());
    }

    public void actionAuditoria(ActionEvent actionEvent) {

    }

    public void actionReportes(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            PDFCreator pdfCreator = new PDFCreator();
            pdfCreator.crearPDF("Registro", "Este es el listado", 5, () -> {

            });
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {

    }
}
