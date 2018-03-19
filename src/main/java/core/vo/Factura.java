package core.vo;

import java.util.Date;

public class Factura {

    private int idfactura;
    private String servicios;
    private String forma_pago;
    private Date fecha_pago;
    private String duracion;
    private Double IVA;
    private Double total;
    private int cliente_cedula;
    private int usuario_cedula;
    private String fecha_pagoEdit;

    public int getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(int idfactura) {
        this.idfactura = idfactura;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getCliente_cedula() {
        return cliente_cedula;
    }

    public void setCliente_cedula(int cliente_cedula) {
        this.cliente_cedula = cliente_cedula;
    }

    public int getUsuario_cedula() {
        return usuario_cedula;
    }

    public void setUsuario_cedula(int usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }

    public String getFecha_pagoEdit() {
        return fecha_pagoEdit;
    }

    public void setFecha_pagoEdit(String fecha_pagoEdit) {
        this.fecha_pagoEdit = fecha_pagoEdit;
    }
}
