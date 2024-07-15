package cafemanagement.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

import cafemanagement.model.Feedback;
import cafemanagement.utils.DatabaseUtil;

public class FeedbackDaoTest {

    private FeedbackDAO feedbackDAO;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup() throws SQLException {
        feedbackDAO = new FeedbackDAO();

        when(DatabaseUtil.getConnection()).thenReturn(mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetFeedbackByEmployeeAndMenu() throws SQLException {
        // Mock ResultSet data
        when(mockResultSet.next()).thenReturn(true); // Simulate ResultSet having data
        when(mockResultSet.getInt("feedbackId")).thenReturn(1);
        when(mockResultSet.getInt("employeeId")).thenReturn(1);
        when(mockResultSet.getInt("menuId")).thenReturn(1);
        when(mockResultSet.getInt("quality")).thenReturn(5);
        when(mockResultSet.getInt("valueForMoney")).thenReturn(4);
        when(mockResultSet.getInt("quantity")).thenReturn(3);
        when(mockResultSet.getInt("taste")).thenReturn(4);
        when(mockResultSet.getInt("rating")).thenReturn(5);
        when(mockResultSet.getString("comment")).thenReturn("Great food!");
        when(mockResultSet.getDate("feedbackDate")).thenReturn(new java.sql.Date(new Date().getTime()));

        Feedback feedback = feedbackDAO.getFeedbackByEmployeeAndMenu(1, 1);

        // Verify interactions with mocks
        verify(mockConnection).prepareStatement(anyString()); // Verify prepareStatement was called
        verify(mockStatement).setInt(1, 1); // Verify setInt parameters
        verify(mockStatement).setInt(2, 1); // Verify setInt parameters
        verify(mockStatement).executeQuery(); // Verify executeQuery was called

        // Verify feedback object properties
        assertEquals(1, feedback.getFeedbackId());
        assertEquals(1, feedback.getEmployeeId());
        assertEquals(1, feedback.getMenuId());
        assertEquals(5, feedback.getQuality());
        assertEquals(4, feedback.getValueForMoney());
        assertEquals(3, feedback.getQuantity());
        assertEquals(4, feedback.getTaste());
        assertEquals(5, feedback.getRating());
        assertEquals("Great food!", feedback.getComment());
        // Adjust date comparison based on your requirements
        assertEquals(new Date().toString(), feedback.getFeedbackDate().toString());
    }

    @Test
    public void testStoreFeedback() throws SQLException {
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);

        Feedback feedback = new Feedback();
        feedback.setEmployeeId(1);
        feedback.setMenuId(1);

        feedbackDAO.storeFeedback(feedback);

        verify(mockConnection).prepareStatement(anyString(), anyInt());
        verify(mockStatement).setInt(1, feedback.getEmployeeId());
        verify(mockStatement).setInt(2, feedback.getMenuId());
    }

    @Test
    public void testUpdateFeedback() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); // Simulate update success

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(1);
        feedback.setQuality(5);

        boolean updated = feedbackDAO.updateFeedback(feedback);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, feedback.getQuality());

        assertTrue(updated);
    }

    @Test
    public void testGetAllFeedback() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getInt("feedbackId")).thenReturn(1, 2); // Mock different IDs

        List<Feedback> feedbackList = feedbackDAO.getAllFeedback();

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery();

        assertEquals(2, feedbackList.size());
    }

    @Test
    public void testGetFeedbackById() throws SQLException {

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("feedbackId")).thenReturn(1);
        when(mockResultSet.getInt("employeeId")).thenReturn(1);
        when(mockResultSet.getInt("menuId")).thenReturn(1);

        Feedback feedback = feedbackDAO.getFeedbackById(1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).executeQuery();

        assertEquals(1, feedback.getFeedbackId());
        assertEquals(1, feedback.getEmployeeId());
        assertEquals(1, feedback.getMenuId());
    }

}
