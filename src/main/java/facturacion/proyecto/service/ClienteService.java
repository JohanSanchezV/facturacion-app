package facturacion.proyecto.service;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    public List<Cliente> listarClientes() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_CLIENTES_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        }

        return lista;
    }

    public Cliente buscarPorIdentificacion(String identificacion) throws Exception {
        String sql = "{ call PR_BUSCAR_CLIENTE_MODULO(?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    return mapearCliente(rs);
                } else {
                    throw new Exception("No existe un cliente con esa identificación.");
                }
            }
        }
    }

    public void insertar(Cliente cliente) throws Exception {
        String sql = "{ call PR_INSERTAR_CLIENTE_MODULO(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cliente.getIdentificacion());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellidoPaterno());
            stmt.setString(4, cliente.getApellidoMaterno());
            stmt.setString(5, cliente.getCorreo());
            stmt.setString(6, cliente.getTelefono());
            stmt.setInt(7, cliente.getIdDireccion());
            stmt.execute();
        }
    }

    public void editar(Cliente cliente) throws Exception {
        String sql = "{ call PR_EDITAR_CLIENTE_MODULO(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cliente.getIdentificacion());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellidoPaterno());
            stmt.setString(4, cliente.getApellidoMaterno());
            stmt.setString(5, cliente.getCorreo());
            stmt.setString(6, cliente.getTelefono());
            stmt.setInt(7, cliente.getIdDireccion());
            stmt.execute();
        }
    }

    public void eliminar(String identificacion) throws Exception {
        String sql = "{ call PR_ELIMINAR_CLIENTE_MODULO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.execute();
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdTercero(rs.getInt("ID_TERCERO"));
        cliente.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
        cliente.setNombre(rs.getString("NOMBRE"));
        cliente.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
        cliente.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
        cliente.setCorreo(rs.getString("CORREO"));
        cliente.setTelefono(rs.getString("NUMERO_TELEFONO"));
        cliente.setIdDireccion(rs.getInt("ID_DIRECCION"));
        return cliente;
    }
}
