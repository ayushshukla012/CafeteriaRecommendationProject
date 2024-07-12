package cafemanagement.service;

import java.sql.SQLException;

import cafemanagement.dao.UserPreferencesDAO;
import cafemanagement.model.UserPreferences;

public class UserPreferencesService {
    private final UserPreferencesDAO userPreferencesDAO;

    public UserPreferencesService() {
        this.userPreferencesDAO = new UserPreferencesDAO();
    }

    public UserPreferences getPreferencesByEmployeeId(int employeeId) {
        return userPreferencesDAO.getPreferencesByEmployeeId(employeeId);
    }

    public void updateUserPreferences(UserPreferences preferences) {
        userPreferencesDAO.updateUserPreferences(preferences);
    }

    public void insertUserPreferences(UserPreferences preferences) throws SQLException{
        userPreferencesDAO.insertUserPreferences(preferences);
    }
}
