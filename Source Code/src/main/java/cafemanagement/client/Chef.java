package cafemanagement.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Chef {
    private String userName;
    private PrintWriter writer;
    private BufferedReader userInput;

    public Chef(String userName, PrintWriter writer, BufferedReader userInput) {
        this.userName = userName;
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
        System.out.println("Chef Menu:");
        System.out.println("1. Send notifications");
        System.out.println("2. View user feedback");
        System.out.println("3. Generate Feedback Report");
        System.out.println("4. Send dishes to review");
        System.out.println("5. Logout");
    }

    public void handleInput(String input) {
        try {
            switch (input) {
                case "1":
                    sendNotification();
                    break;
                case "2":
                    viewUserFeedback();
                    break;
                case "3":
                    generateFeedbackReport();
                    break;
                case "4":
                    sendDishesToReview();
                    break;
                case "5":
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
            System.out.println("Enter your notification:");
            String notification = userInput.readLine().trim();
            // Send notification to server or process it here
            System.out.println("Notification sent.");
        } catch (IOException e) {
            System.err.println("Error reading notification: " + e.getMessage());
        }
    }

    private void viewUserFeedback() {
        System.out.println("Viewing user feedback...");
    }

    private void generateFeedbackReport() {
        System.out.println("Generating feedback report...");
    }

    private void sendDishesToReview() {
        System.out.println("Sending dishes to review...");
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
