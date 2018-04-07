package core.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.FacturaDAO;
import core.dao.ServiciosDAO;
import core.util.*;
import core.util.TableUtil;
import core.vo.Cliente;
import core.vo.Servicios;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public Label jPrecio, jFecha, jNombre, jCiudad, jTelefono;
    public TextField jCedula;
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

        tableServicioUtil.getListTable().addAll(new ArrayList<>());
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
            Servicios servicios = serviciosDAO.selectByNombre(item);
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
            servicios.setPrecioEdit(String.format("%1$,.2f", servicios.getPrecio()) + " Bs");
            tableServicioUtil.getListTable().add(servicios);
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

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            if (totalCantArt.size() > 0) {
                Validar.stringVacio(new String[]{"Tipo de Pago"}, cTipoPago.getSelectionModel().getSelectedItem());
                Validar.stringVacio(new String[]{"Datos del cliente"}, jNombre.getText());
                calcularIva();
                String nameFile = getNameFile();
                setFactura(nameFile);
                setReportPDF(nameFile);
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

    private String getNameFile() {
        DateTime d = new DateTime();
        String time = d.getDayOfMonth() + "-" + d.getMonthOfYear() + "-" + d.getYear() + "-" + d.getSecondOfDay() + ".pdf";
        return "Factura" + time;
    }

    private void setReportPDF(String namePdf) throws IOException, DocumentException {
        DateTime d = new DateTime();
        String timeActual = "" + d.getDayOfMonth() + "/" + d.getMonthOfYear() + "/" + d.getYear();
        String title = "Inversiones Todo Frío C.A. " +
                "\nj-29441763-9 \nDireccion: Avenida los Cedros Cruce C/C Junin Local 105-C Barrio Lourdes Maracay " +
                "\n " + "Factura Nº: " + facturaDAO.selectLastID().getIdfactura();
        String sub = "Factura del día: " + timeActual;

        PDFCreator pdfCreator = new PDFCreator("reports/" + namePdf);
        pdfCreator.createPDF(documento -> {

            Paragraph elementRight = pdfCreator.setParagraph(title, Element.ALIGN_RIGHT, 10, 12, Font.BOLD);
            Paragraph elementsLeft = pdfCreator.setParagraph(sub, Element.ALIGN_LEFT, 10, 12, Font.NORMAL);
            Image image = pdfCreator.setImagePDF("src/main/resources/images/FacturaLogo.png", 150, 100, Element.ALIGN_LEFT);

            PdfPTable tableTitle = pdfCreator.setTablePDF(new float[]{220, 300}, tabla -> {
                PdfPCell pdfPCellLeft = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, image, elementsLeft);
                PdfPCell pdfPCellRight = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, elementRight);
                tabla.addCell(pdfPCellLeft);
                tabla.addCell(pdfPCellRight);
            });
            documento.add(tableTitle);

            documento.add(pdfCreator.setParagraph("Datos del Cliente \n ", Element.ALIGN_LEFT, 10, 12, Font.BOLD));

            PdfPTable tableCliente = pdfCreator.setTablePDFWithoutBorder(new float[]{520}, tabla -> {
                tabla.addCell("Nombre o razón social: " + cliente.getNombres() + " " + cliente.getApellidos());
                tabla.addCell("Domicilio fiscal: " + jCiudad.getText());
                tabla.addCell("C.I. o RIF: " + cliente.getCedula());
                tabla.addCell("Teléfono: " + jTelefono.getText());
            });
            documento.add(tableCliente);

            PdfPTable tableDetail = pdfCreator.setTablePDF(new float[]{40, 220, 130, 130}, tabla -> {
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
            });
            documento.add(tableDetail);

            PdfPTable tableEnd = pdfCreator.setTablePDF(new float[]{260, 260}, tabla -> {
                PdfPTable tableOneTotal = pdfCreator.setTablePDFWithoutBorder(new float[]{260}, tabla1 -> {
                    for (int i = 0; i < 3; i++) tabla1.addCell("");
                });

                PdfPTable tableTwoTotal = pdfCreator.setTablePDF(new float[]{130, 130}, tabla2 -> {
                    tabla2.addCell("Subtotal");
                    tabla2.addCell(String.format("%1$,.2f", subTotal) + " Bs");

                    tabla2.addCell("IVA");
                    tabla2.addCell(String.format("%1$,.2f", iva) + " Bs");

                    tabla2.addCell("Total a Pagar");
                    tabla2.addCell(String.format("%1$,.2f", totalPagar) + " Bs");
                });
                tabla.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                tabla.addCell(tableOneTotal);
                tabla.addCell(tableTwoTotal);
            });
            documento.add(tableEnd);

            String value = "Tipo de Pago: " + cTipoPago.getSelectionModel().getSelectedItem();
            documento.add(pdfCreator.setParagraph(value, Element.ALIGN_LEFT, 10, 12, Font.BOLD));
        });
    }

    private void setFactura(String namePdf) throws ParseException {
        core.vo.Factura factura = new core.vo.Factura();
        StringBuilder serv = new StringBuilder();
        int count = 1;
        for (String totalArt : totalPrecioArt.keySet()) {
            if (count < totalPrecioArt.size())
                serv.append(totalArt).append(" x ").append(totalCantArt.get(totalArt)).append(", ");
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
        factura.setNameFile(namePdf);
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
