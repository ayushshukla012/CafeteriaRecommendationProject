package com.cafeteriamanagement.dao;

import com.cafeteriamanagement.dto.User;

import java.sql.*;

public class UserDao {

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public UserDao(String dbUrl, String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        System.out.println("dbUrl"+dbUrl+"dbUsername"+dbUsername+"dbPassword"+dbPassword);
    }

    public User authenticate(int userId, String password, String roleName) {
        String query = "SELECT u.employeeId, u.name, u.password, r.roleName " +
                       "FROM Users u " +
                       "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE u.employeeId = ? AND u.password = ? AND r.roleName = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, roleName);
   
            // Logging the SQL query being executed
   System.out.println("Executing SQL query: " + pstmt.toString());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"), rs.getString("roleName"));
            } else {
                System.out.println("No matching user found for userId: " + userId + ", roleName: " + roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}