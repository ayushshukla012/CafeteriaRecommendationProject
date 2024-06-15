package cafemanagement.service;

import cafemanagement.model.User;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class UserService {
    
    public User authenticate(int userId, String password, String roleName) {
        String query = "SELECT u.employeeId, u.name, u.password, r.roleName " +
                       "FROM Users u " +
                       "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE u.employeeId = ? AND u.password = ? AND r.roleName = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, roleName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"), rs.getString("roleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
