package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.FeedbackService;
import cafemanagement.service.MenuItemService;
import cafemanagement.service.NotificationService;
import cafemanagement.service.PollService;
import cafemanagement.model.Notification;
import cafemanagement.model.PollItem;
import cafemanagement.model.Feedback;
import cafemanagement.model.Menu;

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
    private MenuItemService menuItemService;
    private FeedbackService feedbackService;

    public EmployeeController(User currentUser, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.notificationsQueue = notificationsQueue;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.pollService = new PollService();
        this.menuItemService = new MenuItemService();
        this.feedbackService = new FeedbackService();
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
        System.out.println("4. See Menu");
        System.out.println("5. Logout");
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
                viewMenu();
                break;
            case "5":
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
        int employeeId = getUserIdByCurrentUser(currentUser);
        try {
            int categoryId = selectFoodCategory();
            if (categoryId == -1) {
                System.out.println("Invalid category selected.");
                return;
            }
    
            List<Menu> menuItems = menuItemService.getMenuItemsDetailsByCategory(categoryId);
            if (menuItems.isEmpty()) {
                System.out.println("No items found in the selected category.");
                return;
            }
    
            System.out.println("Menu items for category:");
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println((i + 1) + ". " + menuItems.get(i));
            }
    
            System.out.println("Select a menu item number:");
            int menuItemNumber = Integer.parseInt(userInput.readLine().trim());
            if (menuItemNumber < 1 || menuItemNumber > menuItems.size()) {
                System.out.println("Invalid menu item number selected.");
                return;
            }
    
            Menu selectedItem = menuItems.get(menuItemNumber - 1);
    
            Feedback existingFeedback = feedbackService.getFeedbackByEmployeeAndMenu(employeeId, selectedItem.getMenuId());
            if (existingFeedback != null) {
                System.out.println("Existing feedback found:");
                displayFeedback(existingFeedback);
                System.out.println("Do you want to update the feedback? (yes/no)");
                String updateResponse = userInput.readLine().trim().toLowerCase();
                if (!"yes".equals(updateResponse)) {
                    System.out.println("Feedback update canceled.");
                    return;
                }
            }
    
            Feedback newFeedback = collectFeedbackDetails(selectedItem.getMenuId(), employeeId);
            if (existingFeedback != null) {
                newFeedback.setFeedbackId(existingFeedback.getFeedbackId());
                feedbackService.updateFeedback(newFeedback);
                System.out.println("Feedback updated successfully.");
            } else {
                feedbackService.storeFeedback(newFeedback);
                System.out.println("Thank you for your feedback!");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private Feedback collectFeedbackDetails(int menuId, int employeeId) throws IOException {
        Feedback feedback = new Feedback();
        feedback.setMenuId(menuId);
        feedback.setEmployeeId(employeeId);
    
        System.out.println("Enter quality (1-5):");
        feedback.setQuality(Integer.parseInt(userInput.readLine().trim()));
    
        System.out.println("Enter value for money (1-5):");
        feedback.setValueForMoney(Integer.parseInt(userInput.readLine().trim()));
    
        System.out.println("Enter quantity (1-5):");
        feedback.setQuantity(Integer.parseInt(userInput.readLine().trim()));
    
        System.out.println("Enter taste (1-5):");
        feedback.setTaste(Integer.parseInt(userInput.readLine().trim()));
    
        System.out.println("Enter rating (1-5):");
        feedback.setRating(Integer.parseInt(userInput.readLine().trim()));
    
        System.out.println("Enter comments (optional):");
        feedback.setComment(userInput.readLine().trim());
    
        feedback.setFeedbackDate(new java.sql.Date(System.currentTimeMillis()));
        return feedback;
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

    private void viewMenu() {
        System.out.println("Viewing Menu With Feedback...");
        List<Menu> menuItems = menuItemService.getAllMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }

        System.out.println("Menu items:");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-15s |%n", "ID", "Name", "Category", "Price",
                "Availability");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");

        printCategory(menuItems, 1, "Breakfast");
        printCategory(menuItems, 2, "Lunch");
        printCategory(menuItems, 3, "Dinner");
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

        private int selectFoodCategory() throws IOException {
        System.out.println("Select food category (1 = Breakfast, 2 = Lunch, 3 = Dinner):");
        String categoryInput = userInput.readLine().trim();
        switch (categoryInput) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            default:
                return -1;
        }
    }

    private void displayFeedback(Feedback feedback) {
        System.out.println("Quality: " + feedback.getQuality());
        System.out.println("Value for Money: " + feedback.getValueForMoney());
        System.out.println("Quantity: " + feedback.getQuantity());
        System.out.println("Taste: " + feedback.getTaste());
        System.out.println("Rating: " + feedback.getRating());
        System.out.println("Comments: " + feedback.getComment());
        System.out.println("Date: " + feedback.getFeedbackDate());
    }

    private void printCategory(List<Menu> menuItems, int categoryId, String categoryName) {
        System.out.printf("| %-104s |%n", categoryName);
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");

        menuItems.stream()
                .filter(menuItem -> menuItem.getCategoryId() == categoryId)
                .forEach(menuItem -> {
                    System.out.printf("| %-5d | %-20s | %-10d | %-10.2f | %-15s |%n",
                            menuItem.getMenuId(),
                            menuItem.getName(),
                            menuItem.getCategoryId(),
                            menuItem.getPrice(),
                            menuItem.isAvailability() ? "Available" : "Not Available");
                });

        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
    }
}
