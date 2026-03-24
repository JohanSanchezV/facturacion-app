package facturacion.proyecto;

import facturacion.proyecto.util.ConexionBD;
import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {
        try (Connection conn = ConexionBD.getConnection()) {
            System.out.println("Conexión exitosa a Oracle Autonomous Database.");
        } catch (Exception e) {
            System.out.println("Error de conexión:");
            e.printStackTrace();
        }
    }
}