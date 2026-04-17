package facturacion.proyecto.service;

import facturacion.proyecto.model.AuditoriaRegistro;
import facturacion.proyecto.model.ReporteFacturaCliente;
import facturacion.proyecto.model.ReporteVentaFecha;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.awt.Desktop;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportesAuditoriaService {

    public List<ReporteVentaFecha> reporteVentasPorFechas(LocalDate desde, LocalDate hasta) throws Exception {
        List<ReporteVentaFecha> lista = new ArrayList<>();
        String sql = "{ call PR_REPORTE_VENTAS_FECHAS(?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (desde == null) stmt.setNull(1, Types.DATE); else stmt.setDate(1, Date.valueOf(desde));
            if (hasta == null) stmt.setNull(2, Types.DATE); else stmt.setDate(2, Date.valueOf(hasta));

            stmt.registerOutParameter(3, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                while (rs.next()) {
                    ReporteVentaFecha r = new ReporteVentaFecha();
                    Date fecha = rs.getDate("FECHA");
                    if (fecha != null) r.setFecha(fecha.toLocalDate());
                    r.setCantidadFacturas(rs.getInt("CANTIDAD_FACTURAS"));
                    r.setTotalVentas(rs.getDouble("TOTAL_VENTAS"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }

    public List<ReporteFacturaCliente> reporteVentasPorCliente(String nombre, String identificacion, LocalDate desde, LocalDate hasta) throws Exception {
        List<ReporteFacturaCliente> lista = new ArrayList<>();
        String sql = "{ call PR_REPORTE_VENTAS_POR_CLIENTE(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (nombre == null || nombre.isBlank()) stmt.setNull(1, Types.VARCHAR); else stmt.setString(1, nombre.trim());
            if (identificacion == null || identificacion.isBlank()) stmt.setNull(2, Types.VARCHAR); else stmt.setString(2, identificacion.trim());
            if (desde == null) stmt.setNull(3, Types.DATE); else stmt.setDate(3, Date.valueOf(desde));
            if (hasta == null) stmt.setNull(4, Types.DATE); else stmt.setDate(4, Date.valueOf(hasta));

            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    ReporteFacturaCliente r = new ReporteFacturaCliente();
                    r.setIdFactura(rs.getInt("ID_FACTURA"));
                    r.setNumeroFactura(rs.getString("NUMERO_FACTURA"));
                    r.setNombreCliente(rs.getString("NOMBRE_CLIENTE"));
                    r.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    Date fecha = rs.getDate("FECHA_EMISION");
                    if (fecha != null) r.setFechaEmision(fecha.toLocalDate());
                    Timestamp ts = rs.getTimestamp("FECHA_HORA_EMISION");
                    if (ts != null) r.setFechaHoraEmision(ts.toLocalDateTime());
                    r.setTotalFactura(rs.getDouble("TOTAL_FACTURA"));
                    r.setRutaPdf(rs.getString("RUTA_PDF"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }

    public List<AuditoriaRegistro> reporteAuditoria(String modulo, String tabla, LocalDate desde, LocalDate hasta) throws Exception {
        List<AuditoriaRegistro> lista = new ArrayList<>();
        String sql = "{ call PR_REPORTE_AUDITORIA(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (modulo == null || modulo.isBlank()) stmt.setNull(1, Types.VARCHAR); else stmt.setString(1, modulo.trim());
            if (tabla == null || tabla.isBlank()) stmt.setNull(2, Types.VARCHAR); else stmt.setString(2, tabla.trim());
            if (desde == null) stmt.setNull(3, Types.DATE); else stmt.setDate(3, Date.valueOf(desde));
            if (hasta == null) stmt.setNull(4, Types.DATE); else stmt.setDate(4, Date.valueOf(hasta));

            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    AuditoriaRegistro a = new AuditoriaRegistro();
                    a.setIdAuditoria(rs.getInt("ID_AUDITORIA"));
                    a.setModulo(rs.getString("MODULO"));
                    a.setAccion(rs.getString("ACCION"));
                    a.setTablaAfectada(rs.getString("TABLA_AFECTADA"));
                    a.setIdRegistro(rs.getString("ID_REGISTRO"));
                    a.setDetalle(rs.getString("DETALLE"));
                    a.setIdUsuarioApp(rs.getInt("ID_USUARIO_APP"));
                    a.setNombreUsuario(rs.getString("NOMBRE_USUARIO"));
                    a.setIdentificacionUsuario(rs.getString("IDENTIFICACION_USUARIO"));
                    Timestamp ts = rs.getTimestamp("FECHA_HORA");
                    if (ts != null) a.setFechaHora(ts.toLocalDateTime());
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public void abrirPdf(String rutaPdf) throws Exception {
        if (rutaPdf == null || rutaPdf.isBlank()) throw new Exception("La factura no tiene una ruta de PDF registrada.");
        File file = new File(rutaPdf);
        if (!file.exists()) throw new Exception("No existe el archivo PDF en la ruta registrada.");
        Desktop.getDesktop().open(file);
    }
}
