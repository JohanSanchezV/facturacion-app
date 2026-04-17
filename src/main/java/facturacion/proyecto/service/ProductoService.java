package facturacion.proyecto.service;

import facturacion.proyecto.model.Categoria;
import facturacion.proyecto.model.Material;
import facturacion.proyecto.model.Producto;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    public List<Producto> listarProductos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_PRODUCTOS_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        }

        return lista;
    }

    public Producto buscarPorCodigo(String codigo) throws Exception {
        String sql = "{ call PR_BUSCAR_PRODUCTO_MODULO(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, codigo);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    return mapearProducto(rs);
                } else {
                    throw new Exception("No existe un producto con ese código.");
                }
            }
        }
    }

    public void insertar(Producto producto) throws Exception {
        String sql = "{ call PR_INSERTAR_PRODUCTO_MODULO(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, producto.getCodigoProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getDescripcionNombre());
            stmt.setInt(4, producto.getIdCategoria());
            stmt.setInt(5, producto.getIdMaterial());
            stmt.setDouble(6, producto.getPrecio());
            stmt.setDouble(7, producto.getCantidadDisponible());
            stmt.setDouble(8, producto.getPorcentajeIva());

            stmt.execute();
        }
    }

    public void editar(Producto producto) throws Exception {
        String sql = "{ call PR_EDITAR_PRODUCTO_MODULO(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, producto.getCodigoProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getDescripcionNombre());
            stmt.setInt(4, producto.getIdCategoria());
            stmt.setInt(5, producto.getIdMaterial());
            stmt.setDouble(6, producto.getPrecio());
            stmt.setDouble(7, producto.getCantidadDisponible());
            stmt.setDouble(8, producto.getPorcentajeIva());

            stmt.execute();
        }
    }

    public void eliminar(String codigoProducto) throws Exception {
        String sql = "{ call PR_ELIMINAR_PRODUCTO_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, codigoProducto);
            stmt.execute();
        }
    }

    public void crearCategoria(String nombreCategoria) throws Exception {
        String sql = "{ call PR_CREAR_CATEGORIA_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombreCategoria);
            stmt.execute();
        }
    }

    public List<Categoria> listarCategorias() throws Exception {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT ID_CATEGORIA, NOMBRE " +
                     "FROM FIDE_CATEGORIA_TB " +
                     "WHERE ID_ESTADO = 1 " +
                     "ORDER BY NOMBRE";

        try (Connection conn = ConexionBD.getConnection();
             var ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                categoria.setNombre(rs.getString("NOMBRE"));
                lista.add(categoria);
            }
        }

        return lista;
    }

    public List<Material> listarMateriales() throws Exception {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT ID_MATERIAL, NOMBRE " +
                     "FROM FIDE_MATERIAL_TB " +
                     "WHERE ID_ESTADO = 1 " +
                     "ORDER BY NOMBRE";

        try (Connection conn = ConexionBD.getConnection();
             var ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Material material = new Material();
                material.setIdMaterial(rs.getInt("ID_MATERIAL"));
                material.setNombre(rs.getString("NOMBRE"));
                lista.add(material);
            }
        }

        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws Exception {
        Producto producto = new Producto();

        producto.setIdProducto(rs.getInt("ID_PRODUCTO"));
        producto.setCodigoProducto(rs.getString("CODIGO_PRODUCTO"));
        producto.setNombre(rs.getString("NOMBRE_PRODUCTO"));
        producto.setPrecio(rs.getDouble("PRECIO_UNITARIO"));
        producto.setCantidadDisponible(rs.getDouble("CANTIDAD_DISPONIBLE"));
        producto.setPorcentajeIva(rs.getDouble("PORCENTAJE_IVA"));
        producto.setDescripcionNombre(rs.getString("DESCRIPCION"));
        producto.setCategoriaNombre(rs.getString("CATEGORIA"));
        producto.setMaterialNombre(rs.getString("MATERIAL"));
        producto.setIdDescripcion(rs.getInt("ID_DESCRIPCION"));
        producto.setIdCategoria(rs.getInt("ID_CATEGORIA"));
        producto.setIdMaterial(rs.getInt("ID_MATERIAL"));
        producto.setIdEstado(rs.getInt("ID_ESTADO"));

        return producto;
    }
}
