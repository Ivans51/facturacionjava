package core.vo;

import java.util.Date;

public class SubServicios {

    private int idsubservicio;
    private String nombreSub;
    private double precioSub;
    private Date fechaSub;
    private int tiempo_estimadoSub;
    private int usuario_cedula;
    private int subservicio_idsubservicio;

    public int getIdsubservicio() {
        return idsubservicio;
    }

    public void setIdsubservicio(int idsubservicio) {
        this.idsubservicio = idsubservicio;
    }

    public String getNombreSub() {
        return nombreSub;
    }

    public void setNombreSub(String nombreSub) {
        this.nombreSub = nombreSub;
    }

    public double getPrecioSub() {
        return precioSub;
    }

    public void setPrecioSub(double precioSub) {
        this.precioSub = precioSub;
    }

    public Date getFechaSub() {
        return fechaSub;
    }

    public void setFechaSub(Date fechaSub) {
        this.fechaSub = fechaSub;
    }

    public int getTiempo_estimadoSub() {
        return tiempo_estimadoSub;
    }

    public void setTiempo_estimadoSub(int tiempo_estimadoSub) {
        this.tiempo_estimadoSub = tiempo_estimadoSub;
    }

    public int getUsuario_cedula() {
        return usuario_cedula;
    }

    public void setUsuario_cedula(int usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }

    public int getSubservicio_idsubservicio() {
        return subservicio_idsubservicio;
    }

    public void setSubservicio_idsubservicio(int subservicio_idsubservicio) {
        this.subservicio_idsubservicio = subservicio_idsubservicio;
    }
}
