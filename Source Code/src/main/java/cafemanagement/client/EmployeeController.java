package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.FeedbackService;
import cafemanagement.service.MenuItemService;
import cafemanagement.service.NotificationService;
import cafemanagement.service.PollService;
import cafemanagement.service.UserPreferencesService;
import cafemanagement.model.Notification;
import cafemanagement.model.PollItem;
import cafemanagement.dao.UserPreferencesDAO;
import cafemanagement.model.Feedback;
import cafemanagement.model.Menu;
import cafemanagement.model.UserPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class EmployeeController {
    private User currentUser;
    private Queue<String> notificationsQueue;
    private static Notification feedbackNotification;
    private PrintWriter writer;
    private BufferedReader userInput;
    protected NotificationService notificationService;
    protected PollService pollService;
    protected MenuItemService menuItemService;
    protected FeedbackService feedbackService;
    protected UserPreferencesService userPreferencesService;

    public EmployeeController(User currentUser, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.notificationsQueue = notificationsQueue;
        this.writer = writer;
        this.userInput = userInput;
        this.notificationService = new NotificationService();
        this.pollService = new PollService();
        this.menuItemService = new MenuItemService();
        this.feedbackService = new FeedbackService();
        this.userPreferencesService = new UserPreferencesService();
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

    protected void displayMenu() {
        System.out.println("Employee Menu:");
        System.out.println("1. See notifications");
        System.out.println("2. Provide feedback");
        System.out.println("3. Select meal for tomorrow");
        System.out.println("4. See menu");
        System.out.println("5. Update your profile");
        System.out.println("6. View chef feedback query");
        System.out.println("7. Logout");
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
                updateProfile();
                break;
            case "6":
                viewFeedbackQuery();
                break;
            case "7":
                logout();
                return;
            default:
                System.out.println("Invalid input. Please enter a number.");
        }
    }

    protected void showNotifications() {
        int employeeId = getUserIdByCurrentUser(currentUser);
    
        List<Notification> notifications = notificationService.getUnreadNotifications(employeeId);
    
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            System.out.println("Notifications:");
            for (Notification notification : notifications) {
                if ("FeedbackRequest".equals(notification.getNotificationType())) {
                    System.out.println(notification.getNotificationDate() + ": " + "Kindly go to 'Feedback query' option to respond to chefs query.");
                    feedbackNotification = notification;
                } else {
                    System.out.println(notification.getNotificationDate() + ": " + notification.getMessage());
                }
                notificationService.markNotificationAsRead(notification.getNotificationId(), employeeId);
            }
        }
    }

    protected void provideFeedback() {
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
            printFeedbackMenu(menuItems, "Selected Category");
    
            Menu selectedItem = null;
            while (selectedItem == null) {
                System.out.println("Select a menu item number:");
                try {
                    int menuItemNumber = Integer.parseInt(userInput.readLine().trim());                    
                    for (Menu menuItem : menuItems) {
                        if (menuItem.getMenuId() == menuItemNumber) {
                            selectedItem = menuItem;
                            break;
                        }
                    }
                    if (selectedItem == null) {
                        System.out.println("Invalid menu item number selected. Please enter a valid number.");
                    }
    
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numeric value.");
                }
            }
    
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
    
        feedback.setQuality(getValidInput("Enter quality (1-5):"));
        feedback.setValueForMoney(getValidInput("Enter value for money (1-5):"));
        feedback.setQuantity(getValidInput("Enter quantity (1-5):"));
        feedback.setTaste(getValidInput("Enter taste (1-5):"));
        feedback.setRating(getValidInput("Enter rating (1-5):"));
    
        System.out.println("Enter comments (optional):");
        feedback.setComment(userInput.readLine().trim());
    
        feedback.setFeedbackDate(new java.sql.Date(System.currentTimeMillis()));
        return feedback;
    }
    
    private int getValidInput(String prompt) throws IOException {
        int value;
        while (true) {
            System.out.println(prompt);
            try {
                value = Integer.parseInt(userInput.readLine().trim());
                if (value >= 1 && value <= 5) {
                    break;
                } else {
                    System.out.println("Please enter a value between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value between 1 and 5.");
            }
        }
        return value;
    }

    private void printFeedbackMenu(List<Menu> menuItems, String categoryName) {
        System.out.printf("| %-134s |%n", categoryName);
        System.out.println(
            "----------------------------------------------------------------------------------------------------------------------------------" +
            "--------------------------------------------------------------");
    
        menuItems.forEach(menuItem -> {
            System.out.printf("| %-5d | %-20s | %-10d | %-10.2f | %-10s | %-15s | %-10s | %-10s | %-15s |%n",
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
    

    protected void selectMealForTomorrow() {
        List<PollItem> pollItems = pollService.getPollItemsForToday();
    
        if (pollItems.isEmpty()) {
            System.out.println("No items available for voting today.");
            return;
        }
    
        int employeeId = getUserIdByCurrentUser(currentUser);
        UserPreferences userPreferences = userPreferencesService.getPreferencesByEmployeeId(employeeId);
        pollItems = sortPollItems(pollItems, userPreferences);
        
        int pollId = pollItems.get(0).getPollId();
        if (pollService.hasVotedToday(pollId, employeeId)) {
            System.out.println("Employee has already voted for today's poll.");
            return;
        }
    
        System.out.println("Select one meal for tomorrow:");
        List<Menu> sortedMenuItems = new ArrayList<>();
        for (int i = 0; i < pollItems.size(); i++) {
            PollItem item = pollItems.get(i);
            Menu menuItem = menuItemService.getMenuItemById(item.getMenuItemId());
            sortedMenuItems.add(menuItem);
            System.out.println((i + 1) + ". " + menuItem.getName() + " (Menu Item ID: " + item.getMenuItemId() + ")");
        }

        displaySuggestion(userPreferences, sortedMenuItems);
    
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

    protected void viewMenu() {
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
        if (feedback == null) {
            System.out.println("No feedback available.");
            return;
        }
        System.out.println("Quality: " + feedback.getQuality());
        System.out.println("Value for Money: " + feedback.getValueForMoney());
        System.out.println("Quantity: " + feedback.getQuantity());
        System.out.println("Taste: " + feedback.getTaste());
        System.out.println("Rating: " + feedback.getRating());
        if (feedback.getComment() != null) {
            System.out.println("Comments: " + feedback.getComment());
        } else {
            System.out.println("Comments: N/A");
        }
        
        System.out.println("Date: " + feedback.getFeedbackDate());
    }
    

    private void printCategory(List<Menu> menuItems, int categoryId, String categoryName) {
        System.out.printf("| %-134s |%n", categoryName);
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
    
        menuItems.stream()
                .filter(menuItem -> menuItem.getCategoryId() == categoryId)
                .forEach(menuItem -> {
                    System.out.printf("| %-5d | %-20s | %-10d | %-10.2f | %-10s | %-15s | %-10s | %-10s | %-15s |%n",
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

    protected List<PollItem> sortPollItems(List<PollItem> pollItems, UserPreferences userPreferences) {
    return pollItems.stream()
            .map(pollItem -> {
                Menu menuItem = menuItemService.getMenuItemById(pollItem.getMenuItemId());
                pollItem.setMenuItem(menuItem);
                return pollItem;
            })
            .sorted((item1, item2) -> {
                Menu menuItem1 = item1.getMenuItem();
                Menu menuItem2 = item2.getMenuItem();

                // Sort by dietary preference first
                int dietaryComparison = menuItem1.getDietaryPreference().compareTo(userPreferences.getDietaryPreference());
                if (dietaryComparison != 0) {
                    return dietaryComparison;
                }

                // Additional sorting criteria can be added here
                return menuItem1.getName().compareTo(menuItem2.getName());
            })
            .collect(Collectors.toList());
    }

    private void displaySuggestion(UserPreferences userPreferences, List<Menu> sortedMenuItems) {
        boolean suggestionMade = false;
    
        for (Menu menuItem : sortedMenuItems) {
            // Suggest based on spice level
            if (menuItem.getSpiceLevel().equals(userPreferences.getSpiceLevel())) {
                System.out.println("Because you like " + userPreferences.getSpiceLevel() + " spice level, you can go with " + menuItem.getName() + ".");
                suggestionMade = true;
                break;
            }
        }
    
        if (!suggestionMade) {
            for (Menu menuItem : sortedMenuItems) {
                // Suggest based on dietary preference
                if (menuItem.getDietaryPreference().equals(userPreferences.getDietaryPreference())) {
                    System.out.println("Because you prefer " + userPreferences.getDietaryPreference() + " food, you can go with " + menuItem.getName() + ".");
                    suggestionMade = true;
                    break;
                }
            }
        }
    
        if (!suggestionMade) {
            for (Menu menuItem : sortedMenuItems) {
                // Suggest based on preferred cuisine
                if (menuItem.getCuisineType().equals(userPreferences.getPreferredCuisine())) {
                    System.out.println("Because you like " + userPreferences.getPreferredCuisine() + " cuisine, you can go with " + menuItem.getName() + ".");
                    suggestionMade = true;
                    break;
                }
            }
        }
    
        if (!suggestionMade && userPreferences.isSweetTooth()) {
            for (Menu menuItem : sortedMenuItems) {
                // Suggest based on sweet tooth
                if (menuItem.isSweet()) {
                    System.out.println("Since you have a sweet tooth, you might like " + menuItem.getName() + ".");
                    suggestionMade = true;
                    break;
                }
            }
        }
    
        if (!suggestionMade) {
            // Default suggestion if no specific match found
            System.out.println("Based on your preferences, you can choose any of the available options.");
        }
    }

    protected void updateProfile() {
        try {
            System.out.println("Updating your Profile...");
            int employeeId = getUserIdByCurrentUser(currentUser);
            UserPreferences preferences = userPreferencesService.getPreferencesByEmployeeId(employeeId);
    
            boolean isNewProfile = false;
            if (preferences == null) {
                isNewProfile = true;
                preferences = new UserPreferences();
                preferences.setEmployeeId(currentUser.getUserId());
            }
    
            // Display current preferences if they exist
            if (!isNewProfile) {
                System.out.println("Your current preferences:");
                System.out.printf("Dietary Preference: %s%n", preferences.getDietaryPreference());
                System.out.printf("Spice Level: %s%n", preferences.getSpiceLevel());
                System.out.printf("Preferred Cuisine: %s%n", preferences.getPreferredCuisine());
                System.out.printf("Sweet Tooth: %s%n", preferences.isSweetTooth() ? "Yes" : "No");
            }
    
            // Update profile questions
            System.out.println("Please answer these questions to update your preferences:");
    
            // Question 1: Dietary Preference
            System.out.println("1) Please select one-");
            System.out.println("   1. Vegetarian");
            System.out.println("   2. Non Vegetarian");
            System.out.println("   3. Eggetarian");
            System.out.println("   4. Skip (if you do not want to update)");
            int option = readIntegerInputInRange(1, 4);
            switch (option) {
                case 1:
                    preferences.setDietaryPreference("Vegetarian");
                    break;
                case 2:
                    preferences.setDietaryPreference("Non Vegetarian");
                    break;
                case 3:
                    preferences.setDietaryPreference("Eggetarian");
                    break;
                case 4:
                    // Skip updating dietary preference
                    break;
                default:
                    System.out.println("Invalid input. Updating with default value (Vegetarian).");
                    preferences.setDietaryPreference("Vegetarian");
                    break;
            }
    
            // Question 2: Spice Level
            System.out.println("2) Please select your spice level");
            System.out.println("   1. High");
            System.out.println("   2. Medium");
            System.out.println("   3. Low");
            System.out.println("   4. Skip (if you do not want to update)");
            option = readIntegerInputInRange(1, 4);
            switch (option) {
                case 1:
                    preferences.setSpiceLevel("High");
                    break;
                case 2:
                    preferences.setSpiceLevel("Medium");
                    break;
                case 3:
                    preferences.setSpiceLevel("Low");
                    break;
                case 4:
                    // Skip updating spice level
                    break;
                default:
                    System.out.println("Invalid input. Updating with default value (Medium).");
                    preferences.setSpiceLevel("Medium");
                    break;
            }
    
            // Question 3: Preferred Cuisine
            System.out.println("3) What do you prefer most?");
            System.out.println("   1. North Indian");
            System.out.println("   2. South Indian");
            System.out.println("   3. Chinese");
            System.out.println("   4. Italian");
            System.out.println("   5. Mexican");
            System.out.println("   6. Other");
            System.out.println("   7. Skip (if you do not want to update)");
            option = readIntegerInputInRange(1, 7);
            switch (option) {
                case 1:
                    preferences.setPreferredCuisine("North Indian");
                    break;
                case 2:
                    preferences.setPreferredCuisine("South Indian");
                    break;
                case 3:
                    preferences.setPreferredCuisine("Chinese");
                    break;
                case 4:
                    preferences.setPreferredCuisine("Italian");
                    break;
                case 5:
                    preferences.setPreferredCuisine("Mexican");
                    break;
                case 6:
                    preferences.setPreferredCuisine("Other");
                    break;
                case 7:
                    // Skip updating preferred cuisine
                    break;
                default:
                    System.out.println("Invalid input. Updating with default value (Other).");
                    preferences.setPreferredCuisine("Other");
                    break;
            }
    
            // Question 4: Sweet Tooth
            System.out.println("4) Do you have a sweet tooth?");
            System.out.println("   1. Yes");
            System.out.println("   2. No");
            System.out.println("   3. Skip (if you do not want to update)");
            option = readIntegerInputInRange(1, 3);
            switch (option) {
                case 1:
                    preferences.setSweetTooth(true);
                    break;
                case 2:
                    preferences.setSweetTooth(false);
                    break;
                case 3:
                    // Skip updating sweet tooth preference
                    break;
                default:
                    System.out.println("Invalid input. Updating with default value (No sweet tooth).");
                    preferences.setSweetTooth(false);
                    break;
            }
    
            // Update or insert the preferences
            if (isNewProfile) {
                try {
                    userPreferencesService.insertUserPreferences(preferences);
                    System.out.println("New profile created successfully.");
                } catch (SQLException e) {
                    System.err.println("Error inserting user preferences: " + e.getMessage());
                }
            } else {
                userPreferencesService.updateUserPreferences(preferences);
                System.out.println("Profile updated successfully.");
            }
    
        } catch (IOException e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
    }
    
    private int readIntegerInputInRange(int min, int max) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        int input;
        while (true) {
            try {
                String inputStr = userInput.readLine().trim();
                if (inputStr.isEmpty()) {
                    System.out.println("Please provide a valid input.");
                    continue;
                }
                input = Integer.parseInt(inputStr);
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Input out of range. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return input;
    }    

    private void viewFeedbackQuery() {
        if (feedbackNotification == null) {
            System.out.println("No new feedback query.");
            return;
        }
        int employeeId = getUserIdByCurrentUser(currentUser);
        System.out.println("Feedback request for menu item: " + feedbackNotification.getMenuItemId());
        System.out.println("Message: " + feedbackNotification.getMessage());
        try {
            System.out.println("Do you want to answer the feedback questions? (yes/no)");
            String response = userInput.readLine().trim().toLowerCase();

            if ("yes".equals(response)) {
                System.out.println("Please provide your answers to the questions one by one.");
                List<String[]> feedbackList = new ArrayList<>();
                String[] questions = feedbackNotification.getMessage().split("\n");

                for (String question : questions) {
                    System.out.println(question);
                    String answer = userInput.readLine().trim();
                    feedbackList.add(new String[] { question, answer });
                }

                saveFeedbackResponses(feedbackNotification.getNotificationId(), employeeId, feedbackList);
                System.out.println("Thank you for your feedback!");
            } else {
                saveFeedbackResponses(feedbackNotification.getNotificationId(), employeeId,
                        Collections.singletonList(new String[] { "No question", "No answer provided." }));
                System.out.println("No feedback provided.");
            }
        } catch (IOException e) {
            System.err.println("Error handling feedback request: " + e.getMessage());
        }
    }

    private void saveFeedbackResponses(int notificationId, int employeeId, List<String[]> feedbackList) {
        for (String[] feedback : feedbackList) {
            feedbackService.saveFeedback(notificationId, employeeId, feedback[0], feedback[1]);
        }
    }

}
