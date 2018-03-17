package core.util;

import com.sun.istack.internal.Nullable;
import core.vo.Cliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ivans on 8/3/2017.
 */
public class TableUtil<T, V> {

    public TableColumn[] tableColumns;
    private T model;
    private ObservableList<T> listTable;
    private TableView<T> table;
    private int posicionPersonaEnTabla;

    public TableUtil(T model, TableView<T> table) {
        this.model = model;
        this.table = table;
    }

    /**
     * Método para inicializar la tabla
     *
     * @param value
     * @param columns
     */
    public void inicializarTabla(String[] value, TableColumn... columns) {
        for (int i = 0; i < columns.length; i++)
            columns[i].setCellValueFactory(new PropertyValueFactory<T, V>(value[i]));
        listTable = FXCollections.observableArrayList();
        this.table.setItems(listTable);
    }

    /**
     * Método para poner en los textFields la tupla que seleccionemos
     */
    public void seleccionarTabla(@Nullable StatusControles statusControles) {
        model = getTablaSeleccionada(table);
        posicionPersonaEnTabla = listTable.indexOf(model);
        if (statusControles != null)
            statusControles.setStatusControls();
    }

    /**
     * PARA SELECCIONAR UNA CELDA DE LA TABLA
     *
     * @param tablaUsuarios
     */
    public T getTablaSeleccionada(TableView tablaUsuarios) {
        if (tablaUsuarios != null) {
            List<T> tabla = tablaUsuarios.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                return tabla.get(0);
            }
        }
        return null;
    }

    public ObservableList<T> getData() {
        return table.getItems();
    }

    public void searchMultiple(String value){
        ObservableList<T> subentries = FXCollections.observableArrayList();

        int count = table.getColumns().size();
        for (int i = 0; i < table.getItems().size(); i++) {
            for (int j = 0; j < count; j++) {
                String entry = "" + table.getColumns().get(j).getCellData(i);
                if (entry.toLowerCase().contains(value)) {
                    subentries.add(table.getItems().get(i));
                    break;
                }
            }
        }
        table.setItems(subentries);
    }

    public TableColumn[] getTableColumns() {
        return tableColumns;
    }

    public T getModel() {
        return model;
    }

    public int getPosicionPersonaEnTabla() {
        return posicionPersonaEnTabla;
    }

    public ObservableList<T> getListTable() {
        return listTable;
    }

    public interface StatusControles {
        void setStatusControls();
    }

    public interface TableForeignKeig {
        void llenarRelacionTabla(String[] s, int i, int ststartSecondModel, TableColumn... columns);
    }

}
