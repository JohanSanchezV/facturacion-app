package facturacion.proyecto.service;

import facturacion.proyecto.model.Producto;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    public Producto buscarPorId(int idProducto) throws Exception {
        String sql = "{ call PR_BUSCAR_PRODUCTO_COMPLETO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idProducto);

            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.DOUBLE);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(7, Types.INTEGER);
            stmt.registerOutParameter(8, Types.INTEGER);
            stmt.registerOutParameter(9, Types.INTEGER);
            stmt.registerOutParameter(10, Types.INTEGER);
            stmt.registerOutParameter(11, Types.INTEGER);
            stmt.registerOutParameter(12, Types.VARCHAR);

            stmt.execute();

            int resultado = stmt.getInt(11);
            String mensaje = stmt.getString(12);

            if (resultado == 0) {
                throw new Exception(mensaje);
            }

            Producto producto = new Producto();
            producto.setIdProducto(idProducto);
            producto.setNombre(stmt.getString(2));
            producto.setPrecio(stmt.getDouble(3));
            producto.setDescripcionNombre(stmt.getString(4));
            producto.setCategoriaNombre(stmt.getString(5));
            producto.setMaterialNombre(stmt.getString(6));
            producto.setIdDescripcion(stmt.getInt(7));
            producto.setIdCategoria(stmt.getInt(8));
            producto.setIdMaterial(stmt.getInt(9));
            producto.setIdEstado(stmt.getInt(10));

            return producto;
        }
    }

    public void insertar(Producto producto) throws Exception {
        String sql = "{ call PR_INSERTAR_PRODUCTO(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, producto.getIdProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setInt(3, producto.getIdDescripcion());
            stmt.setInt(4, producto.getIdCategoria());
            stmt.setInt(5, producto.getIdMaterial());
            stmt.setDouble(6, producto.getPrecio());
            stmt.setInt(7, 1);

            stmt.execute();
        }
    }

    public void editar(Producto producto) throws Exception {
        String sql = "{ call PR_EDITAR_PRODUCTO(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, producto.getIdProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setInt(3, producto.getIdDescripcion());
            stmt.setInt(4, producto.getIdCategoria());
            stmt.setInt(5, producto.getIdMaterial());
            stmt.setDouble(6, producto.getPrecio());
            stmt.setInt(7, 1);

            stmt.execute();
        }
    }

    public void eliminarLogico(int idProducto) throws Exception {
        String sql = "{ call PR_ELIMINAR_PRODUCTO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idProducto);
            stmt.execute();
        }
    }

    public List<Producto> listarActivos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_PRODUCTOS_ACTIVOS_COMPLETO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("ID_PRODUCTO"));
                    producto.setNombre(rs.getString("NOMBRE_PRODUCTO"));
                    producto.setPrecio(rs.getDouble("PRECIO_UNITARIO"));
                    producto.setDescripcionNombre(rs.getString("DESCRIPCION"));
                    producto.setCategoriaNombre(rs.getString("CATEGORIA"));
                    producto.setMaterialNombre(rs.getString("MATERIAL"));
                    lista.add(producto);
                }
            }
        }

        return lista;
    }
}