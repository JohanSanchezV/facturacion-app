package facturacion.proyecto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static final String USER = "PROYECTO_FACTURA_ELECTRONICA";
    private static final String PASSWORD = "Fidelitas_2026+";
    private static final String URL = "jdbc:oracle:thin:@lenguajesbd_low";
    private static final String TNS_ADMIN = "J:/LenguajesBD/wallet/Wallet_LenguajesBD";

    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        props.setProperty("oracle.net.tns_admin", TNS_ADMIN);

        return DriverManager.getConnection(URL, props);
    }
}