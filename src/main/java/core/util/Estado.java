package core.util;

public class Estado {

    // Alert
    public static final int EXITOSA = 1, ERROR = 2;

    // Estato de los controles
    public static final int DISABLE = 1;
    public static final int VISIBLE = 2;
    public static final int EDITABLE = 3;

    // Status Empleado
    public static final int NOLABORANDO = 0;
    public static final int TRABAJANDO = 1;
    public static final int DESPEDIDO = 2;
    public static final int DEPERMISO = 3;

    // Status Usuario
    public static final String USUARIO = "Usuario";
    public static final String ADMINISTRADOR = "Administrador";
    public static final String GERENTE = "Gerente";
}