package core.vo;

public class Ordenes {

    private int idordenes;
    private String nombre;
    private String status;
    private int factura_idfactura;

    public int getIdordenes() {
        return idordenes;
    }

    public void setIdordenes(int idordenes) {
        this.idordenes = idordenes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFactura_idfactura() {
        return factura_idfactura;
    }

    public void setFactura_idfactura(int factura_idfactura) {
        this.factura_idfactura = factura_idfactura;
    }
}
