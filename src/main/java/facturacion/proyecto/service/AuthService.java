package facturacion.proyecto.service;

import facturacion.proyecto.model.SesionUsuario;
import facturacion.proyecto.util.ConexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class AuthService {

    public SesionUsuario login(String correo, String password) throws Exception {
        String sql = "{ call PR_LOGIN_USUARIO_CORREO(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection conn = ConexionBD.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, password);

            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.registerOutParameter(7, Types.VARCHAR);

            stmt.execute();

            int resultado = stmt.getInt(6);
            String mensaje = stmt.getString(7);

            if (resultado == 1) {
                int idUsuario = stmt.getInt(3);
                String nombreUsuario = stmt.getString(4);
                String nombreRol = stmt.getString(5);

                return new SesionUsuario(idUsuario, nombreUsuario, nombreRol);
            } else {
                throw new Exception(mensaje);
            }
        }
    }
}