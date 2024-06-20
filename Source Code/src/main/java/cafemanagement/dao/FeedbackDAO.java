package cafemanagement.dao;

import java.sql.*;

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
    
    public void updateFeedback(Feedback feedback) {
        String query = "UPDATE Feedback SET quality = ?, valueForMoney = ?, quantity = ?, taste = ?, rating = ?, comment = ?, feedbackDate = ? WHERE feedbackId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedback.getQuality());
            statement.setInt(2, feedback.getValueForMoney());
            statement.setInt(3, feedback.getQuantity());
            statement.setInt(4, feedback.getTaste());
            statement.setInt(5, feedback.getRating());
            statement.setString(6, feedback.getComment());
            statement.setDate(7, new Date(System.currentTimeMillis()));
            statement.setInt(8, feedback.getFeedbackId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
