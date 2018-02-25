package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.*;
import core.util.ManagerFXML;
import core.util.PDFCreator;
import core.util.Route;
import core.util.TableUtil;
import core.vo.*;
import core.vo.Factura;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Administrador extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public JFXButton btnAuditoria, btnSalir, btnImprimir;
    public ComboBox<String> cReportes;
    public TableView tableReport;
    public TableColumn tbId, tbFecha, tbHora, tbAcion, tbUsuario;

    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private String selected = "";
    private String[] clientesA = {"Cedula", "Nombres", "Apellidos", "Direccion", "Teléfono"};
    private String[] usuariosA = {"Cedula", "Nombre", "Correo", "Fecha", "Status"};
    private String[] facturasA = {"IdFactura", "Servicios", "FechaPago", "IVA", "Total"};
    private String[] subServicioA = {"Id", "NombreSub", "PrecioSub", "FechaSub", "Tiempo_estimadoSub"};
    private String[] serviciosA = {"Id", "Nombre", "Precio", "Fecha", "TiempoE"};
    private String[] auditoriasA = {"Id", "Fecha", "Hora", "Accion", "Usuario"};
    private ArrayList<String> valuesReport = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCombo();
        selected = "Auditoria";
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
            selected = newValue;
            eligirAccion(newValue);
        });
    }

    private void eligirAccion(String selected) {
        valuesReport.clear();
        switch (selected) {
            case "Servicios":
                setTableServicios();
                break;
            case "Subservicios":
                setTableSubServicios();
                break;
            case "Clliente":
                setTableCliente();
                break;
            case "Factura":
                setTableFactura();
                break;
            case "Usuario":
                setTableUsuario();
                break;
        }
    }

    private void setTableUsuario() {
        setHeaders(usuariosA);
        String[] columA = {"cedula", "nombre", "correo", "fecha", "status"};
        TableUtil<Usuario, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Usuario.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Usuario> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Usuario>) c -> table.seleccionarTabla(this));

        List<Usuario> usuarioList = usuarioDAO.selectAll();
        usuarioList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getCedula()));
            valuesReport.add(String.valueOf(it.getNombre()));
            valuesReport.add(String.valueOf(it.getCorreo()));
            valuesReport.add(String.valueOf(it.getFecha()));
            valuesReport.add(String.valueOf(it.getStatus()));
        });
        table.getListTable().addAll(usuarioList);
    }

    private void setTableFactura() {
        setHeaders(facturasA);
        String[] columA = {"idfactura", "servicios", "fecha_pago", "IVA", "total"};
        TableUtil<Factura, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Factura.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Factura> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Factura>) c -> table.seleccionarTabla(this));

        List<Factura> facturaList = facturaDAO.selectAll();
        facturaList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdfactura()));
            valuesReport.add(String.valueOf(it.getServicios()));
            valuesReport.add(String.valueOf(it.getFecha_pago()));
            valuesReport.add(String.valueOf(it.getIVA()));
            valuesReport.add(String.valueOf(it.getTotal()));
        });
        table.getListTable().addAll(facturaList);
    }

    private void setTableCliente() {
        setHeaders(clientesA);
        String[] columA = {"cedula", "nombres", "apellidos", "direccion", "telefono"};
        TableUtil<Cliente, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Cliente.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Cliente> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Cliente>) c -> table.seleccionarTabla(this));

        List<Cliente> clienteList = clienteDAO.selectAll();
        clienteList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getCedula()));
            valuesReport.add(String.valueOf(it.getNombres()));
            valuesReport.add(String.valueOf(it.getApellidos()));
            valuesReport.add(String.valueOf(it.getDireccion()));
            valuesReport.add(String.valueOf(it.getTelefono()));
        });
        table.getListTable().addAll(clienteList);
    }

    private void setTableSubServicios() {
        setHeaders(subServicioA);
        String[] columA = {"idsubservicio", "Nombre", "Costo", "Fecha", "tiempo_estimado"};
        TableUtil<SubServicios, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(SubServicios.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<SubServicios> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<SubServicios>) c -> table.seleccionarTabla(this));

        List<SubServicios> subServiciosList = subServiciosDAO.selectAll();
        subServiciosList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdsubservicio()));
            valuesReport.add(String.valueOf(it.getNombreSub()));
            valuesReport.add(String.valueOf(it.getPrecioSub()));
            valuesReport.add(String.valueOf(it.getFechaSub()));
            valuesReport.add(String.valueOf(it.getTiempo_estimadoSub()));
        });
        table.getListTable().addAll(subServiciosList);
    }

    private void setTableServicios() {
        setHeaders(serviciosA);
        String[] columA = {"idservicios", "Nombre", "Precio", "Fecha", "tiempo_estimado"};
        TableUtil<Servicios, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Servicios.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Servicios> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> table.seleccionarTabla(this));

        List<Servicios> serviciosList = serviciosDAO.selectAll();
        serviciosList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdservicios()));
            valuesReport.add(String.valueOf(it.getNombre()));
            valuesReport.add(String.valueOf(it.getPrecio()));
            valuesReport.add(String.valueOf(it.getFecha()));
            valuesReport.add(String.valueOf(it.getTiempo_estimado()));
        });
        table.getListTable().addAll(serviciosList);
    }

    private void setTableAuditoria() {
        setHeaders(auditoriasA);
        String[] columA = {"idauditoria", "Fecha", "Hora", "Accion", "nombreUsuario"};
        TableUtil<Auditoria, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Auditoria.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Auditoria> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Auditoria>) c -> table.seleccionarTabla(this));

        List<Auditoria> auditoriaList = auditoriaDAO.selectAll();
        auditoriaList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdAuditoria()));
            valuesReport.add(String.valueOf(it.getFecha()));
            valuesReport.add(String.valueOf(it.getHora()));
            valuesReport.add(String.valueOf(it.getAccion()));
            valuesReport.add(String.valueOf(it.getNombreUsuario()));
        });
        table.getListTable().addAll(auditoriaList);
    }

    private void setHeaders(String... name) {
        tbId.setText(name[0]);
        tbFecha.setText(name[1]);
        tbHora.setText(name[2]);
        tbAcion.setText(name[3]);
        tbUsuario.setText(name[4]);
    }

    public void actionAuditoria(ActionEvent actionEvent) {
        selected = "Auditoria";
        setTableAuditoria();
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionImprimir(ActionEvent actionEvent) {
        DateTime d = new DateTime();
        String fecha = d.getHourOfDay() + "-" + d.getMinuteOfHour() + "-" + d.getSecondOfMinute();
        switch (selected) {
            case "Auditoria":
                String aut = "Auditoria-";
                imprimirAuditoria(auditoriasA, aut + fecha + ".pdf", aut);
                break;
            case "Servicios":
                String ser = "Servicios-";
                imprimirAuditoria(serviciosA, ser + fecha + ".pdf", ser);
                break;
            case "Subservicios":
                String sub = "SubServicios-";
                imprimirAuditoria(subServicioA, sub + fecha + ".pdf", sub);
                break;
            case "Clliente":
                String cli = "Clientes-";
                imprimirAuditoria(clientesA, cli + fecha + ".pdf", cli);
                break;
            case "Factura":
                String fac = "Facturas-";
                imprimirAuditoria(facturasA, fac + fecha + ".pdf", fac);
                break;
            case "Usuario":
                String use = "Usuario-";
                imprimirAuditoria(usuariosA, use + fecha + ".pdf", use);
                break;
        }
    }

    private void imprimirAuditoria(String[] strings, String file, String name) {
        try {
            PDFCreator pdfCreator = new PDFCreator(file, "Listado de "+ name, "Documento generado");
            pdfCreator.setFontTitle(pdfCreator.family, 14, Font.BOLD, pdfCreator.background);
            pdfCreator.setFontSub(pdfCreator.family, 12, Font.ITALIC, pdfCreator.background);
            pdfCreator.crearPDF(5, (PdfPTable tabla) -> {
                Arrays.stream(strings).forEach(tabla::addCell);
                valuesReport.forEach(tabla::addCell);
            });
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {

    }
}
