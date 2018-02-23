package core.vo;

import java.util.Date;

public class SubServicios {

    private int idsubservicio;
    private String nombreSub;
    private double precioSub;
    private Date fechaSub;
    private int tiempo_estimadoSub;

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
}
