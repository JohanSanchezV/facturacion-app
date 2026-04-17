package facturacion.proyecto.service;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.model.FacturaDetalle;
import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.awt.Desktop;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturacionService {

    public List<Cliente> buscarClientes(String criterio) throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "{ call PR_BUSCAR_CLIENTE_FACT(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, criterio);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdTercero(rs.getInt("ID_TERCERO"));
                    c.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    c.setNombre(rs.getString("NOMBRE"));
                    c.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                    c.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
                    c.setCorreo(rs.getString("CORREO"));
                    c.setTelefono(rs.getString("NUMERO_TELEFONO"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public FacturaDetalle buscarProductoFacturacion(String codigoProducto, int cantidad, double porcentajeIva) throws Exception {
        String sql = "{ call PR_BUSCAR_PRODUCTO_FACT(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, codigoProducto);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (!rs.next()) {
                    throw new Exception("No existe un producto con ese código.");
                }

                double precio = rs.getDouble("PRECIO_UNITARIO");
                double subtotal = round(precio * cantidad);
                double impuesto = round(subtotal * (porcentajeIva / 100.0));
                double total = round(subtotal + impuesto);

                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setCodigoProducto(rs.getString("CODIGO_PRODUCTO"));
                detalle.setNombreProducto(rs.getString("NOMBRE"));
                detalle.setDescripcionEditable(rs.getString("DESCRIPCION"));
                detalle.setCantidad(cantidad);
                detalle.setPorcentajeIva(porcentajeIva);
                detalle.setSubtotal(subtotal);
                detalle.setImpuesto(impuesto);
                detalle.setTotal(total);
                return detalle;
            }
        }
    }

    public FacturaResumen crearFacturaPreliminar(int idEmisor, int idTercero, int idUsuarioApp, LocalDate fecha) throws Exception {
        String sql = "{ call PR_CREAR_FACTURA_PRELIMINAR(?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idEmisor);
            stmt.setInt(2, idTercero);
            stmt.setInt(3, idUsuarioApp);
            stmt.setDate(4, Date.valueOf(fecha));
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.execute();

            FacturaResumen resumen = new FacturaResumen();
            resumen.setIdFactura(stmt.getInt(5));
            resumen.setNumeroFactura(stmt.getString(6));
            resumen.setFechaEmision(fecha);
            return resumen;
        }
    }

    public void agregarLineaFactura(int idFactura, String codigoProducto, int cantidad, double porcentajeIva, String descripcionEditable) throws Exception {
        String sql = "{ call PR_AGREGAR_LINEA_FACTURA(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idFactura);
            stmt.setString(2, codigoProducto);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, porcentajeIva);
            stmt.setString(5, descripcionEditable);
            stmt.execute();
        }
    }

    public void emitirFactura(int idFactura, int idUsuarioApp) throws Exception {
        String sql = "{ call PR_EMITIR_FACTURA(?, ?) }";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setInt(2, idUsuarioApp);
            stmt.execute();
        }
    }

    public void guardarRutaPdf(int idFactura, String ruta) throws Exception {
        String sql = "{ call PR_GUARDAR_RUTA_PDF(?, ?) }";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setString(2, ruta);
            stmt.execute();
        }
    }

    public List<FacturaDetalle> obtenerDetalleFactura(int idFactura) throws Exception {
        List<FacturaDetalle> lista = new ArrayList<>();
        String sql = "{ call PR_DETALLE_FACTURA(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idFactura);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    FacturaDetalle detalle = new FacturaDetalle();
                    detalle.setCodigoProducto(rs.getString("CODIGO_PRODUCTO"));
                    detalle.setNombreProducto(rs.getString("NOMBRE_PRODUCTO"));
                    detalle.setDescripcionEditable(rs.getString("DESCRIPCION"));
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPorcentajeIva(rs.getDouble("PORCENTAJE_IVA"));
                    detalle.setSubtotal(rs.getDouble("SUBTOTAL"));
                    detalle.setImpuesto(rs.getDouble("IMPUESTO"));
                    detalle.setTotal(rs.getDouble("TOTAL"));
                    lista.add(detalle);
                }
            }
        }
        return lista;
    }

    public List<FacturaResumen> listarFacturasPendientes(String nombre, String identificacion, LocalDate desde, LocalDate hasta) throws Exception {
        return listarFacturasBase("{ call PR_LISTAR_FACTURAS_PENDIENTES(?, ?, ?, ?, ?) }", nombre, identificacion, desde, hasta);
    }

    public List<FacturaResumen> listarFacturasEmitidas(String nombre, String identificacion, LocalDate desde, LocalDate hasta) throws Exception {
        return listarFacturasBase("{ call PR_LISTAR_FACTURAS_EMITIDAS(?, ?, ?, ?, ?) }", nombre, identificacion, desde, hasta);
    }

    private List<FacturaResumen> listarFacturasBase(String sql, String nombre, String identificacion, LocalDate desde, LocalDate hasta) throws Exception {
        List<FacturaResumen> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (nombre == null || nombre.isBlank()) stmt.setNull(1, Types.VARCHAR); else stmt.setString(1, nombre);
            if (identificacion == null || identificacion.isBlank()) stmt.setNull(2, Types.VARCHAR); else stmt.setString(2, identificacion);
            if (desde == null) stmt.setNull(3, Types.DATE); else stmt.setDate(3, Date.valueOf(desde));
            if (hasta == null) stmt.setNull(4, Types.DATE); else stmt.setDate(4, Date.valueOf(hasta));
            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    FacturaResumen f = new FacturaResumen();
                    f.setIdFactura(rs.getInt("ID_FACTURA"));
                    f.setNumeroFactura(rs.getString("NUMERO_FACTURA"));
                    Date fecha = rs.getDate("FECHA_EMISION");
                    if (fecha != null) f.setFechaEmision(fecha.toLocalDate());
                    Timestamp ts = rs.getTimestamp("FECHA_HORA_EMISION");
                    if (ts != null) f.setFechaHoraEmision(ts.toLocalDateTime());
                    f.setNombreCliente(rs.getString("NOMBRE_CLIENTE"));
                    f.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    f.setTotalFactura(rs.getDouble("TOTAL_FACTURA"));
                    f.setRutaPdf(rs.getString("RUTA_PDF"));
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    public FacturaResumen obtenerFacturaEmitidaPorId(int idFactura) throws Exception {
        List<FacturaResumen> lista = listarFacturasEmitidas(null, null, null, null);
        return lista.stream().filter(f -> f.getIdFactura() == idFactura).findFirst()
                .orElseThrow(() -> new Exception("No se encontró la factura emitida."));
    }

    public FacturaResumen obtenerFacturaPendientePorId(int idFactura) throws Exception {
        List<FacturaResumen> lista = listarFacturasPendientes(null, null, null, null);
        return lista.stream().filter(f -> f.getIdFactura() == idFactura).findFirst()
                .orElseThrow(() -> new Exception("No se encontró la factura pendiente."));
    }

    public void abrirPdf(String ruta) throws Exception {
        if (ruta == null || ruta.isBlank()) {
            throw new Exception("La factura todavía no tiene PDF generado.");
        }
        Desktop.getDesktop().open(new File(ruta));
    }

    private double round(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
