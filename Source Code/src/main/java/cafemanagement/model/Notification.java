package cafemanagement.model;

import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int senderId;
    private String notificationType;
    private int menuItemId;
    private String message;
    private Timestamp notificationDate;

    public Notification(int notificationId, int senderId, String notificationType, int menuItemId, String message, Timestamp notificationDate) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.notificationType = notificationType;
        this.menuItemId = menuItemId;
        this.message = message;
        this.notificationDate = notificationDate;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getNotificationDate() {
        return notificationDate;
    }
}
