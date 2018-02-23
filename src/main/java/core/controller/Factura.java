package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
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

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Factura extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios, cServiciosAgregados, cSubServicio;
    public TextField jPrecio, jFecha, jCedula;
    public JFXButton btnAgregar, btnSalir, btnMostrarProducto, btnBuscar, btnImprimir;
    public Label lblFecha, lblPrecio, lblTotal, lblNombre, lblCiudad, lblTelefono, lblSub;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private SubServiciosDAO subServiciosDAO = new SubServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private double totalPagar;
    private HashMap<String, String> totalArt = new HashMap<>();
    private double iva;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setComboServicio();
    }

    private void setComboServicio() {
        HashMap<String, Integer> nombres = new HashMap<>();
        List<Servicios> servicios = serviciosDAO.selectAll();
        servicios.forEach(servicio -> nombres.put(servicio.getNombre(), servicio.getIdservicios()));
        nombres.forEach((key, value) -> cServicios.getItems().add(key));
        cServicios.valueProperty().addListener((observable, oldValue, newValue) -> {
            List<Servicios> list = serviciosDAO.selectJoinSub(newValue);
            // TODO: 2/23/2018 Modifaicate validation
            if (list.get(0).getSubServicios() != null) {
                setStatusSubServicio();
                setComboSubServicio(newValue, list);
            } else {
                setStatusSubServicio();
                setValorProductoInicial(newValue, servicios);
            }
        });
    }

    private void setComboSubServicio(String newValue, List<Servicios> list) {
        List<String> nombresSub = new ArrayList<>();
        list.forEach(serv -> nombresSub.add(serv.getSubServicios().getNombreSub()));
        cSubServicio.getItems().addAll(nombresSub);
        cSubServicio.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
            SubServicios servicios = subServiciosDAO.selectByNombre(newValue1);
            if (servicios != null) {
                jPrecio.setText(String.valueOf(servicios.getPrecioSub()));
                jFecha.setText(FechaUtil.getDateFormat(servicios.getFechaSub()));
            }
        });
    }

    private void setStatusSubServicio() {
        cSubServicio.getItems().clear();
        cSubServicio.setVisible(!cSubServicio.isVisible());
        lblSub.setVisible(!lblSub.isVisible());
    }

    private void setValorProductoInicial(String newValue, List<Servicios> list) {
        Servicios servicios = serviciosDAO.selectByNombre(newValue);
        if (servicios != null) {
            jPrecio.setText(String.valueOf(servicios.getPrecio()));
            jFecha.setText(FechaUtil.getDateFormat(servicios.getFecha()));
        }
    }

    public void actionAgregar(ActionEvent actionEvent) {
        setComboAgregados();
        setTotal();
    }

    private void setComboAgregados() {
        cServiciosAgregados.getItems().add(cServicios.getSelectionModel().getSelectedItem());
        cServiciosAgregados.valueProperty().addListener((observable, oldValue, newValue) -> {
            Servicios servicios = serviciosDAO.selectAllNombres(newValue);
            if (servicios != null) {
                lblFecha.setText(servicios.getTiempo_estimado());
                lblPrecio.setText(String.valueOf(servicios.getPrecio()));
                lblTotal.setText(String.valueOf(totalPagar));
            }
        });
    }

    private void setTotal() {
        Servicios serv = serviciosDAO.selectAllNombres(cServicios.getSelectionModel().getSelectedItem());
        totalArt.put(serv.getNombre(), String.valueOf(serv.getPrecio()));
        totalArt.forEach((key, value) -> totalPagar += Double.valueOf(value));
    }

    public void borrarItem(ActionEvent actionEvent) {
        String item = cServicios.getSelectionModel().getSelectedItem();
        cServiciosAgregados.getSelectionModel().clearSelection();
        cServiciosAgregados.getItems().remove(item);
        Servicios serv = serviciosDAO.selectAllNombres(item);
        totalPagar -= serv.getPrecio();
    }

    public void actionBuscar(ActionEvent actionEvent) {
        Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
        if (cliente == null)
            abrirStageStyle(Route.ClienteDialog, "Agregar Cliente", Modality.WINDOW_MODAL, null,
                    false, StageStyle.TRANSPARENT, () -> {
                        ClienteDialog display = ManagerFXML.getFxmlLoader().getController();
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
            calcularIva();
            DateTime d = new DateTime();
            String time = d.getDayOfMonth() + "-" + d.getHourOfDay() + "-" + d.getMinuteOfHour() + ".pdf";
            String namePdf = "Factura" + time;
            PDFCreator pdfCreator = new PDFCreator(namePdf, "Todo Frío C.A.", "Factura");
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
                    tabla.addCell(value + " Bs");
                });
                tabla.addCell("IVA");
                tabla.addCell(String.valueOf(iva + " Bs"));
                tabla.addCell("Total a Pagar");
                tabla.addCell(String.valueOf(totalPagar + " Bs"));
            });
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void calcularIva() {
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
