package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.NotificationService;
import cafemanagement.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Chef {
    private User currentUser;
    private PrintWriter writer;
    private BufferedReader userInput;
    private NotificationService notificationService;
    private UserService userService;
    

    public Chef(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.userService = new UserService();
    }

    public void start() {
        while (true) {
            displayMenu();
            try {
                System.out.print("\nChoose an option: ");
                String input = userInput.readLine().trim();
                handleInput(input);
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("Chef Menu:");
        System.out.println("1. Send notifications");
        System.out.println("2. View user feedback");
        System.out.println("3. Generate Feedback Report");
        System.out.println("4. Send dishes to review");
        System.out.println("5. Logout");
    }

    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    sendNotification();
                    break;
                case "2":
                    viewUserFeedback();
                    break;
                case "3":
                    generateFeedbackReport();
                    break;
                case "4":
                    sendDishesToReview();
                    break;
                case "5":
                    logout();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number.");
            }
        } catch (IOException e) {
            System.err.println("Error handling input: " + e.getMessage());
        }
    }

     private void sendNotification() throws IOException {
        try {
            String notificationType = "Recommendation";

            System.out.println("Enter the item ID from Menu:");
            int menuItemId = readIntegerInput();

            System.out.println("Enter your message for Notification:");
            String message = readNotificationMessage();

            List<Integer> receiverIds = getAllEmployeeId(); // Send to all employees
            
            int senderId = getUserIdByCurrentUser(currentUser); //Current User id
 
            notificationService.sendNotification(senderId, notificationType, menuItemId, message, receiverIds);

            writer.println("ChefId: " + senderId + "Notification for " + notificationType + " sent.");
            System.out.println("Notification sent.");
        } catch (IOException e) {
            writer.println("ChefId: " + getUserIdByCurrentUser(currentUser) + "Error reading notification.");
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    private void viewUserFeedback() {
        System.out.println("Viewing user feedback...");
    }

    private void generateFeedbackReport() {
        System.out.println("Generating feedback report...");
    }

    private void sendDishesToReview() {
        System.out.println("Sending dishes to review...");
    }

    private void logout() {
        try {
            writer.println("LOGOUT");
            System.out.println("Logged out.");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error logging out: " + e.getMessage());
        }
    }

    private int getUserIdByCurrentUser(User currentUserInstance) {
        int userId = currentUserInstance.getUserId();
        return userId != 0 ? userId : -1; // Return -1 if user not found
    }

    private List<Integer> getAllEmployeeId() {
        try {
            List<Integer> allEmployeeIds = userService.getAllEmployeeIds();
            return allEmployeeIds;
        } catch (Exception e) {
            System.err.println("Unable to fetch employee ids.");
        }
        return null;
    }

    private int readIntegerInput() throws IOException {
        while (true) {
            try {
                String input = userInput.readLine().trim();
                int number = Integer.parseInt(input);
                return number;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter value in number format.");
            }
        }
    }

    private String readNotificationMessage() throws IOException {
        StringBuilder messageBuilder = new StringBuilder();
        String line;
        // Read message until a blank line is encountered or null is returned (end of stream)
        while ((line = userInput.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            messageBuilder.append(line).append("\n");
        }
        return messageBuilder.toString().trim();
    }
}
