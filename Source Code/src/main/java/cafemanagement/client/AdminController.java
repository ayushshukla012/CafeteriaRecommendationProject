package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.MenuItemService;
import cafemanagement.service.NotificationService;
import cafemanagement.service.UserService;
import cafemanagement.model.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    private User currentUser;
    private PrintWriter writer;
    private BufferedReader userInput;
    protected MenuItemService menuItemService;
    protected UserService userService;
    protected NotificationService notificationService;

    public AdminController(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
        this.menuItemService = new MenuItemService();
        this.userService = new UserService();
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
        System.out.println();
        System.out.println("Admin Menu:");
        System.out.println("1. Add menu item");
        System.out.println("2. Update menu item");
        System.out.println("3. Delete menu item");
        System.out.println("4. View full menu");
        System.out.println("5. Logout");
    }

    public void handleInput(String input) {
        switch (input) {
            case "1":
                addMenuItem();
                break;
            case "2":
                updateMenuItem();
                break;
            case "3":
                deleteMenuItem();
                break;
            case "4":
                viewFullMenu();
                break;
            case "5":
                logout();
                return;
            default:
                System.out.println("Invalid input. Please enter a number.");
        }
    }

    public void addMenuItem() {
        try {
            int categoryId = selectFoodCategory();
            if (categoryId == -1) {
                System.out.println("Invalid category selected.");
                return;
            }

            List<String> existingMenuNames = menuItemService.getAllMenuNames();
            String itemName = "";
            while (true) {
                System.out.println("Enter item name:");
                itemName = userInput.readLine().trim().toLowerCase();
                if (itemName.isEmpty()) {
                    System.out.println("Item name cannot be empty. Please enter again.");
                } else if (existingMenuNames.contains(itemName)) {
                    System.out.println("Item name already exists. Please enter a different name.");
                } else {
                    break;
                }
            }

            float itemPrice = 0;
            while (true) {
                System.out.println("Enter item price:");
                try {
                    itemPrice = Float.parseFloat(userInput.readLine().trim());
                    if (itemPrice > 0) {
                        break;
                    } else {
                        System.out.println("Price must be greater than zero. Please enter again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Please enter again.");
                }
            }

            boolean availability = false;
            while (true) {
                System.out.println("Is the item available? (yes/no):");
                String availabilityInput = userInput.readLine().trim().toLowerCase();
                if ("yes".equals(availabilityInput)) {
                    availability = true;
                    break;
                } else if ("no".equals(availabilityInput)) {
                    availability = false;
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            String spiceLevel = "";
            while (true) {
                System.out.println("Enter spice level (High/Medium/Low):");
                spiceLevel = userInput.readLine().trim();
                if (isValidSpiceLevel(spiceLevel)) {
                    break;
                } else {
                    System.out.println("Invalid spice level. Please enter 'High', 'Medium', or 'Low'.");
                }
            }

            String cuisineType = "";
            while (true) {
                System.out.println("Enter cuisine type (North Indian/South Indian/Chinese/Italian/Mexican/Other):");
                cuisineType = userInput.readLine().trim();
                if (isValidCuisineType(cuisineType)) {
                    break;
                } else {
                    System.out.println("Invalid cuisine type. Please enter a valid option.");
                }
            }

            boolean isSweet = false;
            while (true) {
                System.out.println("Is the item sweet? (yes/no):");
                String isSweetInput = userInput.readLine().trim().toLowerCase();
                if ("yes".equals(isSweetInput)) {
                    isSweet = true;
                    break;
                } else if ("no".equals(isSweetInput)) {
                    isSweet = false;
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            String dietaryPreference = "";
            while (true) {
                System.out.println("Enter dietary preference (Vegetarian/Non Vegetarian/Eggetarian):");
                dietaryPreference = userInput.readLine().trim();
                if (isValidDietaryPreference(dietaryPreference)) {
                    break;
                } else {
                    System.out.println("Invalid dietary preference. Please enter a valid option.");
                }
            }

            writer.println("ADD_MENU_ITEM:" + categoryId + ":" + itemName + ":" + itemPrice + ":" + availability + ":" + spiceLevel + ":" + cuisineType + ":" + isSweet + ":" + dietaryPreference);
            boolean isValueStored = menuItemService.storeMenuItem(itemName, categoryId, itemPrice, availability, spiceLevel, cuisineType, isSweet, dietaryPreference);
            if (isValueStored) {
                System.out.println("Menu item added successfully.");
                int menuItemId = menuItemService.getMenuIdByName(itemName);
                if (menuItemId != -1) {
                    String notificationMessage = "A new item '" + itemName + "' has been added to the menu.";
                    sendNotification(notificationMessage, "NewFoodItem", menuItemId);
                } else {
                    System.out.println("Failed to fetch the menu item ID for notification.");
                }
            } else {
                System.out.println("Failed to add menu item. Please try again.");
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private boolean isValidSpiceLevel(String spiceLevel) {
        return "High".equalsIgnoreCase(spiceLevel) || "Medium".equalsIgnoreCase(spiceLevel) || "Low".equalsIgnoreCase(spiceLevel);
    }
    
    private boolean isValidCuisineType(String cuisineType) {
        return "North Indian".equalsIgnoreCase(cuisineType) || "South Indian".equalsIgnoreCase(cuisineType) || "Chinese".equalsIgnoreCase(cuisineType) || "Italian".equalsIgnoreCase(cuisineType) || "Mexican".equalsIgnoreCase(cuisineType) || "Other".equalsIgnoreCase(cuisineType);
    }
    
    private boolean isValidDietaryPreference(String dietaryPreference) {
        return "Vegetarian".equalsIgnoreCase(dietaryPreference) || "Non Vegetarian".equalsIgnoreCase(dietaryPreference) || "Eggetarian".equalsIgnoreCase(dietaryPreference);
    }

    public void updateMenuItem() {
        try {
            int categoryId = selectFoodCategory();
            if (categoryId == -1) {
                System.out.println("Invalid category selected.");
                return;
            }
            
            List<Menu> menuItems = menuItemService.getMenuItemsDetailsByCategory(categoryId);
            
            if (menuItems.isEmpty()) {
                System.out.println("No menu items found for the selected category.");
                return;
            }
            
            System.out.println("Menu items for category:");
            System.out.printf("%-10s %-20s %-10s %-10s %-10s %-15s %-15s %-10s %-20s%n", "Menu ID", "Name", "Category", "Price", "Availability", "Spice Level", "Cuisine Type", "Is Sweet", "Dietary Preference");
            for (Menu item : menuItems) {
                System.out.printf("%-10d %-20s %-10d %-10.2f %-10b %-15s %-15s %-10b %-20s%n", 
                    item.getMenuId(), item.getName(), item.getCategoryId(), item.getPrice(), item.isAvailability(), 
                    item.getSpiceLevel(), item.getCuisineType(), item.isSweet(), item.getDietaryPreference());
            }
            
            System.out.println("Enter the Menu ID of the item you want to update:");
            int selectedMenuId = Integer.parseInt(userInput.readLine().trim());
            
            Menu selectedItem = null;
            for (Menu item : menuItems) {
                if (item.getMenuId() == selectedMenuId) {
                    selectedItem = item;
                    break;
                }
            }
            
            if (selectedItem == null) {
                System.out.println("Invalid Menu ID selected.");
                return;
            }
            
            System.out.println("What do you want to update?");
            System.out.println("1. Name of item");
            System.out.println("2. Price of item");
            System.out.println("3. Availability");
            System.out.println("4. Spice Level");
            System.out.println("5. Cuisine Type");
            System.out.println("6. Is Sweet");
            System.out.println("7. Dietary Preference");
            System.out.println("8. All fields");
            System.out.println("9. Exit");
            System.out.println("Select an option (1-9):");
            int updateOption = Integer.parseInt(userInput.readLine().trim());
            
            if (updateOption == 9) {
                System.out.println("Exiting update menu.");
                return;
            }
            
            String newName = selectedItem.getName();
            float newPrice = selectedItem.getPrice();
            boolean newAvailability = selectedItem.isAvailability();
            String newSpiceLevel = selectedItem.getSpiceLevel();
            String newCuisineType = selectedItem.getCuisineType();
            boolean newIsSweet = selectedItem.isSweet();
            String newDietaryPreference = selectedItem.getDietaryPreference();
            
            String updatedField = "";
        switch (updateOption) {
            case 1:
                System.out.println("Enter new item name:");
                newName = userInput.readLine().trim();
                updatedField = "Name";
                break;
            case 2:
                System.out.println("Enter new item price:");
                newPrice = Float.parseFloat(userInput.readLine().trim());
                updatedField = "Price";
                break;
            case 3:
                System.out.println("Enter new availability (true/false):");
                newAvailability = Boolean.parseBoolean(userInput.readLine().trim());
                updatedField = "Availability";
                break;
            case 4:
                System.out.println("Enter new spice level:");
                newSpiceLevel = userInput.readLine().trim();
                updatedField = "Spice Level";
                break;
            case 5:
                System.out.println("Enter new cuisine type:");
                newCuisineType = userInput.readLine().trim();
                updatedField = "Cuisine Type";
                break;
            case 6:
                System.out.println("Enter new 'Is Sweet' value (true/false):");
                newIsSweet = Boolean.parseBoolean(userInput.readLine().trim());
                updatedField = "Is Sweet";
                break;
            case 7:
                System.out.println("Enter new dietary preference:");
                newDietaryPreference = userInput.readLine().trim();
                updatedField = "Dietary Preference";
                break;
            case 8:
                System.out.println("Enter new item name:");
                newName = userInput.readLine().trim();
                System.out.println("Enter new item price:");
                newPrice = Float.parseFloat(userInput.readLine().trim());
                System.out.println("Enter new availability (true/false):");
                newAvailability = Boolean.parseBoolean(userInput.readLine().trim());
                System.out.println("Enter new spice level:");
                newSpiceLevel = userInput.readLine().trim();
                System.out.println("Enter new cuisine type:");
                newCuisineType = userInput.readLine().trim();
                System.out.println("Enter new 'Is Sweet' value (true/false):");
                newIsSweet = Boolean.parseBoolean(userInput.readLine().trim());
                System.out.println("Enter new dietary preference:");
                newDietaryPreference = userInput.readLine().trim();
                updatedField = "All fields";
                break;
            default:
                System.out.println("Invalid option selected.");
                return;
        }
            
            boolean isUpdated = menuItemService.updateMenuInDatabase(selectedMenuId, newName, newPrice, newAvailability, newSpiceLevel, newCuisineType, newIsSweet, newDietaryPreference);
            
            if (isUpdated) {
                System.out.println("Menu item updated successfully.");
                String notificationMessage = "The item '" + selectedItem.getName() + "' has had its " + updatedField + " updated.";
                sendNotification(notificationMessage, "AvailabilityChange", selectedMenuId);
            } else {
                System.out.println("Failed to update menu item. Please try again.");
            }
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading input or converting input: " + e.getMessage());
        }
    }

protected void deleteMenuItem() {
    try {
        int categoryId = selectFoodCategory();
        if (categoryId == -1) {
            System.out.println("Invalid category selected.");
            return;
        }

        System.out.println("Do you want to delete one item or many items?");
        System.out.println("1. Delete one item");
        System.out.println("2. Delete many items");

        String deleteOption = userInput.readLine().trim();

        switch (deleteOption) {
            case "1":
                deleteSingleMenuItem(categoryId);
                break;
            case "2":
                deleteMultipleMenuItems(categoryId);
                break;
            default:
                System.out.println("Invalid option selected.");
                break;
        }

    } catch (IOException e) {
        System.err.println("Error reading input: " + e.getMessage());
    }
}

protected void deleteSingleMenuItem(int categoryId) throws IOException {
    try {
        List<Menu> menuItems = menuItemService.getMenuItemsDetailsByCategory(categoryId);

        if (menuItems.isEmpty()) {
            System.out.println("No items found in the selected category.");
            return;
        }

        System.out.println("Select the item number to delete:");
        displayMenuItems(menuItems);

        int itemToDelete = Integer.parseInt(userInput.readLine().trim());
        if (itemToDelete < 1 || itemToDelete > menuItems.size()) {
            System.out.println("Invalid item number selected.");
            return;
        }

        Menu item = menuItems.get(itemToDelete - 1);

        System.out.println("You selected:");
        displayMenuItemDetail(item);

        System.out.println("Confirm deletion? (yes/no)");
        String confirmation = userInput.readLine().trim().toLowerCase();
        if ("yes".equals(confirmation)) {
            boolean isDeleted = menuItemService.deleteMenuItem(item.getMenuId());
            if (isDeleted) {
                System.out.println("Menu item deleted successfully.");
            } else {
                System.out.println("Failed to delete menu item.");
            }
        } else {
            System.out.println("Deletion canceled.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid item number.");
    }
}

protected void deleteMultipleMenuItems(int categoryId) throws IOException {
    try {
        List<Menu> menuItems = menuItemService.getMenuItemsDetailsByCategory(categoryId);

        if (menuItems.isEmpty()) {
            System.out.println("No items found in the selected category.");
            return;
        }

        System.out.println("Select item numbers to delete (comma-separated):");
        displayMenuItems(menuItems);

        String itemsToDeleteInput = userInput.readLine().trim();
        String[] itemNumbers = itemsToDeleteInput.split(",");
        List<Integer> itemsToDelete = new ArrayList<>();

        for (String itemNumber : itemNumbers) {
            try {
                int itemIndex = Integer.parseInt(itemNumber.trim()) - 1;
                if (itemIndex >= 0 && itemIndex < menuItems.size()) {
                    itemsToDelete.add(menuItems.get(itemIndex).getMenuId());
                } else {
                    System.out.println("Invalid item number: " + itemNumber);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + itemNumber);
            }
        }

        if (itemsToDelete.isEmpty()) {
            System.out.println("No valid items selected for deletion.");
            return;
        }

        System.out.println("Items selected for deletion:");
        for (int itemId : itemsToDelete) {
            for (Menu item : menuItems) {
                if (item.getMenuId() == itemId) {
                    displayMenuItemDetail(item);
                    break;
                }
            }
        }

        System.out.println("Confirm deletion? (yes/no)");
        String confirmation = userInput.readLine().trim().toLowerCase();
        if ("yes".equals(confirmation)) {
            boolean areDeleted = menuItemService.deleteMultipleItems(itemsToDelete);
            if (areDeleted) {
                System.out.println("Selected menu items deleted successfully.");
            } else {
                System.out.println("Failed to delete menu items.");
            }
        } else {
            System.out.println("Deletion canceled.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter valid item numbers.");
    }
}

    protected void viewFullMenu() {
        System.out.println("Viewing Full Menu...");
        List<Menu> menuItems = menuItemService.getAllMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }

        System.out.println("Menu items:");
        System.out.println(
            "------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-15s | %-10s | %-15s | %-10s | %-20s |%n", 
                "ID", "Name", "Category", "Price", "Availability", "Spice Level", "Cuisine Type", "Sweet", "Dietary Preference");
        System.out.println(
            "------------------------------------------------------------------------------------------------------------------------------------------------");

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

    private void displayMenuItems(List<Menu> menuItems) {
        System.out.println("Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            Menu item = menuItems.get(i);
            System.out.println((i + 1) + ". " + item.getName());
        }
    }

    private void displayMenuItemDetail(Menu item) {
        System.out.println("Item Detail:");
        System.out.println("Menu ID: " + item.getMenuId());
        System.out.println("Name: " + item.getName());
        System.out.println("Category ID: " + item.getCategoryId());
        System.out.println("Price: " + item.getPrice());
        System.out.println("Availability: " + (item.isAvailability() ? "Yes" : "No"));
        System.out.println("Spice Level: " + item.getSpiceLevel());
        System.out.println("Cuisine Type: " + item.getCuisineType());
        System.out.println("Is Sweet: " + (item.isSweet() ? "Yes" : "No"));
        System.out.println("Dietary Preference: " + item.getDietaryPreference());
    }

    private void printCategory(List<Menu> menuItems, int categoryId, String categoryName) {
        System.out.printf("| %-134s |%n", categoryName);
        System.out.println(
            "------------------------------------------------------------------------------------------------------------------------------------------------");
    
        menuItems.stream()
            .filter(menuItem -> menuItem.getCategoryId() == categoryId)
            .forEach(menuItem -> {
                System.out.printf("| %-5d | %-20s | %-10s | %-10.2f | %-15s | %-10s | %-15s | %-10s | %-20s |%n",
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
            "------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private List<Integer> getAllEmployeeId() {
        try {
            List<Integer> allEmployeeIds = userService.getAllEmployeeIds();
            return allEmployeeIds;
        } catch (Exception e) {
            System.err.println("Unable to fetch employee ids: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private int getUserIdByCurrentUser(User currentUserInstance) {
        int userId = currentUserInstance.getUserId();
        return userId != 0 ? userId : -1;
    }
    
    private void sendNotification(String message, String notificationType, int menuItemId) {
        try {
            List<Integer> receiverIds = getAllEmployeeId();
            int senderId = getUserIdByCurrentUser(currentUser);
            notificationService.sendNotification(senderId, notificationType, menuItemId, message, receiverIds);
            System.out.println("Notification sent.");
        } catch (Exception e) {
            System.err.println("Error while sending notification: " + e.getMessage());
        }
    }
}
