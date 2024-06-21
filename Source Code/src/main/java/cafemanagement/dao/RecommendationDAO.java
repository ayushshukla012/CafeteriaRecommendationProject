package cafemanagement.dao;

import java.sql.*;

import cafemanagement.utils.DatabaseUtil;
import cafemanagement.model.Recommendation;

public class RecommendationDAO {

    public void saveRecommendation(Recommendation recommendation) {
        String query = "INSERT INTO RecommendationEngine (menuId, averageRating, sentimentAnalysis) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, recommendation.getMenuId());
            statement.setDouble(2, recommendation.getAverageRating());
            statement.setString(3, recommendation.getSentimentAnalysis());

            statement.executeUpdate();
            System.out.println("Success");
        } catch (SQLException e) {
            System.out.println("failure");
            e.printStackTrace();
        }
    }
}
