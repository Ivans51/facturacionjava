package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.*;
import core.util.TableUtil;
import core.vo.Usuario;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroUsuario extends ManagerFXML implements Initializable, TableUtil.StatusControles {

    public TextField jNombre, jCorreo, jCedula, jBuscar;
    public PasswordField jClave;
    public JFXButton btnAgregar, btnLimpiar, btnSalir, btnEliminar;
    public AnchorPane anchorPane;
    public TableView<Usuario> tableUsuario;
    public TableColumn tbNacionalidad, tbCedula, tbNombre, tbFecha, tbStatus;
    public ComboBox<String> cNivel;
    public ComboBox<String> cNacionalidad;

    private String[] niveles = {Estado.GERENTE, Estado.ASISTENTE, Estado.TECNICO};
    private String[] nacionalidades = {"V", "E", "J"};
    private TableUtil<Usuario, String> table;
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private List<Usuario> usuarios = new ArrayList<>();
    private List<String> cedulas = new ArrayList<>();
    private List<String> correos = new ArrayList<>();
    private List<String> nombres = new ArrayList<>();
    private Usuario usuario;
    private String[] columS = {"Nacionalidad", "Cedula", "Nombre", "FechaEdit", "Status"};
    private boolean stateEdit = false;
    private String[] field = {"Nombre", "Correo", "Clave"};
    private String[] fieldNumber = {"Cédula"};
    private int[] rangeLimitCedula = {7, 8};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCombo();
        setTable();
        setBuscarTable();
    }

    private void setCombo() {
        cNivel.getItems().addAll(niveles);
        cNacionalidad.getItems().addAll(nacionalidades);
        cNacionalidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "V":
                case "E":
                    rangeLimitCedula = new int[]{7, 8};
                    break;
                case "J":
                    rangeLimitCedula = new int[]{7, 9};
                    break;
            }
        });
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

    private void setBuscarTable() {
        ObservableList<Usuario> data = table.getData();
        jBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length()))
                tableUsuario.setItems(data);
            table.searchMultiple(newValue.toLowerCase());
        });
    }

    private void selectAllUsuario() {
        usuarios = usuarioDAO.selectAll();
        for (Usuario servicios : usuarios) {
            servicios.setFechaEdit(FechaUtil.getDateFormat(servicios.getFecha()));
        }
        usuarios.forEach(it -> cedulas.add(String.valueOf(it.getCedula())));
        usuarios.forEach(it -> nombres.add(it.getNombre()));
        usuarios.forEach(it -> correos.add(it.getCorreo()));
    }

    public void actionAgregar(ActionEvent actionEvent) {
        try {
            Validar.campoVacio(field, jNombre, jCorreo, jClave);
            Validar.isNumber(fieldNumber, jCedula);
            Validar.limitField(rangeLimitCedula, "Cédula o RIF", jCedula);
            Validar.limitField(new int[]{8, 32}, "Clave", jClave);
            String nac = cNacionalidad.getSelectionModel().getSelectedItem();
            String nivel = cNivel.getSelectionModel().getSelectedItem();
            String[] fieldString = {"Nacionalidad, Nivel"};
            Validar.stringVacio(fieldString, nac, nivel);
            eligirConsulta();
            cNacionalidad.getSelectionModel().clearSelection();
            Validar.limmpiarCampos(jNombre, jClave, jCorreo, jCedula);
        } catch (ParseException | Myexception myexception) {
            new AlertUtil(Estado.ERROR, myexception.getMessage());
            myexception.printStackTrace();
        }
    }

    private void eligirConsulta() throws Myexception, ParseException {
        if (!stateEdit) {
            Validar.checkValor(jCedula.getText(), cedulas, "cédula");
            Validar.checkValor(jNombre.getText(), nombres, "nombre");
            usuarioDAO.insert(getUsuarioInsert());
            Usuario usuario = usuarioDAO.selectById(Integer.parseInt(jCedula.getText()));
            table.getListTable().add(usuario);
            new AlertUtil(Estado.EXITOSA, "Usuario creado correctamente");
            new AuditoriaUtil().insertar("Registro del Usuario");
        } else {
            usuarioDAO.update(getUsuarioUpdate());
            selectAllUsuario();
            stateViewEdit(false);
            new AlertUtil(Estado.EXITOSA, "Usuario modificado correctamente");
            new AuditoriaUtil().insertar("Usuario actualizado");
        }
        tableUsuario.refresh();
    }

    private Usuario getUsuarioInsert() throws ParseException {
        Usuario usuario = new Usuario();
        if (!stateEdit) usuario.setCedula(Integer.parseInt(jCedula.getText()));
        usuario.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        usuario.setNombre(jNombre.getText());
        usuario.setClave(jClave.getText());
        usuario.setCorreo(jCorreo.getText());
        usuario.setFecha(FechaUtil.getCurrentDate());
        usuario.setStatus(cNivel.getSelectionModel().getSelectedItem());
        return usuario;
    }

    private Usuario getUsuarioUpdate() throws ParseException {
        if (!stateEdit) usuario.setCedula(Integer.parseInt(jCedula.getText()));
        usuario.setNacionalidad(cNacionalidad.getSelectionModel().getSelectedItem());
        usuario.setNombre(jNombre.getText());
        usuario.setClave(jClave.getText());
        usuario.setCorreo(jCorreo.getText());
        usuario.setFecha(FechaUtil.getCurrentDate());
        usuario.setStatus(cNivel.getSelectionModel().getSelectedItem());
        return usuario;
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

    private void stateViewEdit(boolean edit) {
        stateEdit = edit;
        btnEliminar.setVisible(edit);
        jNombre.setDisable(edit);
        jCedula.setDisable(edit);
        cNacionalidad.setDisable(edit);
        btnAgregar.setText(edit ? "Editar" : "Agregar");
        btnLimpiar.setText(edit ? "Cancelar" : "Limpiar");
    }

    public void actionLimpiar(ActionEvent actionEvent) {
        Validar.limmpiarCampos(jNombre, jClave, jCorreo);
        cNacionalidad.getSelectionModel().clearSelection();
    }

    public void actionSalir(ActionEvent mouseEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }

    public void actionEliminar(ActionEvent actionEvent) {
        Usuario servicios = new Usuario();
        servicios.setCedula(usuario.getCedula());
        servicios.setEstado(0);
        usuarioDAO.updateEstado(servicios);
        table.getListTable().remove(this.usuario);
        tableUsuario.refresh();
        new AlertUtil(Estado.ERROR, "Se guardo el cambio");
    }
}
