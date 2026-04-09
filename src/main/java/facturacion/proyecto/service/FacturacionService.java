package facturacion.proyecto.service;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.model.FacturaDetalle;
import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturacionService {

    public Cliente buscarClienteFacturacion(String identificacion) throws Exception {
        String sql = "{ call PR_BUSCAR_CLIENTE_FACTURACION(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.registerOutParameter(7, Types.VARCHAR);

            stmt.execute();

            int resultado = stmt.getInt(6);
            String mensaje = stmt.getString(7);

            if (resultado == 0) {
                throw new Exception(mensaje);
            }

            Cliente cliente = new Cliente();
            cliente.setIdTercero(stmt.getInt(2));
            cliente.setIdentificacion(identificacion);
            cliente.setNombre(stmt.getString(3));
            cliente.setCorreo(stmt.getString(4));
            cliente.setTelefono(stmt.getString(5));

            return cliente;
        }
    }

    public FacturaDetalle buscarProductoFacturacion(int idProducto, int cantidad, double porcentajeIva) throws Exception {
        String sql = "{ call PR_BUSCAR_PRODUCTO_FACTURACION(?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idProducto);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.DOUBLE);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.registerOutParameter(6, Types.VARCHAR);

            stmt.execute();

            int resultado = stmt.getInt(5);
            String mensaje = stmt.getString(6);

            if (resultado == 0) {
                throw new Exception(mensaje);
            }

            String nombre = stmt.getString(2);
            String descripcion = stmt.getString(4);
            double precio = stmt.getDouble(3);

            double subtotal = cantidad * precio;
            double impuesto = subtotal * (porcentajeIva / 100.0);
            double total = subtotal + impuesto;

            FacturaDetalle detalle = new FacturaDetalle();
            detalle.setIdProducto(idProducto);
            detalle.setNombreProducto(nombre);
            detalle.setDescripcionEditable(descripcion);
            detalle.setCantidad(cantidad);
            detalle.setPorcentajeIva(porcentajeIva);
            detalle.setSubtotal(subtotal);
            detalle.setImpuesto(impuesto);
            detalle.setTotal(total);

            return detalle;
        }
    }

    public int crearFacturaPreliminar(int idEmisor, int idTercero, int idUsuarioApp, LocalDate fecha) throws Exception {
        String sql = "{ call PR_CREAR_FACTURA_PRELIMINAR(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idEmisor);
            stmt.setInt(2, idTercero);
            stmt.setInt(3, idUsuarioApp);
            stmt.setDate(4, Date.valueOf(fecha));
            stmt.registerOutParameter(5, Types.INTEGER);

            stmt.execute();

            return stmt.getInt(5);
        }
    }

    public void agregarLineaFactura(int idFactura, int idProducto, int cantidad, double porcentajeIva) throws Exception {
        String sql = "{ call PR_AGREGAR_LINEA_FACTURA(?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idFactura);
            stmt.setInt(2, idProducto);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, porcentajeIva);

            stmt.execute();
        }
    }

    public void emitirFactura(int idFactura) throws Exception {
        String sql = "{ call PR_EMITIR_FACTURA(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idFactura);
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
                    detalle.setIdProducto(rs.getInt("ID_PRODUCTO"));
                    detalle.setNombreProducto(rs.getString("NOMBRE_PRODUCTO"));
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
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
        List<FacturaResumen> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_FACTURAS_PENDIENTES(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (nombre == null || nombre.isBlank()) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, nombre);
            }

            if (identificacion == null || identificacion.isBlank()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, identificacion);
            }

            if (desde == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(desde));
            }

            if (hasta == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(hasta));
            }

            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    FacturaResumen factura = new FacturaResumen();
                    factura.setIdFactura(rs.getInt("ID_FACTURA"));

                    Date fecha = rs.getDate("FECHA_EMISION");
                    if (fecha != null) {
                        factura.setFechaEmision(fecha.toLocalDate());
                    }

                    factura.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    factura.setNombreCliente(rs.getString("NOMBRE_CLIENTE"));
                    factura.setTotalFactura(rs.getDouble("TOTAL_FACTURA"));

                    lista.add(factura);
                }
            }
        }

        return lista;
    }

    public List<FacturaResumen> listarFacturasEmitidas(String nombre, String identificacion, LocalDate desde, LocalDate hasta) throws Exception {
        List<FacturaResumen> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_FACTURAS_EMITIDAS(?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (nombre == null || nombre.isBlank()) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, nombre);
            }

            if (identificacion == null || identificacion.isBlank()) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, identificacion);
            }

            if (desde == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(desde));
            }

            if (hasta == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(hasta));
            }

            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    FacturaResumen factura = new FacturaResumen();
                    factura.setIdFactura(rs.getInt("ID_FACTURA"));

                    Date fecha = rs.getDate("FECHA_EMISION");
                    if (fecha != null) {
                        factura.setFechaEmision(fecha.toLocalDate());
                    }

                    factura.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    factura.setNombreCliente(rs.getString("NOMBRE_CLIENTE"));
                    factura.setTotalFactura(rs.getDouble("TOTAL_FACTURA"));

                    lista.add(factura);
                }
            }
        }

        return lista;
    }
}