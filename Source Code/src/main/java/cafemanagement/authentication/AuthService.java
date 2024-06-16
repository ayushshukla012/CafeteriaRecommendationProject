package cafemanagement.authentication;

import cafemanagement.model.User;
import cafemanagement.service.UserService;

public class AuthService {
    private UserService userService;
    private User loggedInUser;

    public AuthService() {
        this.userService = new UserService();
    }

    public User login(int userId, String password, String roleName) {
        return userService.authenticate(userId, password, roleName);
    }

    public String logout() {
        if (loggedInUser == null) {
            return "No user is logged in.";
        }
        String name = loggedInUser.getName();
        loggedInUser = null;
        return "Goodbye " + name + "!";
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
