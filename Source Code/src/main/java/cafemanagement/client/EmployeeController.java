package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.NotificationService;
import cafemanagement.service.PollService;
import cafemanagement.model.Notification;
import cafemanagement.model.PollItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;

public class EmployeeController {
    private User currentUser;
    private Queue<String> notificationsQueue;
    private PrintWriter writer;
    private BufferedReader userInput;
    private NotificationService notificationService;
      private PollService pollService;

    public EmployeeController(User currentUser, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.notificationsQueue = notificationsQueue;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.pollService = new PollService();
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
        List<PollItem> pollItems = pollService.getPollItemsForToday();
    
        if (pollItems.isEmpty()) {
            System.out.println("No items available for voting today.");
            return;
        }
    
        int employeeId = getUserIdByCurrentUser(currentUser);
        int pollId = pollItems.get(0).getPollId();
    
        if (pollService.hasVotedToday(pollId, employeeId)) {
            System.out.println("Employee has already voted for today's poll.");
            return;
        }
    
        System.out.println("Select one meal for tomorrow:");
        for (int i = 0; i < pollItems.size(); i++) {
            PollItem item = pollItems.get(i);
            System.out.println((i + 1) + ". " + item.getItemName() + " (Menu Item ID: " + item.getMenuItemId() + ")");
        }
    
        try {
            int selectedIndex = readIntegerInput();
            if (selectedIndex >= 1 && selectedIndex <= pollItems.size()) {
                PollItem selectedPollItem = pollItems.get(selectedIndex - 1);
                System.out.println("Selected Poll Item ID: " + selectedPollItem.getPollItemId() + ", Menu Item ID: " + selectedPollItem.getMenuItemId());
                voteForSelectedMeal(selectedPollItem, employeeId);
            } else {
                System.out.println("Invalid selection. Please select a valid number.");
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
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

    private void voteForSelectedMeal(PollItem selectedPollItem, int employeeId) {
        int pollId = selectedPollItem.getPollId();
        int menuItemId = selectedPollItem.getMenuItemId();
        System.out.println("Voting for menu item ID: " + menuItemId);
        pollService.castVote(pollId, menuItemId, employeeId);
        System.out.println("Vote for " + selectedPollItem.getItemName() + " recorded.");
    }
}
