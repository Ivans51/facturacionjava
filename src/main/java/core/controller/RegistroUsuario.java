package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.*;
import core.vo.Usuario;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroUsuario extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jNombre, jCorreo, jCedula;
    public PasswordField jClave;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEditar;
    public AnchorPane anchorPane;
    public TableView<Usuario> tableUsuario;
    public TableColumn tbNacionalidad, tbCedula, tbNombre, tbFecha, tbStatus;
    public ComboBox<String> cNivel;
    public ComboBox<String> cNacionalidad;

    private String[] niveles = {"Usuario", "Administrador", "Gerente"};
    private String[] nacionalidades = {"V", "E"};
    private TableUtil<Usuario, String> table;
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Usuario> usuarios = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private List<String> correos = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private Usuario usuario;
    private String[] columS = {"Nacionalidad", "Cedula", "Nombre", "Fecha", "Status"};
    private boolean stateEdit = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEditar.setDisable(false);
        cNivel.getItems().addAll(niveles);
        cNacionalidad.getItems().addAll(nacionalidades);
        setTable();
    }

    private void setTable() {
        tableUsuario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table = new TableUtil(Usuario.class, tableUsuario);
        table.inicializarTabla(columS, tbNacionalidad, tbCedula, tbNombre, tbFecha, tbStatus);

        final ObservableList<Usuario> tablaSelecionada = tableUsuario.getSelectionModel().getSelectedItems();
        tablaSelecionada.addListener((ListChangeListener<Usuario>) c -> table.seleccionarTabla(this));

        selectAllUsuario();
        table.getListTable().addAll(usuarios);
    }

    private void selectAllUsuario() {
        usuarios = usuarioDAO.selectAll();
        usuarios.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
        usuarios.forEach(it -> nombres.add(it.getNombre()));
        usuarios.forEach(it -> correos.add(it.getCorreo()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(jNombre, jCorreo, jClave);
            Validar.isNumber(jCedula);
            String nac = cNacionalidad.getSelectionModel().getSelectedItem();
            String nivel = cNivel.getSelectionModel().getSelectedItem();
            Validar.stringVacio(nac, nivel);
            if (!stateEdit) {
                Validar.checkValor(jCedula.getText(), cedulas, "cédula");
                Validar.checkValor(jCorreo.getText(), correos, "correo");
                Validar.checkValor(jNombre.getText(), nombres, "nombre");
                usuarioDAO.insert(getUsuarioSelect());
                Usuario usuario = usuarioDAO.selectById(Integer.parseInt(jCedula.getText()));
                table.getListTable().add(usuario);
            } else {
                usuarioDAO.update(getUsuarioSelect());
                selectAllUsuario();
                stateViewEdit(false);
            }
            tableUsuario.refresh();
            cNacionalidad.getSelectionModel().clearSelection();
            Validar.limmpiarCampos(jNombre, jClave, jCorreo, jCedula);
        } catch (ParseException | Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private Usuario getUsuarioSelect() throws ParseException {
        usuario = new Usuario();
        if (!stateEdit) usuario.setCedula(Integer.parseInt(jCedula.getText()));
        usuario.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        usuario.setNombre(jNombre.getText());
        usuario.setClave(jClave.getText());
        usuario.setCorreo(jCorreo.getText());
        usuario.setFecha(FechaUtil.getCurrentDate());
        usuario.setStatus(cNivel.getSelectionModel().getSelectedItem());
        return usuario;
    }

    public void actionEditar(ActionEvent actionEvent) {
        try {
            stateViewEdit(false);
            Validar.limmpiarCampos(jNombre, jClave, jCorreo, jCedula);
        } catch (Myexception e) {
            new AlertUtil(Estado.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setStatusControls() {
        if (table.getModel() != null) {
            usuario = table.getModel();
            jCedula.setText(String.valueOf(usuario.getCedula()));
            jNombre.setText(usuario.getNombre());
            jClave.setText(usuario.getClave());
            jCorreo.setText(usuario.getCorreo());
            cNacionalidad.getSelectionModel().select(usuario.getNacionalidad());
            stateViewEdit(true);
        }
    }

    private void stateViewEdit(boolean value) {
        stateEdit = value;
        btnEditar.setVisible(value);
        jNombre.setDisable(value);
        jCedula.setDisable(value);
        cNacionalidad.setDisable(value);
        btnAgregar.setText(value ? "Editar" : "Agregar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        try {
            Validar.limmpiarCampos(jNombre, jClave, jCorreo);
            cNacionalidad.getSelectionModel().clearSelection();
        } catch (Myexception myexception) {
            myexception.printStackTrace();
        }
    }

    public void actionSalir(MouseEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
