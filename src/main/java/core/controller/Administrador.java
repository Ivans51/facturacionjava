package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.*;
import core.util.*;
import core.util.TableUtil;
import core.vo.*;
import core.vo.Factura;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Administrador extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public JFXButton btnSalir, btnImprimir, btnConsultar, btnCambiarModo;
    public ComboBox<String> cReportes, cTime;
    public TableView tableReport;
    public TableColumn tbId, tbFecha, tbHora, tbAcion, tbUsuario;
    public DatePicker datePickerUno, datePickerDos;
    public Label lblTotal;

    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private String selected = "";
    private String[] rangoTiempo = {"Día", "Mes"};
    private String[] clientesA = {"Cédula", "Nombres", "Apellidos", "Direccion", "Teléfono"};
    private String[] usuariosA = {"Cédula", "Nombre", "Correo", "Fecha", "Status"};
    private String[] facturasA = {"IdFactura", "Servicios", "FechaPago", "IVA", "Total"};
    private String[] serviciosA = {"Id", "Nombre", "Precio", "Fecha", "TiempoE"};
    private String[] auditoriasA = {"Id", "Fecha", "Hora", "Accion", "Usuario"};
    private ArrayList<String> valuesReport = new ArrayList<>();
    private List<String> tipos = new ArrayList<>();
    private Double totales;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateButton();
        setCombo();
    }

    private void stateButton() {
        // Deshabilitar botones
        switch (Storage.getUsuario().getStatus()) {
            case Estado.TECNICO:
                tipos.add("Factura");
                selected = "Factura";
                setTableFactura();
                break;
            case Estado.GERENTE:
                tipos.add("Auditoria");
                tipos.add("Servicios");
                tipos.add("Cliente");
                tipos.add("Factura");
                tipos.add("Usuario");
                selected = "Auditoria";
                setTableAuditoria();
                break;
            case Estado.ASISTENTE:
                tipos.add("Cliente");
                tipos.add("Factura");
                selected = "Factura";
                // Tabla de inicio
                setTableFactura();
                break;
        }
    }

    private void setCombo() {
        cTime.setItems(FXCollections.observableArrayList(rangoTiempo));
        cTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            setTableFacturaTime(newValue);
        });

        cReportes.setItems(FXCollections.observableArrayList(tipos));
        cReportes.valueProperty().addListener((observable, oldValue, newValue) -> {
            selected = newValue;
            eligirAccion(newValue);
        });
    }

    private void eligirAccion(String selected) {
        valuesReport.clear();
        switch (selected) {
            case "Auditoria":
                setTableAuditoria();
                break;
            case "Servicios":
                setTableServicios();
                break;
            case "Cliente":
                setTableCliente();
                break;
            case "Factura":
                setTableFactura();
                break;
            case "Usuario":
                setTableUsuario();
                break;
        }
        lblTotal.setVisible(false);
    }

    private void setTableUsuario() {
        lblTotal.setVisible(false);
        setHeaders(usuariosA);
        String[] columA = {"cedula", "nombre", "correo", "fecha", "status"};
        TableUtil<Usuario, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Usuario.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

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
        lblTotal.setVisible(true);
        setHeaders(facturasA);
        String[] columA = {"idfactura", "servicios", "fecha_pago", "IVA", "total"};
        TableUtil<Factura, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Factura.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        List<Factura> facturaList = facturaDAO.selectAll();
        addFactura(facturaList);
        table.getListTable().addAll(facturaList);
    }

    private void setTableFacturaTime(String time) {
        setHeaders(facturasA);
        String[] columA = {"idfactura", "servicios", "fecha_pago", "IVA", "total"};
        TableUtil<Factura, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Factura.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        List<Factura> facturaList = new ArrayList<>();
        LocalDate value = datePickerUno.getValue();
        Factura factura = new Factura();
        switch (time) {
            case "Día":
                factura.setIdfactura(value.getDayOfMonth());
                factura.setCliente_cedula(value.getMonthValue());
                factura.setUsuario_cedula(value.getYear());
                facturaList = facturaDAO.selectByDia(factura);
                break;
            case "Semana":
                factura.setIdfactura(value.getDayOfMonth());
                factura.setCliente_cedula(value.getMonthValue());
                factura.setUsuario_cedula(value.getYear());
                facturaList = facturaDAO.selectBySemana(factura);
                break;
            case "Mes":
                factura.setCliente_cedula(value.getMonthValue());
                factura.setUsuario_cedula(value.getYear());
                facturaList = facturaDAO.selectByMes(factura);
                break;
        }
        addFactura(facturaList);
        lblTotal.setVisible(true);
        lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));
        table.getListTable().addAll(facturaList);
    }

    private void addFactura(List<Factura> facturaList) {
        totales = 0.0;
        facturaList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdfactura()));
            valuesReport.add(String.valueOf(it.getServicios()));
            valuesReport.add(String.valueOf(it.getFecha_pago()));
            valuesReport.add(String.valueOf(it.getIVA()));
            totales += it.getTotal();
            valuesReport.add(String.valueOf(totales));
        });
    }

    private void setTableCliente() {
        lblTotal.setVisible(false);
        setHeaders(clientesA);
        String[] columA = {"cedula", "nombres", "apellidos", "direccion", "telefono"};
        TableUtil<Cliente, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Cliente.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

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

    private void setTableServicios() {
        lblTotal.setVisible(false);
        setHeaders(serviciosA);
        String[] columA = {"idservicios", "Nombre", "Precio", "Fecha", "tiempo_estimado"};
        TableUtil<Servicios, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Servicios.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

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
        lblTotal.setVisible(false);
        setHeaders(auditoriasA);
        String[] columA = {"idAuditoria", "Fecha", "Hora", "Accion", "nombreUsuario"};
        TableUtil<Auditoria, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Auditoria.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

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

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionImprimir(ActionEvent actionEvent) {
        DateTime d = new DateTime();
        String fecha = d.getDayOfMonth() + "-" + d.getMonthOfYear() + "-" + d.getYear();
        switch (selected) {
            case "Auditoria":
                String aut = "Auditoria-";
                imprimirAuditoria(auditoriasA, aut + fecha + ".pdf", aut);
                break;
            case "Servicios":
                String ser = "Servicios-";
                imprimirAuditoria(serviciosA, ser + fecha + ".pdf", ser);
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
            DateTime d = new DateTime();
            String timeActual = d.getHourOfDay() + "-" + d.getDayOfMonth() + d.getDayOfYear();
            PDFCreator pdfCreator = new PDFCreator(file, "Listado de " + name, "Fecha: " + timeActual, "");
            pdfCreator.setFontTitle(pdfCreator.family, 14, Font.BOLD, pdfCreator.background);
            pdfCreator.setFontSub(pdfCreator.family, 12, Font.ITALIC, pdfCreator.background);
            pdfCreator.crearPDF(5, (PdfPTable tabla) -> {
                Arrays.stream(strings).forEach(tabla::addCell);
                valuesReport.forEach(tabla::addCell);
            });
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void actionConsultar(ActionEvent actionEvent) {

    }

    public void actionCambiar(ActionEvent actionEvent) {

    }
}
