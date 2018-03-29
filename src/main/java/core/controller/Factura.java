package core.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.FacturaDAO;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
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
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Factura extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios, cTipoPago;
    public TextField jPrecio, jFecha, jCedula, jNombre, jCiudad, jTelefono;
    public JFXButton btnAgregar, btnSalir, btnImprimir, btnEliminar;
    public TableView<Cliente> tableCliente;
    public TableColumn tbCedula, tbNombreCliente, tbCiudad, tbFecha, tbTelefono;
    public TableView<Servicios> tableServicio;
    public TableColumn tbID, tbNombreServicio, tbPrecio, tbTiempoEstimado;

    private TableUtil<Cliente, String> tableClienteUtil;
    private TableUtil<Servicios, String> tableServicioUtil;
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Servicios> servicios = new ArrayList<>();

    private HashMap<String, Double> totalPrecioArt = new HashMap<>();
    private HashMap<String, Integer> totalCantArt = new HashMap<>();
    private HashMap<String, Servicios> totalArt = new HashMap<>();
    private int tiempoMaximo = 0;
    private double iva, subTotal, totalPagar;

    private String[] columS = {"cedula", "nombres", "apellidos", "direccion", "telefono"};
    private String[] columServicio = {"idservicios", "nombre", "precio", "tiempo_estimado"};
    private String[] tiposPago = {"Efectivo", "Transferencia", "Cheque", "Otro"};
    private Servicios servicio;
    private Cliente cliente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cTipoPago.getItems().addAll(tiposPago);
        setComboServicio();
        setTableServicios();
        setTableCliente();
        setBuscarTableCliente();
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
                jFecha.setText(select.getTiempo_estimado());
                int tiempo = Integer.parseInt(select.getTiempo_estimado());
                if (tiempo > tiempoMaximo)
                    tiempoMaximo = tiempo;
            }
        });
    }

    private void setTableServicios() {
        tableServicio.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableServicioUtil = new TableUtil(Servicios.class, tableServicio);
        tableServicioUtil.inicializarTabla(columServicio, tbID, tbNombreServicio, tbPrecio, tbTiempoEstimado);

        final ObservableList<Servicios> tablaSelecionada = tableServicio.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Servicios>) c -> tableServicioUtil.seleccionarTabla(this));

        tableServicioUtil.getListTable().addAll(servicios);
    }

    private void setTableCliente() {
        tableCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableClienteUtil = new TableUtil(Cliente.class, tableCliente);
        tableClienteUtil.inicializarTabla(columS, tbCedula, tbNombreCliente, tbCiudad, tbFecha, tbTelefono);

        final ObservableList<Cliente> tablaSelecionada = tableCliente.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Cliente>) c -> tableClienteUtil.seleccionarTabla(this));

        tableClienteUtil.getListTable().addAll(clienteDAO.selectAll());
    }

    private void setBuscarTableCliente() {
        ObservableList<Cliente> data = tableClienteUtil.getData();
        jCedula.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length()))
                tableCliente.setItems(data);
            tableClienteUtil.searchMultiple(newValue.toLowerCase());
        });
    }

    // Despliega los servicios y subservicios agregados
    public void actionAgregar(ActionEvent actionEvent) {
        String item = cServicios.getSelectionModel().getSelectedItem();
        if (item != null && !"".equals(item)) {
            tableServicioUtil.getListTable().add(serviciosDAO.selectByNombre(item));
            tableServicio.refresh();
            setTotal(item);
            limpiar();
        } else {
            new AlertUtil(Estado.ERROR, "Selecciona un servicio");
        }
    }

    // A medida que se agregan los servicio se suma el precio total del servicio
    private void setTotal(String item) {
        Servicios serv = serviciosDAO.selectByNombre(item);
        totalArt.put(serv.getNombre(), serv);
        if (totalCantArt.get(serv.getNombre()) != null) {
            totalCantArt.merge(serv.getNombre(), 1, Integer::sum);
        } else
            totalCantArt.put(serv.getNombre(), 1);
        totalPrecioArt.put(serv.getNombre(), serv.getPrecio());
        totalPagar += serv.getPrecio();
        // lblTotal.setText(String.valueOf(totalPagar));
    }

    // Una vez que se agrega el servicio si limpia los de arriba
    private void limpiar() {
        jPrecio.setText("");
        jFecha.setText("");
        cServicios.getSelectionModel().clearSelection();
    }

    public void actionEliminar(ActionEvent actionEvent) {
        if (totalCantArt.get(servicio.getNombre()) != null) {
            totalCantArt.merge(servicio.getNombre(), -1, Integer::sum);
        }
        if (totalArt.size() == 1) {
            totalArt.remove(servicio.getNombre());
            totalPrecioArt.remove(servicio.getNombre());
        }
        totalPagar -= servicio.getPrecio();
        tableServicioUtil.getListTable().remove(servicio);
        tableServicio.refresh();
    }

    @Override
    public void setStatusControls() {
        if (tableServicioUtil.getTablaSeleccionada(tableServicio) != null)
            servicio = tableServicioUtil.getModel();
        else if (tableClienteUtil.getTablaSeleccionada(tableCliente) != null) {
            cliente = tableClienteUtil.getModel();
            jNombre.setText(String.valueOf(cliente.getNombres()));
            jCiudad.setText(cliente.getDireccion());
            jTelefono.setText(cliente.getTelefono());
        }
    }

    // Busca los clientes de la empresa
    /*public void actionBuscar(ActionEvent actionEvent) {
        Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
        if (cliente == null)
            abrirStageStyle(Route.ClienteDialog, "Agregar Cliente", Modality.WINDOW_MODAL, null,
                    false, StageStyle.TRANSPARENT, () -> {
                        DialogCliente display = ManagerFXML.getFxmlLoader().getController();
                        display.setModel(jCedula.getText());
                    });
        else {
            new AlertUtil(Estado.EXITOSA, "Usuario ya esta registrado en el sistema");
        }
    }*/

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            if (totalCantArt.size() > 0) {
                Validar.stringVacio(new String[]{"Tipo de Pago"}, cTipoPago.getSelectionModel().getSelectedItem());
                Validar.campoVacio(new String[]{"Datos del cliente"}, jNombre);
                calcularIva();
                setFactura();
                setReportPDF();
                new AlertUtil(Estado.EXITOSA, "Factura generada", closeAlert -> {
                    cerrarStage(closeAlert);
                    cambiarEscena(Route.InicioInfo, anchorPane);
                });
            } else {
                new AlertUtil(Estado.EXITOSA, "Debe adquirir al menos un servicio");
            }
        } catch (ParseException | DocumentException | IOException | Myexception e) {
            e.printStackTrace();
            if (e instanceof Myexception) new AlertUtil(Estado.EXITOSA, e.getMessage());
        }
    }

    private void setReportPDF() throws IOException, DocumentException {
        DateTime d = new DateTime();
        String time = d.getDayOfMonth() + "-" + d.getMonthOfYear() + "-" + d.getYear() + "-" + d.getSecondOfDay() + ".pdf";
        String namePdf = "Factura" + time;
        String timeActual = "" + d.getDayOfMonth() + "/" + d.getMonthOfYear() + "/" + d.getYear();
        String title = "Inversiones Todo Frío C.A. " +
                "\nj-29441763-9 \nDireccion: Avenida los Cedros Cruce C/C Junin Local 105-C Barrio Lourdes Maracay " +
                "\n " + "Factura Nº: " + facturaDAO.selectLastID().getIdfactura();
        String sub = "Factura del día: " + timeActual;
        PDFCreator pdfCreator = new PDFCreator(namePdf, title, sub, "src/main/resources/images/FacturaLogo.png");
        pdfCreator.setFontTitle(pdfCreator.family, 12, Font.BOLD, pdfCreator.background);
        pdfCreator.setFontSub(pdfCreator.family, 12, Font.NORMAL, pdfCreator.background);
        pdfCreator.setOtherParragraph("Tipo de Pago: " + cTipoPago.getSelectionModel().getSelectedItem());
        pdfCreator.setColumnWidthOne(new float[]{40, 220, 130, 130});
        pdfCreator.setColumnWidthTwo(new float[]{520});
        pdfCreator.setColumnWidthThree(new float[]{260, 130, 130});
        pdfCreator.crearPDF(4, (PdfPTable tabla) -> {
            tabla.addCell("Cant.");
            tabla.addCell("Concepto o Descripción");
            tabla.addCell("Precio Unitario");
            tabla.addCell("Total");
            totalArt.forEach((key, value) -> {
                Integer cant = totalCantArt.get(key);
                Double precio = value.getPrecio();
                tabla.addCell(pdfCreator.setStyleCellTable(String.valueOf(cant)));
                tabla.addCell(pdfCreator.setStyleCellTable(key));
                tabla.addCell(pdfCreator.setStyleCellTable(String.format("%1$,.2f", precio) + " Bs"));
                tabla.addCell(pdfCreator.setStyleCellTable(String.format("%1$,.2f", cant * precio) + " Bs"));
            });
            for (int i = 0; i < 3; i++) {
                tabla.addCell("");
            }
        }, 1, tabla -> {
            tabla.addCell("Nombre o razón social: " + cliente.getNombres() + " " + cliente.getApellidos());
            tabla.addCell("Domicilio fiscal: " + jCiudad.getText());
            tabla.addCell("C.I. o RIF: " + cliente.getCedula());
            tabla.addCell("Teléfono: " + jTelefono.getText());
        }, 3, tabla -> {
            tabla.addCell("");
            tabla.addCell("Subtotal");
            tabla.addCell(String.format("%1$,.2f", subTotal) + " Bs");

            tabla.addCell("");
            tabla.addCell("IVA");
            tabla.addCell(String.format("%1$,.2f", iva) + " Bs");

            tabla.addCell("");
            tabla.addCell("Total a Pagar");
            tabla.addCell(String.format("%1$,.2f", totalPagar) + " Bs");
        });
    }

    private void setFactura() throws ParseException {
        core.vo.Factura factura = new core.vo.Factura();
        StringBuilder serv = new StringBuilder();
        int count = 1;
        for (String totalArt : totalPrecioArt.keySet()) {
            if (count < totalPrecioArt.size())
                serv.append(totalArt).append(" x ").append(totalCantArt.get(totalArt)).append(" ,");
            else
                serv.append(totalArt).append(" x ").append(totalCantArt.get(totalArt));
            count++;
        }
        factura.setForma_pago(cTipoPago.getSelectionModel().getSelectedItem());
        factura.setServicios(serv.toString());
        factura.setFecha_pago(FechaUtil.getCurrentDate());
        factura.setDuracion(String.valueOf(tiempoMaximo + "Hr."));
        factura.setIVA(iva);
        factura.setTotal(totalPagar);
        factura.setCliente_cedula(cliente.getCedula());
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
}
