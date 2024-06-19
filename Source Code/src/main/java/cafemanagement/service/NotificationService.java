package cafemanagement.service;

import cafemanagement.utils.DatabaseUtil;
import cafemanagement.model.Notification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


public class NotificationService {

    public void sendNotification(int senderId, String notificationType, int menuItemId, String message, List<Integer> receiverIds) {
        String insertNotificationSQL = "INSERT INTO Notifications (senderId, notificationType, menuItemId, message) VALUES (?, ?, ?, ?)";
        String insertUserNotificationSQL = "INSERT INTO UserNotifications (receiverId, notificationId) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement psNotification = conn.prepareStatement(insertNotificationSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insert notification into Notifications table
            psNotification.setInt(1, senderId);
            psNotification.setString(2, notificationType);
            psNotification.setInt(3, menuItemId);
            psNotification.setString(4, message);
            psNotification.executeUpdate();

            // Retrieve generated notification ID
            ResultSet rs = psNotification.getGeneratedKeys();
            int notificationId = -1;
            if (rs.next()) {
                notificationId = rs.getInt(1);
            }

            // Insert user notifications into UserNotifications table
            try (PreparedStatement psUserNotification = conn.prepareStatement(insertUserNotificationSQL)) {
                for (int receiverId : receiverIds) {
                    psUserNotification.setInt(1, receiverId);
                    psUserNotification.setInt(2, notificationId);
                    psUserNotification.addBatch();
                }
                psUserNotification.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getUnreadNotifications(int employeeId) {
        List<Notification> notifications = new ArrayList<>();

        String query = "SELECT n.notificationId, n.senderId, n.notificationType, n.menuItemId, n.message, n.notificationDate " +
                       "FROM Notifications n " +
                       "JOIN UserNotifications un ON n.notificationId = un.notificationId " +
                       "WHERE un.receiverId = ? AND un.isRead = FALSE " +
                       "ORDER BY n.notificationDate ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employeeId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int notificationId = rs.getInt("notificationId");
                int senderId = rs.getInt("senderId");
                String notificationType = rs.getString("notificationType");
                int menuItemId = rs.getInt("menuItemId");
                String message = rs.getString("message");
                java.sql.Timestamp notificationDate = rs.getTimestamp("notificationDate");

                Notification notification = new Notification(notificationId, senderId, notificationType, menuItemId, message, notificationDate);
                notifications.add(notification);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public void markNotificationAsRead(int notificationId, int receiverId) {
        String updateQuery = "UPDATE UserNotifications SET isRead = TRUE WHERE notificationId = ? AND receiverId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
             
            statement.setInt(1, notificationId);
            statement.setInt(2, receiverId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle properly in your application, e.g., logging
        }
    }
}
