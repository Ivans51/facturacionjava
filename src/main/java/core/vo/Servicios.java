package core.vo;

import java.util.Date;

public class Servicios {

    private int idservicios;
    private String nombre;
    private Double precio;
    private Date fecha;
    private String tiempo_estimado;
    private int subservicio_idsubservicio;
    private int usuario_cedula;

    public int getIdservicios() {
        return idservicios;
    }

    public void setIdservicios(int idservicios) {
        this.idservicios = idservicios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTiempo_estimado() {
        return tiempo_estimado;
    }

    public void setTiempo_estimado(String tiempo_estimado) {
        this.tiempo_estimado = tiempo_estimado;
    }

    public int getSubservicio_idsubservicio() {
        return subservicio_idsubservicio;
    }

    public void setSubservicio_idsubservicio(int subservicio_idsubservicio) {
        this.subservicio_idsubservicio = subservicio_idsubservicio;
    }

    public int getUsuario_cedula() {
        return usuario_cedula;
    }

    public void setUsuario_cedula(int usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }
}
