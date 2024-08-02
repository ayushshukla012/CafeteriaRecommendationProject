package cafemanagement.dao;

import cafemanagement.model.UserPreferences;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferencesDAO {
    
    public UserPreferences getPreferencesByEmployeeId(int employeeId) {
        String query = "SELECT * FROM UserPreferences WHERE employeeId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employeeId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UserPreferences preferences = new UserPreferences();
                preferences.setPreferenceId(rs.getInt("preferenceId"));
                preferences.setEmployeeId(rs.getInt("employeeId"));
                preferences.setDietaryPreference(rs.getString("dietaryPreference"));
                preferences.setSpiceLevel(rs.getString("spiceLevel"));
                preferences.setPreferredCuisine(rs.getString("preferredCuisine"));
                preferences.setSweetTooth(rs.getBoolean("sweetTooth"));
                return preferences;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserPreferences(UserPreferences preferences) {
        String query = "UPDATE UserPreferences SET dietaryPreference = ?, spiceLevel = ?, preferredCuisine = ?, sweetTooth = ? WHERE employeeId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, preferences.getDietaryPreference());
            statement.setString(2, preferences.getSpiceLevel());
            statement.setString(3, preferences.getPreferredCuisine());
            statement.setBoolean(4, preferences.isSweetTooth());
            statement.setInt(5, preferences.getEmployeeId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUserPreferences(UserPreferences preferences) throws SQLException {
        String query = "INSERT INTO UserPreferences (employeeId, dietaryPreference, spiceLevel, preferredCuisine, sweetTooth) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, preferences.getEmployeeId());
            statement.setString(2, preferences.getDietaryPreference());
            statement.setString(3, preferences.getSpiceLevel());
            statement.setString(4, preferences.getPreferredCuisine());
            statement.setBoolean(5, preferences.isSweetTooth());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User preferences inserted successfully.");
            } else {
                System.out.println("Failed to insert user preferences.");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting user preferences: " + e.getMessage());
            throw e;
        }
    }
}