package facturacion.proyecto.service;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.util.ConexionBD;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    public Cliente buscarPorIdentificacion(String identificacion) throws Exception {
        String sql = "{ call PR_BUSCAR_CLIENTE_POR_IDENTIFICACION(?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, identificacion);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(7, Types.VARCHAR);
            stmt.registerOutParameter(8, Types.INTEGER);
            stmt.registerOutParameter(9, Types.VARCHAR);

            stmt.execute();

            int resultado = stmt.getInt(8);
            String mensaje = stmt.getString(9);

            if (resultado == 0) {
                throw new Exception(mensaje);
            }

            Cliente cliente = new Cliente();
            cliente.setIdTercero(stmt.getInt(2));
            cliente.setNombre(stmt.getString(3));
            cliente.setApellidoPaterno(stmt.getString(4));
            cliente.setApellidoMaterno(stmt.getString(5));
            cliente.setCorreo(stmt.getString(6));
            cliente.setTelefono(stmt.getString(7));
            cliente.setIdentificacion(identificacion);

            return cliente;
        }
    }

    public void insertar(Cliente cliente, int idDireccion) throws Exception {
        String sql = "{ call PR_INSERTAR_CLIENTE_COMPLETO(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, cliente.getIdTercero());
            stmt.setString(2, cliente.getIdentificacion());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellidoPaterno());
            stmt.setString(5, cliente.getApellidoMaterno());
            stmt.setString(6, cliente.getCorreo());
            stmt.setString(7, cliente.getTelefono());
            stmt.setInt(8, idDireccion);

            stmt.execute();
        }
    }

    public void editar(Cliente cliente, int idDireccion) throws Exception {
        String sql = "{ call PR_EDITAR_CLIENTE_COMPLETO(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, cliente.getIdTercero());
            stmt.setString(2, cliente.getIdentificacion());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellidoPaterno());
            stmt.setString(5, cliente.getApellidoMaterno());
            stmt.setString(6, cliente.getCorreo());
            stmt.setString(7, cliente.getTelefono());
            stmt.setInt(8, idDireccion);

            stmt.execute();
        }
    }

    public void eliminar(int idTercero) throws Exception {
        String sql = "{ call PR_ELIMINAR_CLIENTE_COMPLETO(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idTercero);
            stmt.execute();
        }
    }

    public List<Cliente> listarActivos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "{ call PR_LISTAR_CLIENTES_ACTIVOS(?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdTercero(rs.getInt("ID_TERCERO"));
                    cliente.setIdentificacion(rs.getString("NUMERO_IDENTIFICACION"));
                    cliente.setCorreo(rs.getString("CORREO"));
                    cliente.setTelefono(rs.getString("NUMERO_TELEFONO"));

                    String nombreCompleto = rs.getString("NOMBRE_COMPLETO");
                    String[] partes = nombreCompleto != null ? nombreCompleto.split(" ", 3) : new String[]{"", "", ""};

                    cliente.setNombre(partes.length > 0 ? partes[0] : "");
                    cliente.setApellidoPaterno(partes.length > 1 ? partes[1] : "");
                    cliente.setApellidoMaterno(partes.length > 2 ? partes[2] : "");

                    lista.add(cliente);
                }
            }
        }

        return lista;
    }
}