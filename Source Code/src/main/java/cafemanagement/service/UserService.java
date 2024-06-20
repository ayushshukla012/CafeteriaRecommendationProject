package cafemanagement.service;

import cafemanagement.dao.UserDAO;
import cafemanagement.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(int userId, String password, String roleName) {
        return userDAO.authenticate(userId, password, roleName);
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public List<Integer> getAllEmployeeIds() {
        return userDAO.getAllEmployeeIds();
    }

    public void logUserAttempt(int userId, String status) {
        userDAO.logUserAttempt(userId, status);
    }
}
