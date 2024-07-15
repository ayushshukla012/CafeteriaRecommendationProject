package cafemanagement.client;

import cafemanagement.model.Feedback;
import cafemanagement.model.Menu;
import cafemanagement.model.User;
import cafemanagement.service.NotificationService;
import cafemanagement.service.UserService;
import cafemanagement.service.PollService;
import cafemanagement.service.FeedbackService;
import cafemanagement.service.MenuItemService;
import cafemanagement.service.RecommendationService;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChefController {
    private User currentUser;
    private PrintWriter writer;
    private BufferedReader userInput;
    protected NotificationService notificationService;
    protected UserService userService;
    protected PollService pollService;
    protected MenuItemService menuItemService;
    protected RecommendationService recommendationService;
    protected FeedbackService feedbackService;

    public ChefController(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.userService = new UserService();
        this.pollService = new PollService();
        this.menuItemService = new MenuItemService();
        this.feedbackService = new FeedbackService();
        try {
            this.recommendationService = new RecommendationService();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("1. Send employee notifications");
        System.out.println("2. View menu");
        System.out.println("3. Generate feedback report");
        System.out.println("4. Select dishes for tomorrow");
        System.out.println("5. See user input to select for tomorrow.");
        System.out.println("6. Get recommendation");
        System.out.println("7. Show discarded menu");
        System.out.println("8. Logout");
    }

    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    sendEmployeeNotification(); // General
                    break;
                case "2":
                    viewMenu();
                    break;
                case "3":
                    generateFeedbackReport(); // Optional
                    break;
                case "4":
                    sendDishesToReview(); // Add recommendation engine
                    break;
                case "5":
                    userInputOnDishesToReview(); // Get sentiment for a feedback
                    break;
                case "6":
                    getRecommendation(); // Get sentiment for a feedback
                    break;
                case "7":
                    getDiscardedMenu();
                    break;
                case "8":
                    logout();
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number.");
            }
        } catch (IOException e) {
            System.err.println("Error handling input: " + e.getMessage());
        }
    }

    private void sendNotification(String message, String notificationType, int menuItemId) {
        try {
            List<Integer> receiverIds = getAllEmployeeId(); // Send to all employees
            int senderId = getUserIdByCurrentUser(currentUser); // Current User id
            notificationService.sendNotification(senderId, notificationType, menuItemId, message, receiverIds);
            System.out.println("Notification sent.");
        } catch (Exception e) {
            System.err.println("Error while sending notification: " + e);
        }
    }

    protected void sendEmployeeNotification() throws IOException {
        try {
            String notificationType = "Recommendation";

            System.out.println("Enter the item ID from Menu:");
            int menuItemId = readIntegerInput();

            System.out.println("Enter your message for Notification:");
            String message = readNotificationMessage();            

            sendNotification(message, notificationType, menuItemId);
            writer.println("Notification for " + notificationType + " sent.");
        } catch (IOException e) {
            writer.println("ChefId: " + getUserIdByCurrentUser(currentUser) + "Error reading notification.");
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    protected void viewMenu() {
        System.out.println("Viewing Menu With Feedback...");
        List<Menu> menuItems = menuItemService.getAllMenuItems();
    
        if (menuItems.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }
    
        System.out.println("Menu items:");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-15s | %-12s | %-15s | %-6s | %-18s |%n", 
                "ID", "Name", "Category", "Price", "Availability", "Spice Level", "Cuisine Type", "Sweet", "Dietary Preference");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
    
        printCategory(menuItems, 1, "Breakfast");
        printCategory(menuItems, 2, "Lunch");
        printCategory(menuItems, 3, "Dinner");
    }

    protected void generateFeedbackReport() {
        System.out.println("Generating feedback report...");

        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        List<Menu> menuItems = menuItemService.getAllMenuItems();

        Map<Integer, String> menuItemMap = menuItems.stream()
                .collect(Collectors.toMap(Menu::getMenuId, Menu::getName));

        Map<Integer, List<Feedback>> feedbackByMenuId = feedbackList.stream()
                .sorted((f1, f2) -> Integer.compare(f1.getMenuId(), f2.getMenuId()))
                .collect(Collectors.groupingBy(Feedback::getMenuId));

        String filePath = Paths.get(System.getProperty("user.home"), "Downloads", "Monthly Report.csv").toString();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("+----------+--------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n");
            writer.append("| Menu ID  | Item Name          | Feedback                                                                                                                                |\n");
            writer.append("+----------+--------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n");

            for (Map.Entry<Integer, List<Feedback>> entry : feedbackByMenuId.entrySet()) {
                int menuId = entry.getKey();
                List<Feedback> feedbacks = entry.getValue();
                String itemName = menuItemMap.getOrDefault(menuId, "Unknown");

                String feedbackDetails = feedbacks.stream()
                        .map(f -> String.format("[%s] Quality: %d, Value for Money: %d, Quantity: %d, Taste: %d, Rating: %d, Comment: %s",
                                f.getFeedbackDate(), f.getQuality(), f.getValueForMoney(), f.getQuantity(), f.getTaste(), f.getRating(), f.getComment()))
                        .collect(Collectors.joining(" | "));

                writer.append(String.format("| %-8d | %-18s | %-199s |\n", menuId, itemName, feedbackDetails));
                writer.append("+----------+--------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------+\n");
            }

            System.out.println("Feedback report generated successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing feedback report: " + e.getMessage());
        }
    }

    protected void sendDishesToReview() {
        try {
            System.out.println("Enter the category ID for the poll (1 for Breakfast, 2 for Lunch, 3 for Dinner):");
            int categoryId = readIntegerInput();
            System.out.println("categoryId: " + categoryId);

            List<Map<String, Object>> recommendedItems = recommendationService.recommendFood(categoryId);
            if (recommendedItems.isEmpty()) {
                System.out.println("No items found for the selected category.");
                return;
            }

            System.out.println("Menu items:");
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-12s | %-15s | %-10s | %-12s |%n",
                    "No.", "Name", "Menu ID", "Category ID", "Price", "Availability", "Avg Rating", "Sentiment");
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------");

            for (int i = 0; i < recommendedItems.size(); i++) {
                Map<String, Object> menuItem = recommendedItems.get(i);
                System.out.printf("| %-5d | %-20s | %-10d | %-10d | %-12.2f | %-15s | %-10.2f | %-12s |%n",
                        (i + 1), menuItem.get("name"), menuItem.get("menuId"), menuItem.get("categoryId"),
                        menuItem.get("price"), (Boolean) menuItem.get("availability") ? "Available" : "Not Available",
                        menuItem.get("averageRating"), menuItem.get("sentiment"));
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------");

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
                            if (itemIndex >= 0 && itemIndex < recommendedItems.size()) {
                                int menuId = (int) recommendedItems.get(itemIndex).get("menuId");
                                if (selectedItems.contains(menuId)) {
                                    hasDuplicate = true;
                                    break;
                                } else {
                                    selectedItems.add(menuId);
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
                    for (int menuId : selectedItems) {
                        recommendedItems.stream()
                                .filter(menuItem -> (int) menuItem.get("menuId") == menuId)
                                .forEach(menuItem -> System.out.printf(
                                        "%-20s (ID: %d, Category ID: %d, Price: %.2f, Availability: %s, Avg Rating: %.2f, Sentiment: %s)%n",
                                        menuItem.get("name"), menuItem.get("menuId"), menuItem.get("categoryId"),
                                        menuItem.get("price"),
                                        (Boolean) menuItem.get("availability") ? "Available" : "Not Available",
                                        menuItem.get("averageRating"), menuItem.get("sentiment")));
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

        } catch (InterruptedException | IOException e) {
            System.err.println("Error creating poll: " + e.getMessage());
        }
    }

    protected void getDiscardedMenu() {
        System.out.println("Fetching discarded menu items...");

        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        List<Menu> menuItems = menuItemService.getAllMenuItems();

        Map<Integer, List<Feedback>> feedbackByMenuId = feedbackList.stream()
                .collect(Collectors.groupingBy(Feedback::getMenuId));

        List<Integer> discardMenuIds = new ArrayList<>();

        for (Map.Entry<Integer, List<Feedback>> entry : feedbackByMenuId.entrySet()) {
            int menuId = entry.getKey();
            List<Feedback> feedbacks = entry.getValue();

            double averageRating = feedbacks.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);

            String sentiment = recommendationService.calculateSentiment(feedbacks);

            if (averageRating < 2 && sentiment.equals("very negative")) {
                discardMenuIds.add(menuId);
            }
        }

        if (discardMenuIds.isEmpty()) {
            System.out.println("No items to discard based on the criteria.");
            return;
        }

        List<Menu> discardedItems = menuItems.stream()
                .filter(menu -> discardMenuIds.contains(menu.getMenuId()))
                .collect(Collectors.toList());

        System.out.println("+----------+--------------------+---------+-------------+--------------+----------------+");
        System.out.println("| Menu ID  | Item Name          | Price   | Availability | Average Rating | Sentiment      |");
        System.out.println("+----------+--------------------+---------+-------------+--------------+----------------+");

        for (Menu item : discardedItems) {
            List<Feedback> itemFeedbacks = feedbackByMenuId.get(item.getMenuId());
            double averageRating = itemFeedbacks.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);

            String sentiment = recommendationService.calculateSentiment(itemFeedbacks);

            System.out.printf("| %-8d | %-18s | %-7.2f | %-11s | %-13.2f | %-14s |\n",
                    item.getMenuId(), item.getName(), item.getPrice(), item.isAvailability() ? "Yes" : "No",
                    averageRating, sentiment);
        }

        System.out.println("+----------+--------------------+---------+-------------+--------------+----------------+");
        taskForDiscardedMenu();
    }

    private void taskForDiscardedMenu() {
        boolean exit = false;
        while (!exit) {
            displayOptions();
            try {
                int choice = readIntegerInput();
                switch (choice) {
                    case 1:
                        removeFoodItemFromMenu();
                        break;
                    case 2:
                        getDetailedFeedback();
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Exiting Chef/Admin console.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                }
            } catch(IOException e) {
                System.err.println("Error in discarded menu: " + e);
            }
        }
    }

    private void displayOptions() {
        System.out.println("\nConsole Options:");
        System.out.println("1. Remove the Food Item from Menu List (Should be done once a month)");
        System.out.println("2. Get Detailed Feedback (Should be done once a month)");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");
    }

    private void removeFoodItemFromMenu() {
        try {
            System.out.print("Enter the food item ID to remove from the menu: ");
            int menuItemId = Integer.parseInt(userInput.readLine().trim());

            Menu menuItem = menuItemService.getMenuItemById(menuItemId);
            if (menuItem == null) {
                System.out.println("Food item not found in the menu.");
                return;
            }

            menuItemService.deleteMenuItem(menuItemId);
            System.out.println(menuItem.getName() + " has been removed from the menu.");
        }  catch(IOException e) {
            System.err.println("Error in remove food item from menu: " + e);
        }
    }

    private void getDetailedFeedback() {
        try {
            System.out.println("\nRolling out detailed feedback questions...");
            System.out.print("Enter the food item ID to gather feedback for: ");
            int menuItemId = Integer.parseInt(userInput.readLine().trim());
    
            Menu menuItem = menuItemService.getMenuItemById(menuItemId);
            if (menuItem == null) {
                System.out.println("Food item not found in the menu.");
                return;
            }
    
            String itemName = menuItem.getName();
    
            System.out.println("Here are some example questions you might ask:");
            System.out.println("1. What didn't you like about " + itemName + "?");
            System.out.println("2. How would you like " + itemName + " to taste?");
            System.out.println("3. Share your mom's recipe.");
    
            String[] questions = new String[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter question " + (i + 1) + " (or press Enter to skip): ");
                String question = userInput.readLine().trim();
                if (question.isEmpty()) {
                    break;
                }
                questions[i] = question;
            }
    
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("We are trying to improve your experience with ").append(itemName).append(". Please provide your feedback and help us.\n");
    
            for (int i = 0; i < questions.length; i++) {
                if (questions[i] != null) {
                    messageBuilder.append("Q").append(i + 1).append(". ").append(questions[i]).append("\n");
                }
            }
    
            String message = messageBuilder.toString();
            sendNotification(message, "FeedbackRequest", menuItemId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid menu item ID.");
        } catch (Exception e) {
            System.err.println("Error gathering detailed feedback: " + e);
        }
    }
    

    protected void logout() {
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
        return new ArrayList<>();
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
        while ((line = userInput.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            messageBuilder.append(line).append("\n");
        }
        return messageBuilder.toString().trim();
    }

    protected void getRecommendation() {
        System.out.println("Enter feedback to calculate the sentiment.");
        try {
            String input = userInput.readLine().trim();
            System.out.println("Calculating recommendation for you. Please wait!!!");
            String calculatedSentiment = recommendationService.calculateUserFeedbackSentiment(input);
            System.out.println("Sentiment: " + calculatedSentiment);
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }

    private void printCategory(List<Menu> menuItems, int categoryId, String categoryName) {
        System.out.printf("| %-134s |%n", categoryName);
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
    
        menuItems.stream()
                .filter(menuItem -> menuItem.getCategoryId() == categoryId)
                .forEach(menuItem -> {
                    System.out.printf("| %-5d | %-20s | %-10d | %-10.2f | %-15s | %-12s | %-15s | %-6s | %-18s |%n",
                            menuItem.getMenuId(),
                            menuItem.getName(),
                            menuItem.getCategoryId(),
                            menuItem.getPrice(),
                            menuItem.isAvailability() ? "Available" : "Not Available",
                            menuItem.getSpiceLevel(),
                            menuItem.getCuisineType(),
                            menuItem.isSweet() ? "Yes" : "No",
                            menuItem.getDietaryPreference());
                });
    
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
    }

    public void userInputOnDishesToReview() {
        Map<Integer, Integer> votesMap = pollService.getVotesForLatestPoll();
    
        if (votesMap.isEmpty()) {
            System.out.println("No votes found for the latest poll.");
            return;
        }
    
        List<Menu> menuItems = menuItemService.getAllMenuItems();
        Map<Integer, String> menuItemMap = menuItems.stream()
                .collect(Collectors.toMap(Menu::getMenuId, Menu::getName));
    
        System.out.println("User Input on Dishes to Review:");
        System.out.println("------------------------------------------------");
        System.out.printf("| %-20s | %-10s |%n", "Menu Item", "Vote Count");
        System.out.println("------------------------------------------------");
    
        for (Map.Entry<Integer, Integer> entry : votesMap.entrySet()) {
            String menuItemName = menuItemMap.getOrDefault(entry.getKey(), "Unknown");
            System.out.printf("| %-20s | %-10d |%n", menuItemName, entry.getValue());
        }
    
        System.out.println("------------------------------------------------");
    }
    


}
