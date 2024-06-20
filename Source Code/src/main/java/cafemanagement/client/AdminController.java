package cafemanagement.client;

import cafemanagement.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminController {
    private User currentUser;
    private PrintWriter writer;
    private BufferedReader userInput;

    public AdminController(User currentUser, PrintWriter writer, BufferedReader userInput) {
        this.currentUser = currentUser;
        this.writer = writer;
        this.userInput = userInput;
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
                checkItemAvailability();
                break;
            case "5":
                logout();
                return;
            default:
                System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void addMenuItem() {
        try {
            System.out.println("Enter item name:");
            String itemName = userInput.readLine().trim();
            System.out.println("Enter item price:");
            String itemPrice = userInput.readLine().trim();
            // Send item details to server or process it here
            System.out.println("Menu item added: " + itemName + " - " + itemPrice);
        } catch (IOException e) {
            System.err.println("Error reading menu item: " + e.getMessage());
        }
    }

    private void updateMenuItem() {
        try {
            System.out.println("Enter item name:");
            String itemName = userInput.readLine().trim();
            System.out.println("Enter new item price:");
            String newItemPrice = userInput.readLine().trim();
            // Send item details to server or process it here
            System.out.println("Menu item updated: " + itemName + " - " + newItemPrice);
        } catch (IOException e) {
            System.err.println("Error reading menu item: " + e.getMessage());
        }
    }

    private void deleteMenuItem() {
        try {
            System.out.println("Enter item name to delete:");
            String itemName = userInput.readLine().trim();
            // Send item details to server or process it here
            System.out.println("Menu item deleted: " + itemName);
        } catch (IOException e) {
            System.err.println("Error reading menu item: " + e.getMessage());
        }
    }

    private void checkItemAvailability() {
        System.out.println("Checking item availability...");
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
}
