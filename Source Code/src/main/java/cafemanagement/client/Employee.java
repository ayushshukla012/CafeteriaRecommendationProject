package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.NotificationService;
import cafemanagement.model.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;

public class Employee {
    private User currentUser;
    private Queue<String> notificationsQueue;
    private PrintWriter writer;
    private BufferedReader userInput;
    private NotificationService notificationService;

    public Employee(User currentUser, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.notificationsQueue = notificationsQueue;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
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
        System.out.println("Employee Menu:");
        System.out.println("1. See notifications");
        System.out.println("2. Provide Feedback");
        System.out.println("3. Select Meal for tomorrow");
        System.out.println("4. Vote for Meal");
        System.out.println("5. See Menu");
        System.out.println("6. Logout");
    }

    public void handleInput(String input) {
        switch (input) {
            case "1":
                showNotifications();
                break;
            case "2":
                provideFeedback();
                break;
            case "3":
                selectMealForTomorrow();
                break;
            case "4":
                voteForMeal();
                break;
            case "5":
                seeMenu();
                break;
            case "6":
                logout();
                return;
            default:
                System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void showNotifications() {
        int employeeId = getUserIdByCurrentUser(currentUser);

        List<Notification> notifications = notificationService.getUnreadNotifications(employeeId);

        if (notifications.isEmpty()) {
           System.out.println("No notifications.");
        } else {
            System.out.println("Notifications:");
            for (Notification notification : notifications) {
                System.out.println(notification.getNotificationDate() + ": " + notification.getMessage());
                notificationService.markNotificationAsRead(notification.getNotificationId(), employeeId);
            }
        }
    }

    private void provideFeedback() {
        try {
            System.out.println("Enter your feedback:");
            String feedback = userInput.readLine().trim();
            // Send feedback to server or process it here
            System.out.println("Thank you for your feedback!");
        } catch (IOException e) {
            System.err.println("Error reading feedback: " + e.getMessage());
        }
    }

    private void selectMealForTomorrow() {
        System.out.println("Selecting meal for tomorrow...");
    }

    private void voteForMeal() {
        System.out.println("Voting for meal...");
    }

    private void seeMenu() {
        System.out.println("Seeing menu...");
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
}
