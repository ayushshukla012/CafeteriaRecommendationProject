package cafemanagement.service;

import cafemanagement.dao.NotificationDAO;
import cafemanagement.model.Notification;

import java.util.List;

public class NotificationService {
    private final NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    public void sendNotification(int senderId, String notificationType, int menuItemId, String message, List<Integer> receiverIds) {
        notificationDAO.sendNotification(senderId, notificationType, menuItemId, message, receiverIds);
    }

    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotifications(userId);
    }

    public void markNotificationAsRead(int notificationId, int receiverId) {
        notificationDAO.markNotificationAsRead(notificationId, receiverId);
    }
}
