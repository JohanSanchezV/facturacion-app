package facturacion.proyecto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FacturaResumen {
    private int idFactura;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private LocalDateTime fechaHoraEmision;
    private String identificacion;
    private String nombreCliente;
    private double totalFactura;
    private String rutaPdf;

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDateTime getFechaHoraEmision() { return fechaHoraEmision; }
    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) { this.fechaHoraEmision = fechaHoraEmision; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public double getTotalFactura() { return totalFactura; }
    public void setTotalFactura(double totalFactura) { this.totalFactura = totalFactura; }

    public String getRutaPdf() { return rutaPdf; }
    public void setRutaPdf(String rutaPdf) { this.rutaPdf = rutaPdf; }
}
