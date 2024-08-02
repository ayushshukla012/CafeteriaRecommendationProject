package cafemanagement.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cafemanagement.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NotificationDaoTest {

    private NotificationDAO notificationDAO;
    private Connection mockConnection;
    private PreparedStatement mockPsNotification;
    private PreparedStatement mockPsUserNotification;
    private ResultSet mockResultSet;

    @Before
    public void setup() throws SQLException {
        notificationDAO = new NotificationDAO();

        mockConnection = mock(Connection.class);
        mockPsNotification = mock(PreparedStatement.class);
        mockPsUserNotification = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockPsNotification);
        when(mockPsNotification.executeUpdate()).thenReturn(1);
        when(mockPsNotification.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPsUserNotification);
    }

    @Test
    public void testSendNotification() throws SQLException {
        int senderId = 1;
        String notificationType = "NewMenuItem";
        int menuItemId = 10;
        String message = "New menu item added";
        List<Integer> receiverIds = List.of(2, 3);

        notificationDAO.sendNotification(senderId, notificationType, menuItemId, message, receiverIds);

        verify(mockConnection).prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS));
        verify(mockPsNotification).setInt(1, senderId);
        verify(mockPsNotification).setString(2, notificationType);
        verify(mockPsNotification).setInt(3, menuItemId);
        verify(mockPsNotification).setString(4, message);
        verify(mockPsNotification).executeUpdate();
        verify(mockPsNotification).getGeneratedKeys();

        verify(mockConnection, times(receiverIds.size())).prepareStatement(anyString());
        verify(mockPsUserNotification, times(receiverIds.size())).setInt(eq(1), anyInt());
        verify(mockPsUserNotification, times(receiverIds.size())).setInt(eq(2), eq(1));
        verify(mockPsUserNotification, times(receiverIds.size())).addBatch();
        verify(mockPsUserNotification, times(receiverIds.size())).executeBatch();
    }

    @Test
    public void testGetUnreadNotifications() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("notificationId")).thenReturn(1);
        when(mockResultSet.getInt("senderId")).thenReturn(2);
        when(mockResultSet.getString("notificationType")).thenReturn("Promotion");
        when(mockResultSet.getInt("menuItemId")).thenReturn(10);
        when(mockResultSet.getString("message")).thenReturn("Promotion message");
        when(mockResultSet.getTimestamp("notificationDate")).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));

        Notification notification = new Notification(
                mockResultSet.getInt("notificationId"),
                mockResultSet.getInt("senderId"),
                mockResultSet.getString("notificationType"),
                mockResultSet.getInt("menuItemId"),
                mockResultSet.getString("message"),
                mockResultSet.getTimestamp("notificationDate")
        );

        List<Notification> notifications = notificationDAO.getUnreadNotifications(2);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPsNotification).setInt(1, 2);
        verify(mockPsNotification).executeQuery();

        assertEquals(1, notifications.size());
        assertEquals("Promotion", notifications.get(0).getNotificationType());
    }

    @Test
    public void testMarkNotificationAsRead() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPsUserNotification);
        when(mockPsUserNotification.executeUpdate()).thenReturn(1);

        notificationDAO.markNotificationAsRead(1, 2);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPsUserNotification).setInt(1, 1);
        verify(mockPsUserNotification).setInt(2, 2);
        verify(mockPsUserNotification).executeUpdate();
    }
}
