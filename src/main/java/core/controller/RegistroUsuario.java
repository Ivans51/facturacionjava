package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.*;
import core.vo.Servicios;
import core.vo.Usuario;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroUsuario extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jNombre, jClave, jCorreo, jCedula;
    public JFXButton btnAgregar, btnLimpiar, btnSalir,btnEditar, btnEliminar;
    public AnchorPane anchorPane;
    public TableView<Usuario> tableUsuario;
    public TableColumn tbCedula, tbNombre, tbFecha, tbStatus;
    public ComboBox cNivel;

    private TableUtil<Usuario, String> table;
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Usuario> usuarios = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private Usuario usuario;
    private String[] columS = {"Cedula", "Nombre", "Fecha", "Status"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEditar.setDisable(false);
        setTable();
    }

    private void setTable() {
        tableUsuario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Usuario.class, tableUsuario);
        table.inicializarTabla(columS, tbCedula, tbNombre, tbFecha, tbStatus);

        final ObservableList<Usuario> tablaSelecionada = tableUsuario.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Usuario>) c -> table.seleccionarTabla(this));

        selectAllUsuario();
        table.getListTable().addAll(usuarios);
    }

    private void selectAllUsuario() {
        usuarios = usuarioDAO.selectAll();
        usuarios.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCorreo, jClave);
            Validar.checkValor(jCedula.getText(), cedulas);
            // Validar.isLetterOptimo(jNombre.getText(), jApellido.getText());
            Usuario usuario = usuarioDAO.selectById(Integer.parseInt(jCedula.getText()));
            table.getListTable().add(usuario );
            tableUsuario.refresh();
            Validar.limmpiarCampos(jNombre, jClave, jCorreo, jCedula);
        } catch (Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCorreo, jClave);
            usuarioDAO.update(getUsuarioSelect());
            selectAllUsuario();
            tableUsuario.refresh();
            btnEditar.setDisable(false);
            btnAgregar.setDisable(true);
            Validar.limmpiarCampos(jNombre, jClave, jCorreo, jCedula);
        } catch (ParseException | Myexception e) {
            new AlertUtil(Estado.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    private Usuario getUsuarioSelect() throws ParseException {
        usuario.setNombre(jNombre.getText());
        usuario.setCedula(Integer.parseInt(jCedula.getText()));
        usuario.setClave(jClave.getText());
        usuario.setCorreo(jCorreo.getText());
        usuario.setFecha(FechaUtil.getCurrentDate());
        usuario.setStatus(cNivel.getSelectionModel().getSelectedItem().toString());
        return usuario;
    }

    public void actionEliminar(ActionEvent actionEvent) {
        try {
            usuarioDAO.delete(usuario.getCedula());
            table.getListTable().remove(usuario);
            tableUsuario.refresh();
            Validar.limmpiarCampos(jNombre, jClave, jCorreo);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            usuario = table.getModel();
            btnEditar.setDisable(true);
            jNombre.setText(usuario.getNombre());
            jClave.setText("*****");
            jCorreo.setText(usuario.getClave());
            btnAgregar.setDisable(false);
        }
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jClave, jCorreo);
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(MouseEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
