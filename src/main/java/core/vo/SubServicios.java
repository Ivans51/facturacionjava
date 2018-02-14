package core.vo;

import java.util.Date;

public class SubServicios {

    private int idsubservicio;
    private String nombre;
    private double costo;
    private Date fecha;
    private int tiempo_estimado;

    public int getIdsubservicio() {
        return idsubservicio;
    }

    public void setIdsubservicio(int idsubservicio) {
        this.idsubservicio = idsubservicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTiempo_estimado() {
        return tiempo_estimado;
    }

    public void setTiempo_estimado(int tiempo_estimado) {
        this.tiempo_estimado = tiempo_estimado;
    }
}
