package core.util;

import core.vo.Usuario;
import javafx.stage.Stage;

/**
 * Created by WAMS-10 on 30/07/2017.
 */
public class Storage {

    public static Usuario usuario;
    private static Stage stage;
    /*public static Valores valores;
    public static Empleado empleado;*/

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Storage.usuario = usuario;
    }

    public static void setStage(Stage stage) {
        Storage.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    /* public static Valores getValores() {
        return valores;
    }

    public static void setValores(Valores valores) {
        Storage.valores = valores;
    }

    public static Empleado getEmpleado() {
        return empleado;
    }

    public static void setEmpleado(Empleado empleado) {
        Storage.empleado = empleado;
    }*/
}
