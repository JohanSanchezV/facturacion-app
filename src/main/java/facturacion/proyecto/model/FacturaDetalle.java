package facturacion.proyecto.model;

public class FacturaDetalle {
    private int idProducto;
    private String nombreProducto;
    private String descripcionEditable;
    private int cantidad;
    private double porcentajeIva;
    private double subtotal;
    private double impuesto;
    private double total;

    public FacturaDetalle() {
    }

    public FacturaDetalle(int idProducto, String nombreProducto, int cantidad, double subtotal, double impuesto, double total) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionEditable() {
        return descripcionEditable;
    }

    public void setDescripcionEditable(String descripcionEditable) {
        this.descripcionEditable = descripcionEditable;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}