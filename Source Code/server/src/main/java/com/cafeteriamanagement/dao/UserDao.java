package com.cafeteriamanagement.dao;

import com.cafeteriamanagement.dto.User;
import com.cafeteriamanagement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private ConnectionPool connectionPool;

    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User authenticate(int userId, String password, String roleName) {
        String query = "SELECT u.employeeId, u.name, u.password, r.roleName " +
                       "FROM Users u " +
                       "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE u.employeeId = ? AND u.password = ? AND r.roleName = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, roleName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"), rs.getString("roleName"));
            } else {
                System.out.println("No matching user found for userId: " + userId + ", roleName: " + roleName);
            }

            connectionPool.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
