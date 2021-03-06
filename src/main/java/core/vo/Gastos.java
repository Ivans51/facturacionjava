package core.vo;

import java.util.Date;

public class Gastos {

    private int idgastos;
    private double monto;
    private String concepto;
    private String tipoPago;
    private String ncuenta;
    private int estado;
    private Date fecha;
    private String fechaEdit;
    private String montoEdit;
    private int usuario_cedula;

    public int getIdgastos() {
        return idgastos;
    }

    public void setIdgastos(int idgastos) {
        this.idgastos = idgastos;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getNcuenta() {
        return ncuenta;
    }

    public void setNcuenta(String ncuenta) {
        this.ncuenta = ncuenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getUsuario_cedula() {
        return usuario_cedula;
    }

    public void setUsuario_cedula(int usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFechaEdit() {
        return fechaEdit;
    }

    public void setFechaEdit(String fechaEdit) {
        this.fechaEdit = fechaEdit;
    }

    public String getMontoEdit() {
        return montoEdit;
    }

    public void setMontoEdit(String montoEdit) {
        this.montoEdit = montoEdit;
    }
}
