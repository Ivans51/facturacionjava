package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.FacturaDAO;
import core.dao.ServiciosDAO;
import core.dao.SubServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
import core.vo.SubServicios;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Factura extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios, cServiciosAgregados, cSubServicio;
    public TextField jPrecio, jFecha, jCedula;
    public JFXButton btnAgregar, btnSalir, btnMostrarProducto, btnBuscar, btnImprimir;
    public Label lblFecha, lblPrecio, lblTotal, lblNombre, lblCiudad, lblTelefono, lblSub;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private double totalPagar;
    private HashMap<String, Double> totalArt = new HashMap<>();
    private double iva;
    private int tiempoMaximo = 0;
    private double subTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setComboServicio();
    }

    // Muestra los servicios
    private void setComboServicio() {
        HashMap<String, Integer> nombres = new HashMap<>();
        List<Servicios> servicios = serviciosDAO.selectAllFilter();
        servicios.forEach(servicio -> nombres.put(servicio.getNombre(), servicio.getIdservicios()));
        nombres.forEach((key, value) -> cServicios.getItems().add(key));
        cServicios.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Servicios> list = serviciosDAO.selectJoinSub(newValue);
            if (list.size() > 0) {
                setStatusSubServicio();
                setComboSubServicio(list);
            }
        });
    }

    // Muestra la informacion segun el tipo de servio y muestra los subservicios
    private void setComboSubServicio(List<Servicios> list) {
        List<String> nombresSub = new ArrayList<>();
        list.forEach(serv -> nombresSub.add(serv.getSubServicios().getNombreSub()));
        cSubServicio.getItems().addAll(nombresSub);
        cSubServicio.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
            SubServicios servicios = subServiciosDAO.selectByNombre(newValue1);
            if (servicios != null) {
                jPrecio.setText(String.valueOf(servicios.getPrecioSub()));
                jFecha.setText(FechaUtil.getDateFormat(servicios.getFechaSub()));
                int tiempo = servicios.getTiempo_estimadoSub();
                if (tiempo > tiempoMaximo)
                    tiempoMaximo = tiempo;
            }
        });
    }

    private void setStatusSubServicio() {
        cSubServicio.getItems().clear();
        cSubServicio.setVisible(!cSubServicio.isVisible());
        lblSub.setVisible(!lblSub.isVisible());
    }

    // Despliega los servicios y subservicios agregados
    public void actionAgregar(ActionEvent actionEvent) {
        String item = cSubServicio.getSelectionModel().getSelectedItem();
        if (item != null && !"".equals(item)) {
            setComboAgregados(item);
            setTotal(item);
            limpiar();
        } else {
            new AlertUtil(Estado.ERROR, "Selecciona un servicio y subservicio");
        }
    }

    // Se llama desde la funcion del boton agregar
    private void setComboAgregados(String item) {
        cServiciosAgregados.getItems().add(item);
        cServiciosAgregados.valueProperty().addListener((observable, oldValue, newValue) -> {
            SubServicios servicios = subServiciosDAO.selectByNombre(newValue);
            if (servicios != null) {
                lblFecha.setText(String.valueOf(servicios.getTiempo_estimadoSub()));
                lblPrecio.setText(String.valueOf(servicios.getPrecioSub()));
                lblTotal.setText(String.valueOf(totalPagar));
            }
        });
    }

    // A medida que se agregan los servicio se suma el precio total del servicio
    private void setTotal(String item) {
        SubServicios serv = subServiciosDAO.selectByNombre(item);
        totalArt.put(serv.getNombreSub(), serv.getPrecioSub());
        totalPagar += serv.getPrecioSub();
        lblTotal.setText(String.valueOf(totalPagar));
    }

    // Una vez que se agrega el servicio si limpia los de arriba
    private void limpiar() {
        jPrecio.setText("");
        jFecha.setText("");
        cServicios.getSelectionModel().clearSelection();
        setStatusSubServicio();
    }

    // Borra un servicio agregado
    public void borrarItem(ActionEvent actionEvent) {
        String item = cServiciosAgregados.getSelectionModel().getSelectedItem();
        if (item != null && !"".equals(item)) {
            cServiciosAgregados.getSelectionModel().clearSelection();
            cServiciosAgregados.getItems().remove(item);
            totalArt.remove(item);
            totalPagar -= subServiciosDAO.selectByNombre(item).getPrecioSub();
            lblFecha.setText("");
            lblPrecio.setText("");
            lblTotal.setText(String.valueOf(totalPagar));
        }
    }

    // Busca los clientes de la empresa
    public void actionBuscar(ActionEvent actionEvent) {
        Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
        if (cliente == null)
            abrirStageStyle(Route.ClienteDialog, "Agregar Cliente", Modality.WINDOW_MODAL, null,
                    false, StageStyle.TRANSPARENT, () -> {
                        DialogCliente display = ManagerFXML.getFxmlLoader().getController();
                        display.setModel(jCedula.getText(), lblNombre, lblCiudad, lblTelefono);
                    });
        else {
            new AlertUtil(Estado.EXITOSA, "Usuario registrado en el sistema");
            lblNombre.setText(cliente.getNombres());
            lblCiudad.setText(cliente.getDireccion());
            lblTelefono.setText(cliente.getTelefono());
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
            tabla.addCell(lblNombre.getText());
            tabla.addCell("Telefono");
            tabla.addCell(lblTelefono.getText());
            tabla.addCell("Ciudad");
            tabla.addCell(lblCiudad.getText());
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

    public interface Controls {
        void send();
    }
}
