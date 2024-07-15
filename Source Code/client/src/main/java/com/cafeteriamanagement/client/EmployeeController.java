package com.cafeteriamanagement.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;

import com.cafeteriamanagement.dto.User;

public class EmployeeController {

    private User loggedInUser;
    private Queue<String> notificationsQueue;
    private PrintWriter writer;
    private BufferedReader userInput;
 
    public EmployeeController(User loggedInUser, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.loggedInUser = loggedInUser;
        this.notificationsQueue = notificationsQueue;
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
        System.out.println("Employee Menu:");
        System.out.println("1. See notifications");
        System.out.println("2. Provide feedback");
        System.out.println("3. Select meal for tomorrow");
        System.out.println("4. See menu");
        System.out.println("5. Update your profile");
        System.out.println("6. Logout");
    }

    public void handleInput(String input) {
        switch (input) {
            case "1":
                // See notifications
                break;
            case "2":
                // Provide feedback
                break;
            case "3":
                // Select meal for tomorrow
                break;
            case "4":
                viewMenu();
                break;
            case "5":
                // Update your profile
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
