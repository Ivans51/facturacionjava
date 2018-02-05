package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.AuditoriaDAO;
import core.util.ManagerFXML;
import core.util.TableUtil;
import core.vo.Auditoria;
import core.vo.Cliente;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Administrador extends ManagerFXML implements Initializable, TableUtil.StatusControles{

    public AnchorPane anchorPane;
    public JFXButton btnAuditoria, btnReportes, btnSalir, btnImprimir;
    public ComboBox cReportes;
    public TableView<Auditoria> tableReport;
    public TableColumn tbId, tbFecha, tbHora, tbAcion, tbUsuario;

    private TableUtil<Auditoria, String> table;
    private String[] columA = {"Id", "Fecha", "Hora", "Accion", "Usuario"};
    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Auditoria> auditorias = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableUtil(Auditoria.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Auditoria> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Auditoria>) c -> table.seleccionarTabla(this));

        selectAllAuditoria();
        table.getListTable().addAll(auditorias);
    }

    private void selectAllAuditoria() {
        auditorias = auditoriaDAO.selectAll();
    }

    public void actionAuditoria(ActionEvent actionEvent) {

    }

    public void actionReportes(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {

    }

    public void actionImprimir(ActionEvent actionEvent) {

    }

    @Override
    public void setStatusControls() {

    }
}
