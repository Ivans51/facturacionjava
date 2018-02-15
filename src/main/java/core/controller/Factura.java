package core.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Factura extends ManagerFXML implements Initializable {

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios, cServiciosAgregados;
    public TextField jPrecio, jFecha, jCedula;
    public JFXButton btnAgregar, btnSalir, btnMostrarProducto, btnBuscar, btnImprimir;
    public Label lblFecha, lblPrecio, lblTotal, lblNombre, lblCiudad, lblTelefono;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private int totalPagar;
    private HashMap<String, String> totalArt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> nombres = serviciosDAO.selectNombres();
        // cServicios.setItems(FXCollections.observableArrayList(nombres));
        nombres.forEach(nombre -> cServicios.getItems().addAll(nombre));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        setTotal();
        setCombo();
    }

    private void setTotal() {
        totalArt = new HashMap<>();
        Servicios serv = serviciosDAO.selectAllNombres(cServicios.getSelectionModel().getSelectedItem());
        totalArt.put(serv.getNombre(), String.valueOf(serv.getPrecio()));
        totalArt.forEach((key, value) -> totalPagar += Integer.valueOf(value));
    }

    private void setCombo() {
        cServiciosAgregados.getItems().add(cServicios.getSelectionModel().getSelectedItem());
        cServiciosAgregados.valueProperty().addListener((observable, oldValue, newValue) -> {
            Servicios servicios = serviciosDAO.selectAllNombres(newValue);
            lblFecha.setText(servicios.getTiempo_estimado());
            lblPrecio.setText(String.valueOf(servicios.getPrecio()));
            lblTotal.setText(String.valueOf(totalPagar));
        });
    }

    public void borrarItem(ActionEvent actionEvent) {
        String item = cServicios.getSelectionModel().getSelectedItem();
        cServiciosAgregados.getItems().remove(item);
        Servicios serv = serviciosDAO.selectAllNombres(item);
        totalPagar -= serv.getPrecio();
    }

    public void actionBuscar(ActionEvent actionEvent) {
        Cliente cliente = clienteDAO.selectById(Integer.parseInt(jCedula.getText()));
        if (cliente == null)
            new ClienteDialog();
        else {
            new AlertUtil(Estado.EXITOSA, "Usuario registrado en el sistema");
            lblNombre.setText(cliente.getNombres());
            lblCiudad.setText(cliente.getDireccion());
            lblTelefono.setText(cliente.getTelefono());
        }
    }

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            DateTime d = new DateTime();
            String time = d.getHourOfDay() + "-" + d.getMinuteOfHour() + d.getSecondOfMinute() + ".pdf";
            PDFCreator pdfCreator = new PDFCreator("Factura" + time, "Todo Frío C.A.", "Factura");
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
                    tabla.addCell(value);
                });
                tabla.addCell("Total a Pagar");
                tabla.addCell(String.valueOf(totalPagar));
            });
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
