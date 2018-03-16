package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.FacturaDAO;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Factura extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios;
    public TextField jPrecio, jFecha, jCedula, jNombre, jCiudad, jTelefono;
    public JFXButton btnAgregar, btnSalir, btnBuscar, btnImprimir, btnElegir;

    private TableUtil<Cliente, String> tableClienteUtil;
    public TableView<Cliente> tableCliente;
    public TableColumn tbCedula, tbNombreCliente, tbCiudad, tbFecha, tbTelefono;

    private TableUtil<Cliente, String> tableServicioUtil;
    public TableView<Servicios> tableServicio;
    public TableColumn tbID, tbNombreServicio, tbPrecio, tbTiempoEstimado;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private HashMap<String, Double> totalArt = new HashMap<>();
    private int tiempoMaximo = 0;
    private double iva, subTotal, totalPagar;

    private String[] columS = {"cedula", "nombres", "apellidos", "direccion", "telefono"};
    private List<Cliente> clientes = new ArrayList<>();
    private List<Servicios> servicios = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setComboServicio();
        setTableCliente();
        setTableServicio();
    }

    private void setTableCliente() {
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableServicioUtil = new TableUtil(Servicios.class, tableServicio);
        tableServicioUtil.inicializarTabla(columS, tbCedula, tbNombreCliente, tbCiudad, tbFecha, tbTelefono);

        final ObservableList<Servicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> tableServicioUtil.seleccionarTabla(this));

        clientes = clienteDAO.selectAll();
        tableServicioUtil.getListTable().addAll(clientes);
    }

    private void setTableServicio() {
        tableCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableClienteUtil = new TableUtil(Cliente.class, tableCliente);
        tableClienteUtil.inicializarTabla(columS, tbID, tbNombreServicio, tbPrecio, tbTiempoEstimado);

        final ObservableList<Cliente> tablaSelecionada = tableCliente.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Cliente>) c -> tableClienteUtil.seleccionarTabla(this));

        clientes = clienteDAO.selectAll();
        tableClienteUtil.getListTable().addAll(clientes);
    }

    // Muestra los servicios
    private void setComboServicio() {
        HashMap<String, Integer> nombres = new HashMap<>();
        List<Servicios> servicios = serviciosDAO.selectAllFilter();
        servicios.forEach(servicio -> nombres.put(servicio.getNombre(), servicio.getIdservicios()));
        nombres.forEach((key, value) -> cServicios.getItems().add(key));
        cServicios.valueProperty().addListener((observable, oldValue, newValue) -> {
            Servicios select = serviciosDAO.selectByNombre(newValue);
            if (select != null) {
                jPrecio.setText(String.valueOf(select.getPrecio()));
                jFecha.setText(FechaUtil.getDateFormat(select.getFecha()));
                int tiempo = Integer.parseInt(select.getTiempo_estimado());
                if (tiempo > tiempoMaximo)
                    tiempoMaximo = tiempo;
            }
        });
    }

    // Despliega los servicios y subservicios agregados
    public void actionAgregar(ActionEvent actionEvent) {
        String item = cServicios.getSelectionModel().getSelectedItem();
        if (item != null && !"".equals(item)) {
            //setComboAgregados(item);
            setTotal(item);
            limpiar();
        } else {
            new AlertUtil(Estado.ERROR, "Selecciona un servicio y subservicio");
        }
    }

    // A medida que se agregan los servicio se suma el precio total del servicio
    private void setTotal(String item) {
        Servicios serv = serviciosDAO.selectByNombre(item);
        totalArt.put(serv.getNombre(), serv.getPrecio());
        totalPagar += serv.getPrecio();
        // lblTotal.setText(String.valueOf(totalPagar));
    }

    // Una vez que se agrega el servicio si limpia los de arriba
    private void limpiar() {
        jPrecio.setText("");
        jFecha.setText("");
        cServicios.getSelectionModel().clearSelection();
    }

    // Busca los clientes de la empresa
    public void actionBuscar(ActionEvent actionEvent) {
        Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
        if (cliente == null)
            abrirStageStyle(Route.ClienteDialog, "Agregar Cliente", Modality.WINDOW_MODAL, null,
                    false, StageStyle.TRANSPARENT, () -> {
                        DialogCliente display = ManagerFXML.getFxmlLoader().getController();
                        display.setModel(jCedula.getText());
                    });
        else {
            new AlertUtil(Estado.EXITOSA, "Usuario registrado en el sistema");
        }
    }

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            if (totalArt.size() > 0) {
                calcularIva();
                setReportPDF();
                setFactura();
                new AuditoriaUtil().insertar("Factura generada");
            } else {
                new AlertUtil(Estado.EXITOSA, "Debe adquirir un servicio");
            }
        } catch (FileNotFoundException | DocumentException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void setReportPDF() throws FileNotFoundException, DocumentException {
        DateTime d = new DateTime();
        String time = d.getDayOfMonth() + "-" + d.getMonthOfYear() +  "-" + d.getYear() + ".pdf";
        String namePdf = "Factura" + time;
        String timeActual = "" + d.getDayOfMonth() + "/" + d.getMonthOfYear() +  "/" + d.getYear();
        PDFCreator pdfCreator = new PDFCreator(namePdf,
                "Todo Frío C.A. " +
                        "\nj-29441763-9 \nDireccion: Avenida los Cedros Cruce/C Junin Local 105-C Barrio Lourdes Maracay " +
                        "\n " + "Factura Nº: " + facturaDAO.selectLastID().getIdfactura(),
                "Factura del día: " + timeActual);
        pdfCreator.setFontTitle(pdfCreator.family, 14, Font.BOLD, pdfCreator.background);
        pdfCreator.setFontSub(pdfCreator.family, 12, Font.ITALIC, pdfCreator.background);
        pdfCreator.crearPDF(2, (PdfPTable tabla) -> {
            tabla.addCell("Cedula");
            tabla.addCell(jCedula.getText());
            tabla.addCell("Nombre");
            tabla.addCell(jNombre.getText());
            tabla.addCell("Telefono");
            tabla.addCell(jTelefono.getText());
            tabla.addCell("Ciudad");
            tabla.addCell(jCiudad.getText());
            totalArt.forEach((key, value) -> {
                tabla.addCell(key);
                tabla.addCell(String.format("%1$,.2f", value) + " Bs");
            });
            tabla.addCell("Subtotal");
            tabla.addCell(String.format("%1$,.2f", subTotal) + " Bs");
            tabla.addCell("IVA");
            tabla.addCell(String.format("%1$,.2f", iva) + " Bs");
            tabla.addCell("Total a Pagar");
            tabla.addCell(String.format("%1$,.2f", totalPagar) + " Bs");
        });
    }

    private void setFactura() throws ParseException {
        core.vo.Factura factura = new core.vo.Factura();
        StringBuilder serv = new StringBuilder();
        StringBuilder servPago = new StringBuilder();
        totalArt.forEach((key, value) -> {
            serv.append(key).append(", ");
            servPago.append(value).append(", ");
        });
        factura.setForma_pago(String.valueOf(servPago));
        factura.setServicios(serv.toString());
        factura.setFecha_pago(FechaUtil.getCurrentDate());
        DateTime dateTime = new DateTime(FechaUtil.getCurrentDate());
        factura.setFecha_entrega(dateTime.plusDays(tiempoMaximo).toDate());
        factura.setIVA(iva);
        factura.setTotal(totalPagar);
        factura.setCliente_cedula(Integer.parseInt(jCedula.getText()));
        factura.setUsuario_cedula(Storage.getUsuario().getCedula());
        facturaDAO.insert(factura);
    }

    private void calcularIva() {
        subTotal = totalPagar;
        iva = totalPagar * 0.12;
        totalPagar += iva;
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionElegir(ActionEvent actionEvent) {

    }

    @Override
    public void setStatusControls() {

    }
}
