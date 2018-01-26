package core.util;

import core.vo.Usuario;

/**
 * Created by WAMS-10 on 30/07/2017.
 */
public class Storage {

    public static Usuario usuario;
    /*public static Valores valores;
    public static Empleado empleado;*/

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Storage.usuario = usuario;
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
