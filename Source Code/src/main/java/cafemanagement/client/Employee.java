package cafemanagement.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;

public class Employee {
    private String userName;
    private Queue<String> notificationsQueue;
    private PrintWriter writer;
    private BufferedReader userInput;

    public Employee(String userName, Queue<String> notificationsQueue, PrintWriter writer, BufferedReader userInput) {
        this.userName = userName;
        this.notificationsQueue = notificationsQueue;
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
        if (notificationsQueue.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            System.out.println("Notifications:");
            while (!notificationsQueue.isEmpty()) {
                System.out.println(notificationsQueue.poll());
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
        System.out.println("Selecting meal for tomorrow...");
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
}
