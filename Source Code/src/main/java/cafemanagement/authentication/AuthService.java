package cafemanagement.authentication;

import cafemanagement.model.User;
import cafemanagement.service.UserService;

public class AuthService {
    private UserService userService;
    private User loggedInUser;

    public AuthService() {
        this.userService = new UserService();
    }

    public String login(int userId, String password, String roleName) {
        User user = userService.authenticate(userId, password, roleName);
        if (user != null) {
            loggedInUser = user;
            return "Login successful. Welcome " + user.getName() + "!";
        } else {
            return "Invalid credentials or role. Please try again.";
        }
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
