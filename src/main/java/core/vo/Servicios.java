package core.vo;

import java.util.Date;

public class Servicios {

    private int idservicios;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Date fecha;
    private int estado;
    private String tiempo_estimado;
    private String precioEdit;
    private String fechaEdit;
    /*private List<SubServicios> subServiciosList = new ArrayList<>();*/

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

    public int isEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioEdit() {
        return precioEdit;
    }

    public void setPrecioEdit(String precioEdit) {
        this.precioEdit = precioEdit;
    }

    public String getFechaEdit() {
        return fechaEdit;
    }

    public void setFechaEdit(String fechaEdit) {
        this.fechaEdit = fechaEdit;
    }
}
