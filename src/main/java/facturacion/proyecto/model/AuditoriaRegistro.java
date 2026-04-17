package facturacion.proyecto.model;

import java.time.LocalDateTime;

public class AuditoriaRegistro {
    private int idAuditoria;
    private String modulo;
    private String accion;
    private String tablaAfectada;
    private String idRegistro;
    private String detalle;
    private int idUsuarioApp;
    private String nombreUsuario;
    private String identificacionUsuario;
    private LocalDateTime fechaHora;

    public int getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(int idAuditoria) { this.idAuditoria = idAuditoria; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }
    public String getIdRegistro() { return idRegistro; }
    public void setIdRegistro(String idRegistro) { this.idRegistro = idRegistro; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public int getIdUsuarioApp() { return idUsuarioApp; }
    public void setIdUsuarioApp(int idUsuarioApp) { this.idUsuarioApp = idUsuarioApp; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getIdentificacionUsuario() { return identificacionUsuario; }
    public void setIdentificacionUsuario(String identificacionUsuario) { this.identificacionUsuario = identificacionUsuario; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
