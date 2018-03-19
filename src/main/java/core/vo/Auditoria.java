package core.vo;

import java.util.Date;

public class Auditoria {

    private int idAuditoria;
    private Date fecha;
    private String hora;
    private String accion;
    private String nombreUsuario;
    private int usuario_cedula;
    private String fechaEdit;

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getUsuario_cedula() {
        return usuario_cedula;
    }

    public void setUsuario_cedula(int usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "idAuditoria=" + idAuditoria +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", accion='" + accion + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", usuario_cedula=" + usuario_cedula +
                '}';
    }

    public String getFechaEdit() {
        return fechaEdit;
    }

    public void setFechaEdit(String fechaEdit) {
        this.fechaEdit = fechaEdit;
    }
}