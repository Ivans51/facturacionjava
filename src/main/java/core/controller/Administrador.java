package core.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.*;
import core.util.*;
import core.util.TableUtil;
import core.vo.*;
import core.vo.Factura;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Administrador extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public JFXButton btnSalir, btnImprimir, btnConsultar, btnImprimirFactura;
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
    private String comboReportes = "";
    private String[] rangoTiempo = new String[]{"Dia", "Mes", "Rango"};
    private String[] clientesA = {"Cédula", "Nombres", "Apellidos", "Direccion", "Teléfono"};
    private String[] usuariosA = {"Cédula", "Nombre", "Correo", "Fecha", "Status"};
    private String[] facturasA = {"Nombre", "Servicios", "FechaPago", "idfactura", "Total"};
    private String[] serviciosA = {"Id", "Nombre", "Precio", "Fecha", "TiempoEntrega"};
    private String[] auditoriasA = {"Id", "Fecha", "Hora", "Acción", "Usuario"};
    private ArrayList<String> valuesReport = new ArrayList<>();
    private List<String> tipos = new ArrayList<>();
    private Double totales;
    private String comboTime = "Dia";
    private TableUtil<Factura, String> tableFacturaUtil;
    private Factura facturaRow;
    private HashMap<String, Integer> totalCantServ = new HashMap<>();
    private HashMap<String, Double> totalPrecioServ = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePickerDos.setVisible(false);
        lblTotal.setVisible(false);
        stateButton();
        setCombo();
    }

    private void stateButton() {
        // Deshabilitar botones
        TableUtil<Factura, String> tableFactura = getFacturaStringTableUtil();
        switch (Storage.getUsuario().getStatus()) {
            case Estado.TECNICO:
                tipos.add("Factura");
                comboReportes = "Factura";
                tableFactura.getListTable().addAll(addFactura(facturaDAO.joinFacturaCliente()));
                lblTotal.setVisible(true);
                lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));
                break;
            case Estado.GERENTE:
                tipos.add("Auditoria");
                tipos.add("Servicios");
                tipos.add("Cliente");
                tipos.add("Factura");
                tipos.add("Usuario");
                comboReportes = "Auditoria";
                TableUtil<Auditoria, String> tableAuditoria = getAuditoriaStringTableUtil();
                tableAuditoria.getListTable().addAll(addAuditoria(auditoriaDAO.selectAll()));
                break;
            case Estado.ASISTENTE:
                rangoTiempo = new String[]{"Dia"};
                tipos.add("Cliente");
                tipos.add("Factura");
                comboReportes = "Factura";
                eligirTime("Dia");
                lblTotal.setVisible(true);
                lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));
                break;
        }
    }

    private void setCombo() {
        cTime.setItems(FXCollections.observableArrayList(rangoTiempo));
        cTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            btnImprimirFactura.setVisible(false);
            comboTime = newValue;
            datePickerDos.setVisible(newValue.equals("Rango"));
            // setTableFacturaTime(newValue);
        });

        cReportes.setItems(FXCollections.observableArrayList(tipos));
        cReportes.valueProperty().addListener((observable, oldValue, newValue) -> {
            btnImprimirFactura.setVisible(false);
            comboReportes = newValue;
            eligirReporte(newValue);
        });
    }

    private void setDatePicker() {
        datePickerUno.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Dia".equals(comboTime) || "Mes".equals(comboTime))
                setTableFacturaTime();
        });
    }

    private void eligirReporte(String selected) {
        valuesReport.clear();
        lblTotal.setVisible(false);
        btnImprimirFactura.setVisible(false);
        switch (selected) {
            case "Auditoria":
                TableUtil<Auditoria, String> tableAudi = getAuditoriaStringTableUtil();
                tableAudi.getListTable().addAll(addAuditoria(auditoriaDAO.selectAll()));
                break;
            case "Servicios":
                TableUtil<Servicios, String> tableServ = getServiciosStringTableUtil();
                tableServ.getListTable().addAll(addServicios(serviciosDAO.selectAll()));
                break;
            case "Cliente":
                TableUtil<Cliente, String> tableCliente = getClienteStringTableUtil();
                tableCliente.getListTable().addAll(addClientes(clienteDAO.selectAll()));
                break;
            case "Factura":
                TableUtil<Factura, String> tableFac = getFacturaStringTableUtil();
                tableFac.getListTable().addAll(addFactura(facturaDAO.joinFacturaCliente()));
                lblTotal.setVisible(true);
                lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));
                break;
            case "Usuario":
                TableUtil<Usuario, String> tableUsuario = getUsuarioStringTableUtil();
                tableUsuario.getListTable().addAll(addUsuarios(usuarioDAO.selectAll()));
                break;
        }
    }

    // Usuario
    private TableUtil<Usuario, String> getUsuarioStringTableUtil() {
        setHeaders(usuariosA);
        String[] columA = {"cedula", "nombre", "correo", "fechaEdit", "correo"};
        TableUtil<Usuario, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Usuario.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);
        return table;
    }

    private List<Usuario> addUsuarios(List<Usuario> usuarioList) {
        for (Usuario servicios : usuarioList) {
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
        }
        usuarioList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getCedula()));
            valuesReport.add(String.valueOf(it.getNombre()));
            valuesReport.add(String.valueOf(it.getCorreo()));
            valuesReport.add(String.valueOf(it.getFechaEdit()));
            valuesReport.add(String.valueOf(it.getCorreo()));
        });
        return usuarioList;
    }

    // Factura
    private TableUtil<Factura, String> getFacturaStringTableUtil() {
        setHeaders(facturasA);
        String[] columA = {"nombreCliente", "servicios", "fecha_pagoEdit", "idfactura", "totalEdit"};
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableFacturaUtil = new TableUtil(Factura.class, tableReport);
        tableFacturaUtil.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);

        final ObservableList<Factura> tablaSelecionada = tableReport.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Factura>) c -> tableFacturaUtil.seleccionarTabla(this));

        return tableFacturaUtil;
    }

    private List<Factura> addFactura(List<Factura> facturaList) {
        totales = 0.0;
        for (Factura factura : facturaList) {
            factura.setFecha_pagoEdit(FechaUtil.getDateFormat(factura.getFecha_pago()));
            factura.setTotalEdit(String.format("%1$,.2f", factura.getTotal()) + " Bs");
            String cliente = factura.getCliente().getNombres() + " " + factura.getCliente().getApellidos();
            factura.setNombreCliente(cliente);
        }
        facturaList.forEach(it -> {
            String cliente = it.getCliente().getNombres() + " " + it.getCliente().getApellidos();
            valuesReport.add(cliente);
            valuesReport.add(String.valueOf(it.getServicios()));
            valuesReport.add(String.valueOf(it.getFecha_pagoEdit()));
            valuesReport.add(String.valueOf(it.getDuracion()));
            totales += it.getTotal();
            valuesReport.add(String.valueOf(totales));
        });
        return facturaList;
    }

    // Clientes
    private TableUtil<Cliente, String> getClienteStringTableUtil() {
        setHeaders(clientesA);
        String[] columA = {"cedula", "nombres", "apellidos", "direccion", "telefono"};
        TableUtil<Cliente, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Cliente.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);
        return table;
    }

    private List<Cliente> addClientes(List<Cliente> clienteList) {
        clienteList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getCedula()));
            valuesReport.add(it.getNombres());
            valuesReport.add(it.getApellidos());
            valuesReport.add(it.getDireccion());
            valuesReport.add(it.getTelefono());
        });
        return clienteList;
    }

    // Servicios
    private TableUtil<Servicios, String> getServiciosStringTableUtil() {
        setHeaders(serviciosA);
        String[] columA = {"idservicios", "Nombre", "PrecioEdit", "FechaEdit", "tiempo_estimado"};
        TableUtil<Servicios, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Servicios.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);
        return table;
    }

    private List<Servicios> addServicios(List<Servicios> serviciosList) {
        for (Servicios servicios : serviciosList) {
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
            servicios.setPrecioEdit(String.format("%1$,.2f", servicios.getPrecio()) + " Bs");
        }
        serviciosList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdservicios()));
            valuesReport.add(it.getNombre());
            valuesReport.add(String.valueOf(it.getPrecioEdit()));
            valuesReport.add(String.valueOf(it.getFechaEdit()));
            valuesReport.add(String.valueOf(it.getTiempo_estimado()));
        });
        return serviciosList;
    }

    // Auditoria
    private TableUtil<Auditoria, String> getAuditoriaStringTableUtil() {
        setHeaders(auditoriasA);
        String[] columA = {"idAuditoria", "FechaEdit", "Hora", "Accion", "nombreUsuario"};
        TableUtil<Auditoria, String> table;
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Auditoria.class, tableReport);
        table.inicializarTabla(columA, tbId, tbFecha, tbHora, tbAcion, tbUsuario);
        return table;
    }

    private List<Auditoria> addAuditoria(List<Auditoria> auditoriaList) {
        for (Auditoria auditoria : auditoriaList) {
            auditoria.setFechaEdit(FechaUtil.getDateFormat(auditoria.getFecha()));
        }
        auditoriaList.forEach(it -> {
            valuesReport.add(String.valueOf(it.getIdAuditoria()));
            valuesReport.add(String.valueOf(it.getFechaEdit()));
            valuesReport.add(it.getHora());
            valuesReport.add(String.valueOf(it.getAccion()));
            valuesReport.add(String.valueOf(it.getNombreUsuario()));
        });
        return auditoriaList;
    }

    // With time
    public void actionConsultar(ActionEvent actionEvent) {
        btnImprimirFactura.setVisible(false);
        if (comboTime != null && !"".equals(comboTime) && datePickerUno.getValue() != null)
            eligirTime(comboReportes);
        else
            new AlertUtil(Estado.EXITOSA, "Falta valores por seleccionar");
    }

    private void eligirTime(String selected) {
        valuesReport.clear();
        lblTotal.setVisible(false);
        switch (selected) {
            case "Auditoria":
                setTableAuditoriaTime();
                break;
            case "Servicios":
                setTableServicioTime();
                break;
            case "Cliente":
                setTableClienteTime();
                break;
            case "Factura":
                setTableFacturaTime();
                break;
            case "Usuario":
                setTableUsuarioTime();
                break;
        }
    }

    private void setTableAuditoriaTime() {
        TableUtil<Auditoria, String> table = getAuditoriaStringTableUtil();

        List<Auditoria> auditoriaList = new ArrayList<>();
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Auditoria auditoria = new Auditoria();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    auditoria.setIdAuditoria(timeOne.getDayOfMonth());
                    auditoria.setHora(String.valueOf(timeOne.getMonthValue()));
                    auditoria.setUsuario_cedula(timeOne.getYear());
                    auditoriaList = auditoriaDAO.selectByDia(auditoria);
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        auditoria.setAccion(String.valueOf(Date.valueOf(timeOne)));
                        auditoria.setNombreUsuario(String.valueOf(Date.valueOf(timeTwo)));
                        auditoriaList = auditoriaDAO.selectByRango(auditoria);
                    }
                    break;
                case "Mes":
                    auditoria.setHora(String.valueOf(timeOne.getMonthValue()));
                    auditoria.setUsuario_cedula(timeOne.getYear());
                    auditoriaList = auditoriaDAO.selectByMes(auditoria);
                    break;
            }
            table.getListTable().addAll(addAuditoria(auditoriaList));
        }
    }

    private void setTableServicioTime() {
        TableUtil<Servicios, String> table = getServiciosStringTableUtil();

        List<Servicios> serviciosList = new ArrayList<>();
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Servicios servicios = new Servicios();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    servicios.setIdservicios(timeOne.getDayOfMonth());
                    servicios.setNombre(String.valueOf(timeOne.getMonthValue()));
                    servicios.setDescripcion(String.valueOf(timeOne.getYear()));
                    serviciosList = serviciosDAO.selectByDia(servicios);
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        servicios.setNombre(String.valueOf(Date.valueOf(timeOne)));
                        servicios.setDescripcion(String.valueOf(Date.valueOf(timeTwo)));
                        serviciosList = serviciosDAO.selectByRango(servicios);
                    }
                    break;
                case "Mes":
                    servicios.setNombre(String.valueOf(timeOne.getMonthValue()));
                    servicios.setDescripcion(String.valueOf(timeOne.getYear()));
                    serviciosList = serviciosDAO.selectByMes(servicios);
                    break;
            }
            table.getListTable().addAll(addServicios(serviciosList));
        }
    }

    private void setTableClienteTime() {
        TableUtil<Cliente, String> table = getClienteStringTableUtil();

        List<Cliente> clienteList = new ArrayList<>();
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Cliente cliente = new Cliente();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    cliente.setCedula(timeOne.getDayOfMonth());
                    cliente.setNombres(String.valueOf(timeOne.getMonthValue()));
                    cliente.setApellidos(String.valueOf(timeOne.getYear()));
                    clienteList = clienteDAO.selectByDia(cliente);
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        cliente.setNombres(String.valueOf(Date.valueOf(timeOne)));
                        cliente.setApellidos(String.valueOf(Date.valueOf(timeTwo)));
                        clienteList = clienteDAO.selectByRango(cliente);
                    }
                    break;
                case "Mes":
                    cliente.setNombres(String.valueOf(timeOne.getMonthValue()));
                    cliente.setApellidos(String.valueOf(timeOne.getYear()));
                    clienteList = clienteDAO.selectByMes(cliente);
                    break;
            }
            table.getListTable().addAll(addClientes(clienteList));
        }
    }

    private void setTableFacturaTime() {
        TableUtil<Factura, String> table = getFacturaStringTableUtil();

        List<Factura> facturaList = new ArrayList<>();
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Factura factura = new Factura();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    factura.setIdfactura(timeOne.getDayOfMonth());
                    factura.setCliente_cedula(timeOne.getMonthValue());
                    factura.setUsuario_cedula(timeOne.getYear());
                    facturaList = facturaDAO.selectByDia(factura);
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        factura.setFecha_pago(Date.valueOf(timeOne));
                        factura.setDuracion(String.valueOf(Date.valueOf(timeTwo)));
                        facturaList = facturaDAO.selectByRango(factura);
                    }
                    break;
                case "Mes":
                    factura.setCliente_cedula(timeOne.getMonthValue());
                    factura.setUsuario_cedula(timeOne.getYear());
                    facturaList = facturaDAO.selectByMes(factura);
                    break;
            }
            table.getListTable().addAll(addFactura(facturaList));
            lblTotal.setVisible(true);
            lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));
        }
    }

    private void setTableUsuarioTime() {
        TableUtil<Usuario, String> table = getUsuarioStringTableUtil();

        List<Usuario> usuarioList = new ArrayList<>();
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Usuario usuario = new Usuario();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    usuario.setCedula(timeOne.getDayOfMonth());
                    usuario.setNombre(String.valueOf(timeOne.getMonthValue()));
                    usuario.setClave(String.valueOf(timeOne.getYear()));
                    usuarioList = usuarioDAO.selectByDia(usuario);
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        usuario.setNombre(String.valueOf(Date.valueOf(timeOne)));
                        usuario.setClave(String.valueOf(Date.valueOf(timeTwo)));
                        usuarioList = usuarioDAO.selectByRango(usuario);
                    }
                    break;
                case "Mes":
                    usuario.setNombre(String.valueOf(timeOne.getMonthValue()));
                    usuario.setClave(String.valueOf(timeOne.getYear()));
                    usuarioList = usuarioDAO.selectByMes(usuario);
                    break;
            }
            table.getListTable().addAll(addUsuarios(usuarioList));
        }
    }

    private void setHeaders(String... name) {
        tbId.setText(name[0]);
        tbFecha.setText(name[1]);
        tbHora.setText(name[2]);
        tbAcion.setText(name[3]);
        tbUsuario.setText(name[4]);
    }

    public void actionImprimir(ActionEvent actionEvent) {
        new PrintSelection(comboReportes, auditoriasA, serviciosA, clientesA, facturasA, usuariosA)
                .printAction(this::imprimirPDF);
    }

    private void imprimirPDF(String[] strings, String file, String nameAction) {
        try {
            DateTime d = new DateTime();
            String title = "Inversiones Todo Frío C.A. " +
                    "\nj-29441763-9 \nDireccion: Avenida los Cedros Cruce C/C Junin Local 105-C Barrio Lourdes Maracay " +
                    "\n " + nameAction;
            String sub = "Factura del día: " + d.getDayOfMonth() + "/" + d.getMonthOfYear() + "/" + d.getYear();
            String imagePath = "src/main/resources/images/FacturaLogo.png";
            PDFCreator pdfCreator = new PDFCreator(file);
            pdfCreator.createPDF(documento -> {
                Paragraph paragraphRight = pdfCreator.setParagraph(title, Element.ALIGN_RIGHT, 10, 14, Font.BOLD);
                Paragraph paragraphLeft = pdfCreator.setParagraph(sub, Element.ALIGN_LEFT, 10, 12, Font.NORMAL);
                Image imgLogo = pdfCreator.setImagePDF(imagePath, 150, 100, Element.ALIGN_LEFT);
                PdfPTable tableTitle = pdfCreator.setTablePDF(new float[]{260, 260}, tabla -> {
                    PdfPCell cellLeft = pdfCreator.setCellPDF(Element.ALIGN_LEFT, Rectangle.NO_BORDER, imgLogo, paragraphLeft);
                    tabla.addCell(cellLeft);
                    PdfPCell cellRight = pdfCreator.setCellPDF(Element.ALIGN_RIGHT, Rectangle.NO_BORDER, paragraphRight);
                    tabla.addCell(cellRight);
                }, false);
                documento.add(tableTitle);

                PdfPTable table = pdfCreator.setTablePDF(new float[]{40, 220, 130, 130}, (PdfPTable tabla) -> {
                    Arrays.stream(strings).forEach(tabla::addCell);
                    valuesReport.forEach(tabla::addCell);
                }, false);
                documento.add(table);
            });
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        btnImprimirFactura.setVisible(true);
        if (tableFacturaUtil.getTablaSeleccionada(tableReport) != null) {
            facturaRow = facturaDAO.selectById(tableFacturaUtil.getModel().getIdfactura());
            // Cliente cliente = clienteDAO.selectById(facturaRow.getCliente_cedula());
        }
    }

    public void actionImprimirFactura(ActionEvent actionEvent) {
        new PDFCreator("reports/" + facturaRow.getNameFile()).openPDF();
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
