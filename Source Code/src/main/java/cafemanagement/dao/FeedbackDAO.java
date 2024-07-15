package cafemanagement.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cafemanagement.model.Feedback;
import cafemanagement.utils.DatabaseUtil;

public class FeedbackDAO {

    public Feedback getFeedbackByEmployeeAndMenu(int employeeId, int menuId) {
    String query = "SELECT * FROM Feedback WHERE employeeId = ? AND menuId = ?";
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, employeeId);
        statement.setInt(2, menuId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                feedback.setEmployeeId(resultSet.getInt("employeeId"));
                feedback.setMenuId(resultSet.getInt("menuId"));
                feedback.setQuality(resultSet.getInt("quality"));
                feedback.setValueForMoney(resultSet.getInt("valueForMoney"));
                feedback.setQuantity(resultSet.getInt("quantity"));
                feedback.setTaste(resultSet.getInt("taste"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setComment(resultSet.getString("comment"));
                feedback.setFeedbackDate(resultSet.getDate("feedbackDate"));
                return feedback;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    public void storeFeedback(Feedback feedback) {
        String query = "INSERT INTO Feedback (employeeId, menuId, quality, valueForMoney, quantity, taste, rating, comment, feedbackDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedback.getEmployeeId());
            statement.setInt(2, feedback.getMenuId());
            statement.setInt(3, feedback.getQuality());
            statement.setInt(4, feedback.getValueForMoney());
            statement.setInt(5, feedback.getQuantity());
            statement.setInt(6, feedback.getTaste());
            statement.setInt(7, feedback.getRating());
            statement.setString(8, feedback.getComment());
            statement.setDate(9, new Date(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean updateFeedback(Feedback feedback) {
        String query = "UPDATE Feedback SET quality = ?, valueForMoney = ?, quantity = ?, taste = ?, rating = ?, comment = ?, feedbackDate = ? WHERE feedbackId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, feedback.getQuality());
            statement.setInt(2, feedback.getValueForMoney());
            statement.setInt(3, feedback.getQuantity());
            statement.setInt(4, feedback.getTaste());
            statement.setInt(5, feedback.getRating());
            statement.setString(6, feedback.getComment());
            statement.setDate(7, new Date(feedback.getFeedbackDate().getTime()));
            statement.setInt(8, feedback.getFeedbackId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            } else {
                System.out.println("No feedback found with feedbackId: {0}" + feedback.getFeedbackId());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating feedback with feedbackId: " + feedback.getFeedbackId() + e);
            return false;
        }
    }

    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM Feedback";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                feedback.setEmployeeId(resultSet.getInt("employeeId"));
                feedback.setMenuId(resultSet.getInt("menuId"));
                feedback.setQuality(resultSet.getInt("quality"));
                feedback.setValueForMoney(resultSet.getInt("valueForMoney"));
                feedback.setQuantity(resultSet.getInt("quantity"));
                feedback.setTaste(resultSet.getInt("taste"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setComment(resultSet.getString("comment"));
                feedback.setFeedbackDate(resultSet.getDate("feedbackDate"));

                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackList;
    }

    public Feedback getFeedbackById(int feedbackId) {
        Feedback feedback = new Feedback();
        String query = "SELECT * FROM Feedback WHERE feedbackId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedbackId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    feedback.setFeedbackId(resultSet.getInt("feedbackId"));
                    feedback.setEmployeeId(resultSet.getInt("employeeId"));
                    feedback.setMenuId(resultSet.getInt("menuId"));
                    feedback.setQuality(resultSet.getInt("quality"));
                    feedback.setValueForMoney(resultSet.getInt("valueForMoney"));
                    feedback.setQuantity(resultSet.getInt("quantity"));
                    feedback.setTaste(resultSet.getInt("taste"));
                    feedback.setRating(resultSet.getInt("rating"));
                    feedback.setComment(resultSet.getString("comment"));
                    feedback.setFeedbackDate(resultSet.getDate("feedbackDate"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public void saveFeedback(int notificationId, int employeeId, String question, String response) {
        String query = "INSERT INTO FeedbackQuery (notificationId, employeeId, question, response, responseDate) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setInt(1, notificationId);
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, question);
            pstmt.setString(4, response);
            pstmt.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
