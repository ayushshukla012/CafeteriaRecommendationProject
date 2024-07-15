package com.cafeteriamanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionPool {

    private List<Connection> availableConnections = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE;
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    public ConnectionPool(String dbUrl, String dbUsername, String dbPassword, int initialPoolSize) {
        ConnectionPool.dbUrl = dbUrl;
        ConnectionPool.dbUsername = dbUsername;
        ConnectionPool.dbPassword = dbPassword;
        ConnectionPool.INITIAL_POOL_SIZE = initialPoolSize;

        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            availableConnections.add(createConnection());
        }
    }

    public Connection getConnection() {
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("Maximum pool size reached, no available connections!");
        }

        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    public static Connection createConnection() {
        try {
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating connection to the database", e);
        }
    }

    public int getAvailableConnectionsCount() {
        return availableConnections.size();
    }
}
