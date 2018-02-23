package core.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Servicios {

    private int idservicios;
    private String nombre;
    private Double precio;
    private Date fecha;
    private String tiempo_estimado;
    private SubServicios subServicios;
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

    public SubServicios getSubServicios() {
        return subServicios;
    }

    public void setSubServicios(SubServicios subServicios) {
        this.subServicios = subServicios;
    }

    /*public List<SubServicios> getSubServiciosList() {
        return subServiciosList;
    }

    public void setSubServiciosList(List<SubServicios> subServiciosList) {
        this.subServiciosList = subServiciosList;
    }*/
}
