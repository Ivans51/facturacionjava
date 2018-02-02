package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.util.*;
import core.vo.Cliente;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroCliente extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jCedula, jNombre, jApellido, jDireccion, jNombreCiudad;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEditar, btnEliminar;
    public TableView tableCliente;
    public AnchorPane anchorPane;
    public TableColumn tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono, tbUsuario;
    private TableUtil table;
    private List<Cliente> clientes = new ArrayList<>();
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private String[] columS = {"cedula", "nombre", "apellido", "direccion", "telefono", "usuario"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableUtil(Cliente.class, tableCliente);
        table.inicializarTabla(columS, tbCedula, tbNombres, tbApellidos, tbDireccion, tbTelefono, tbUsuario);

        final ObservableList<Cliente> tablaSelecionada = tableCliente.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Cliente>) c -> table.seleccionarTabla(this));

        selectAllEmpleadoContrato();
        table.getListTable().addAll(clientes);
    }

    private void selectAllEmpleadoContrato() {
        clientes = clienteDAO.selectAll();
        for (int i = 0; i < clientes.size(); i++) {
            /*try {
                String fechaNac = FechaUtil.getDateFormat(clientes.get(i).getFechaNacimiento());
                String fechaIngreso = FechaUtil.getDateFormat(clientes.get(i).getContratacion().getFechaInicio());
                String fechaCulminacion = FechaUtil.getDateFormat(clientes.get(i).getContratacion().getFechaCulminacion());
                empleadoContratacion = new EmpleadoContratacion();
                empleadoContratacion.setCedula(clientes.get(i).getCedula());
                empleadoContratacion.setNombreEmpleado(clientes.get(i).getNombreEmpleado());
                empleadoContratacion.setDireccion(clientes.get(i).getDireccion());
                empleadoContratacion.setFechaNac(fechaNac);
                empleadoContratacion.setCargo(clientes.get(i).getCargo());
                empleadoContratacion.setStatusActual(String.valueOf(clientes.get(i).getStatusLaborando()));
                empleadoContratacion.setSueldo(String.valueOf(clientes.get(i).getContratacion().getSalario()));
                empleadoContratacion.setFechaIngreso(fechaIngreso);
                empleadoContratacion.setFechaCulminacion(fechaCulminacion);
                empleadoContratacions.add(empleadoContratacion);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void actionAgregar(ActionEvent actionEvent) {

    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jCedula, jNombre, jApellido, jDireccion, jNombreCiudad);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionEditar(ActionEvent actionEvent) {

    }

    public void actionEliminar(ActionEvent actionEvent) {
    }

    @Override
    public void setStatusControls() {

    }
}
