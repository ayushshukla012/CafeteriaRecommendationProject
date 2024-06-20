package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.NotificationService;
import cafemanagement.service.UserService;
import cafemanagement.service.PollService;
import cafemanagement.service.MenuItemService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChefController {
    private User currentUser;
    private PrintWriter writer;
    private BufferedReader userInput;
    private NotificationService notificationService;
    private UserService userService;
    private PollService pollService;
    private MenuItemService menuItemService;
    

    public ChefController(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.userService = new UserService();
        this.pollService = new PollService();
        this.menuItemService = new MenuItemService();
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
        System.out.println("3. Generate feedback report");
        System.out.println("4. Send dishes to review");
        System.out.println("5. Ask user for feedback");
        System.out.println("6. Logout");
    }

    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    sendNotification();
                    break;
                case "2":
                    viewMenuWithFeedback();
                    break;
                case "3":
                    generateFeedbackReport();
                    break;
                case "4":
                    sendDishesToReview(); //Add recommendation engine
                    break;
                case "5":
                    askForFeedback();
                    break;
                case "6":
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

    private void viewMenuWithFeedback() {
        System.out.println("Viewing Menu With Feedback...");
    }

    private void generateFeedbackReport() {
        System.out.println("Generating feedback report...");
    }

    private void askForFeedback() {
        System.out.println("ask user For Feedback");
    }

    private void sendDishesToReview() {
        try {
            System.out.println("Enter the category ID for the poll (1 for Breakfast, 2 for Lunch, 3 for Dinner):");
            int categoryId = readIntegerInput();
            System.out.println("categoryId: " + categoryId);
    
            List<String> menuItems = menuItemService.getMenuItemsByCategory(categoryId);
            if (menuItems.isEmpty()) {
                System.out.println("No items found for the selected category.");
                return;
            }
    
            System.out.println("Menu items:");
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println((i + 1) + ". " + menuItems.get(i));
            }
    
            List<Integer> selectedItems = new ArrayList<>();
            while (true) {
                selectedItems.clear();
                System.out.println("Select 5 items by entering their numbers (comma-separated):");
                String[] selectedIndices = userInput.readLine().trim().split(",");
    
                if (selectedIndices.length >= 3 && selectedIndices.length <= 5) {
                    boolean hasDuplicate = false;
                    try {
                        for (String index : selectedIndices) {
                            int itemIndex = Integer.parseInt(index.trim()) - 1;
                            if (itemIndex >= 0 && itemIndex < menuItems.size()) {
                                if (selectedItems.contains(itemIndex + 1)) {
                                    hasDuplicate = true;
                                    break;
                                } else {
                                    selectedItems.add(itemIndex + 1);
                                }
                            } else {
                                throw new NumberFormatException("Index out of range");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid input. Please enter valid numbers.");
                        continue;
                    }
    
                    if (hasDuplicate) {
                        System.out.println("Duplicate items selected. Please select unique items.");
                        continue;
                    }
    
                    System.out.println("Selected items:");
                    for (int index : selectedItems) {
                        System.out.println(menuItems.get(index - 1));
                    }
    
                    System.out.println("Do you confirm these items? (yes/no):");
                    String confirmation = userInput.readLine().trim().toLowerCase();
                    if (confirmation.equals("yes") || confirmation.equals("y")) {
                        break;
                    } else {
                        System.out.println("Please select items again.");
                    }
                } else {
                    System.out.println("Please select between 3 and 5 items.");
                }
            }
    
            int pollId = pollService.createPoll(currentUser.getUserId(), new java.sql.Date(System.currentTimeMillis()));
            pollService.addItemsToPoll(pollId, selectedItems);
            System.out.println("Poll created successfully with ID: " + pollId);
    
        } catch (IOException e) {
            System.err.println("Error creating poll: " + e.getMessage());
        }
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
