package facturacion.proyecto.model;

import java.time.LocalDate;

public class ReporteVentaFecha {
    private LocalDate fecha;
    private int cantidadFacturas;
    private double totalVentas;

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public int getCantidadFacturas() { return cantidadFacturas; }
    public void setCantidadFacturas(int cantidadFacturas) { this.cantidadFacturas = cantidadFacturas; }
    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }
}
