package cafemanagement.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
    private static ConnectionProvider connectionProvider;

    static {
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            connectionProvider = () -> DriverManager.getConnection(url, user, password);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    public static void setConnectionProvider(ConnectionProvider provider) {
        connectionProvider = provider;
    }
}
