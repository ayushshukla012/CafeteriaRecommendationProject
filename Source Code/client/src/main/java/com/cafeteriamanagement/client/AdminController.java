package com.cafeteriamanagement.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.cafeteriamanagement.dto.User;

public class AdminController {

    private User loggedInUser;
    private PrintWriter writer;
    private BufferedReader userInput;

    public AdminController(User loggedInUser, PrintWriter writer, BufferedReader userInput) {
        this.loggedInUser = loggedInUser;
        this.writer = writer;
        this.userInput = userInput;
    }

    public void start() throws IOException {
        String input;
        while (true) {
            displayMenu();
            try {
                System.out.print("\nEnter your choice: ");
                input = userInput.readLine();

                if (input == null || input.trim().isEmpty()) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    continue;
                }
                handleInput(input.trim());
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
                // Add menu item
                break;
            case "2":
                // Update menu item
                break;
            case "3":
                // Delete menu item
                break;
            case "4":
                // Check item availability
                break;
            case "5":
                viewMenu();
                break;
            case "6":
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice. Please enter a valid number.");
        }
    }

    private void viewMenu() {
        writer.println("VIEW_MENU");
        writer.flush();

        try {
            BufferedReader serverReader = AuthClient.getServerReader();
            String response = serverReader.readLine();

            if (response.startsWith("MENU_LIST")) {
                String[] menuData = response.split(":");
                int numMenus = Integer.parseInt(menuData[1]);

                for (int i = 0; i < numMenus; i++) {
                    String[] menuDetails = menuData[2 + i].split(",");
                    System.out.println("Menu ID: " + menuDetails[0]);
                    System.out.println("Name: " + menuDetails[1]);
                    // Display other menu details
                }
            } else {
                System.out.println("Failed to fetch menu data.");
            }
        } catch (IOException e) {
            System.err.println("Error fetching menu data: " + e.getMessage());
        }
    }
}
