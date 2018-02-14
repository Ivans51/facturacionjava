package core.controller;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.ServiciosDAO;
import core.util.*;
import core.vo.Cliente;
import core.vo.Servicios;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Factura extends ManagerFXML implements Initializable{

    public AnchorPane anchorPane;
    public ComboBox<String> cServicios, cServiciosAgregados;
    public TextField jPrecio, jFecha, jCedula;
    public JFXButton btnAgregar, btnSalir, btnMostrarProducto,btnBuscar, btnImprimir;
    public Label lblFecha, lblPrecio, lblTotal, lblNombre, lblCiudad, lblTelefono;

    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> nombres = serviciosDAO.selectNombres();
        nombres.forEach(nombre -> cServicios.getItems().add(nombre));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        cServiciosAgregados.getItems().add(cServicios.getSelectionModel().getSelectedItem());
    }

    public void actionMostrar(ActionEvent actionEvent) {
        Servicios servicios = serviciosDAO.selectByNombre(cServiciosAgregados.getSelectionModel().getSelectedItem());
        lblFecha.setText(FechaUtil.getDateFormat(servicios.getFecha()));
        lblPrecio.setText(String.valueOf(servicios.getPrecio()));
        // TODO: 2/4/2018 Fix Date
        lblTotal.setText("");
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
            PDFCreator pdfCreator = new PDFCreator();
            pdfCreator.crearPDF("Todo Frío C.A.", "Factura", 4, () -> {
                pdfCreator.getTabla().addCell("Nombre y Apellido");
            });
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.Inicio, anchorPane);
    }
}
