import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL = "jdbc:oracle:thin:@proyectolengdb_high";
    private static final String USER = "ufproy01";
    private static final String PASSWORD = "palabraPaso2026";
    private static final String TNS_ADMIN = "C:/sqldeveloper/Wallet_proyectolengdb";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.put("user", USER);
        props.put("password", PASSWORD);
        props.put("oracle.net.tns_admin", TNS_ADMIN);

        return DriverManager.getConnection(URL, props);
    }
}