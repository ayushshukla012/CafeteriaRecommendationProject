package cafemanagement.client;

import cafemanagement.model.User;
import cafemanagement.service.MenuItemService;
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
    private MenuItemService menuItemService;

    public AdminController(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
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
        System.out.println("Admin Menu:");
        System.out.println("1. Add menu item");
        System.out.println("2. Update menu item");
        System.out.println("3. Delete menu item");
        System.out.println("4. Check item availability");
        System.out.println("5. View full menu");
        System.out.println("6. Logout");
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
                checkItemAvailability();
                break;
            case "5":
                viewFullMenu();
                break;
            case "6":
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

            String itemName = "";
            while (true) {
                System.out.println("Enter item name:");
                itemName = userInput.readLine().trim().toLowerCase();
                if (itemName.isEmpty()) {
                    System.out.println("Item name cannot be empty. Please enter again.");
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

            writer.println("ADD_MENU_ITEM:" + categoryId + ":" + itemName + ":" + itemPrice + ":" + availability);
            boolean isValueStored =  menuItemService.storeMenuItem(itemName, categoryId, itemPrice, availability);
            if (isValueStored) {
                System.out.println("Menu item added successfully.");
            } else {
                System.out.println("Failed to add menu item. Please try again.");
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

public void updateMenuItem() {
    try {
        int categoryId = selectFoodCategory();
        if (categoryId == -1) {
            System.out.println("Invalid category selected.");
            return;
        }
        
        List<String> menuItems = menuItemService.getMenuItemsByCategory(categoryId);
        
        if (menuItems.isEmpty()) {
            System.out.println("No menu items found for the selected category.");
            return;
        }
        
        System.out.println("Menu items for category:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
        
        System.out.println("Select an item to update (enter the number):");
        int selectedIndex = Integer.parseInt(userInput.readLine().trim());
        
        if (selectedIndex < 1 || selectedIndex > menuItems.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        String selectedItem = menuItems.get(selectedIndex - 1);
        System.out.println("Selected item: " + selectedItem);
        
        System.out.println("What do you want to update?");
        System.out.println("1. Name of item");
        System.out.println("2. Price of item");
        System.out.println("3. Both");
        System.out.println("Select an option (1/2/3):");
        int updateOption = Integer.parseInt(userInput.readLine().trim());
        
        String itemName = "";
        float newItemPrice = 0;
        
        switch (updateOption) {
            case 1:
                System.out.println("Enter new item name:");
                itemName = userInput.readLine().trim();
                break;
            case 2:
                System.out.println("Enter new item price:");
                newItemPrice = Float.parseFloat(userInput.readLine().trim());
                break;
            case 3:
                System.out.println("Enter new item name:");
                itemName = userInput.readLine().trim();
                System.out.println("Enter new item price:");
                newItemPrice = Float.parseFloat(userInput.readLine().trim());
                break;
            default:
                System.out.println("Invalid option selected.");
                return;
        }
        
        boolean isUpdated = menuItemService.updateMenuInDatabase(selectedItem, itemName, newItemPrice);
        
        if (isUpdated) {
            System.out.println("Menu item updated successfully.");
        } else {
            System.out.println("Failed to update menu item. Please try again.");
        }
        
    } catch (IOException | NumberFormatException e) {
        System.err.println("Error reading input or converting input: " + e.getMessage());
    }
}

private void deleteMenuItem() {
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

private void deleteSingleMenuItem(int categoryId) throws IOException {
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

private void deleteMultipleMenuItems(int categoryId) throws IOException {
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

    private void checkItemAvailability() {
        System.out.println("Checking item availability...");
    }

    private void viewFullMenu() {
        System.out.println("viewFullMenu...");
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
        System.out.println("Name: " + item.getName());
        System.out.println("Price: " + item.getPrice());
        System.out.println("Availability: " + (item.isAvailability() ? "Yes" : "No"));
    }
    
}
