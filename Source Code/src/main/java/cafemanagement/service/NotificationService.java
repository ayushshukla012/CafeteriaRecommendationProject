package cafemanagement.service;

import cafemanagement.model.User;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public void sendRecommendationNotification(int menuItemId) {
        String message = "Recommendation: Check out the new menu item for tomorrow!";
        String notificationType = "Recommendation";
        List<User> employees = getUsersWithRole("Employee");

        saveNotificationToDB(employees, notificationType, menuItemId, message);
    }

    public void sendNewFoodItemNotification(int menuItemId) {
        String message = "New Food Item added: Check out the new item on the menu!";
        String notificationType = "NewFoodItem";
        List<User> employees = getUsersWithRole("Employee");

        saveNotificationToDB(employees, notificationType, menuItemId, message);
    }

    public void sendAvailabilityChangeNotification(int menuItemId, boolean availability) {
        String message = "Availability Change: The availability of an item has changed to " + (availability ? "available" : "unavailable") + ".";
        String notificationType = "AvailabilityChange";
        List<User> employees = getUsersWithRole("Employee");

        saveNotificationToDB(employees, notificationType, menuItemId, message);
    }

    private List<User> getUsersWithRole(String roleName) {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.employeeId, u.name, u.password, r.roleName " +
                "FROM Users u " +
                "JOIN UserRoles ur ON u.employeeId = ur.userId " +
                "JOIN Roles r ON ur.roleId = r.roleId " +
                "WHERE r.roleName = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roleName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(new User(rs.getInt("employeeId"), rs.getString("name"), rs.getString("password"), rs.getString("roleName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void saveNotificationToDB(List<User> users, String notificationType, int menuItemId, String message) {
        String insertNotificationQuery = "INSERT INTO Notifications (receiverId, notificationType, menuItemId, message) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertNotificationQuery)) {

            for (User user : users) {
                pstmt.setInt(1, user.getEmployeeId());
                pstmt.setString(2, notificationType);
                pstmt.setInt(3, menuItemId);
                pstmt.setString(4, message);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
