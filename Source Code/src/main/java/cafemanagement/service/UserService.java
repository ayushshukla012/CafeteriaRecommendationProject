package cafemanagement.service;

import cafemanagement.model.User;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
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

    public User getUserByUsername(String username) {
        String query = "SELECT u.employeeId, u.name, u.password, r.roleName " +
                       "FROM Users u " +
                       "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE u.name = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"), rs.getString("roleName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> getAllEmployeeIds() {
        List<Integer> employeeIds = new ArrayList<>();
    
        String query = "SELECT u.employeeId " +
                       "FROM Users u " +
                       "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                       "JOIN Roles r ON ur.roleId = r.roleId " +
                       "WHERE r.roleName = 'employee'";
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                employeeIds.add(rs.getInt("employeeId"));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return employeeIds;
    }
}
