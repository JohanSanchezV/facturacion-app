package facturacion.proyecto.service;

import facturacion.proyecto.model.Empresa;
import facturacion.proyecto.model.Estado;
import facturacion.proyecto.model.Rol;
import facturacion.proyecto.model.Usuario;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public List<Usuario> listarUsuarios() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_USUARIOS_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        }

        return lista;
    }

    public Usuario buscarPorIdentificacion(String identificacion) throws Exception {
        String sql = "{ call PR_BUSCAR_USUARIO_MODULO(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                } else {
                    throw new Exception("No existe un usuario con esa identificación.");
                }
            }
        }
    }

    public void insertar(Usuario usuario) throws Exception {
        String sql = "{ call PR_INSERTAR_USUARIO_MODULO(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario.getNumeroIdentificacion());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellidoPaterno());
            stmt.setString(4, usuario.getApellidoMaterno());
            stmt.setString(5, usuario.getCorreo());
            stmt.setInt(6, usuario.getIdEmisor());
            stmt.setInt(7, usuario.getIdRol());
            stmt.setString(8, usuario.getPassHash());
            stmt.execute();
        }
    }

    public void editar(Usuario usuario) throws Exception {
        String sql = "{ call PR_EDITAR_USUARIO_MODULO(?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario.getNumeroIdentificacion());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellidoPaterno());
            stmt.setString(4, usuario.getApellidoMaterno());
            stmt.setString(5, usuario.getCorreo());
            stmt.setInt(6, usuario.getIdEmisor());
            stmt.setInt(7, usuario.getIdRol());
            stmt.setInt(8, usuario.getIdEstado());
            stmt.setString(9, usuario.getPassHash());
            stmt.execute();
        }
    }

    public void eliminar(String identificacion) throws Exception {
        String sql = "{ call PR_ELIMINAR_USUARIO_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.execute();
        }
    }

    public List<Empresa> listarEmpresas() throws Exception {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT ID_EMISOR, NOMBRE_LEGAL FROM FIDE_EMPRESA_TB WHERE ID_ESTADO = 1 ORDER BY NOMBRE_LEGAL";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empresa empresa = new Empresa();
                empresa.setIdEmisor(rs.getInt("ID_EMISOR"));
                empresa.setNombreLegal(rs.getString("NOMBRE_LEGAL"));
                lista.add(empresa);
            }
        }
        return lista;
    }

    public List<Rol> listarRoles() throws Exception {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT ID_ROL, NOMBRE FROM FIDE_ROL_APP_TB WHERE ID_ESTADO = 1 ORDER BY NOMBRE";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("ID_ROL"));
                rol.setNombre(rs.getString("NOMBRE"));
                lista.add(rol);
            }
        }
        return lista;
    }

    public List<Estado> listarEstados() throws Exception {
        List<Estado> lista = new ArrayList<>();
        String sql = "SELECT ID_ESTADO, NOMBRE FROM FIDE_ESTADO_TB WHERE ID_ESTADO IN (1,2) ORDER BY ID_ESTADO";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estado estado = new Estado();
                estado.setIdEstado(rs.getInt("ID_ESTADO"));
                estado.setNombre(rs.getString("NOMBRE"));
                lista.add(estado);
            }
        }
        return lista;
    }

    private Usuario mapearUsuario(ResultSet rs) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuarioApp(rs.getInt("ID_USUARIO_APP"));
        usuario.setIdTercero(rs.getInt("ID_TERCERO"));
        usuario.setNumeroIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
        usuario.setNombre(rs.getString("NOMBRE"));
        usuario.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
        usuario.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
        usuario.setCorreo(rs.getString("CORREO"));
        usuario.setIdEmisor(rs.getInt("ID_EMISOR"));
        usuario.setNombreLegal(rs.getString("NOMBRE_EMPRESA"));
        usuario.setIdRol(rs.getInt("ID_ROL"));
        usuario.setNombreRol(rs.getString("NOMBRE_ROL"));
        usuario.setIdEstado(rs.getInt("ID_ESTADO"));
        usuario.setNombreEstado(rs.getString("NOMBRE_ESTADO"));
        usuario.setPassHash(rs.getString("PASS_HASH"));
        return usuario;
    }
}
