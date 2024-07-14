package com.cafeteriamanagement.client;

import java.io.*;
import java.net.Socket;
import java.net.ConnectException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cafeteriamanagement.dto.User;

public class AuthClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;
    private static User loggedInUser;
    private static String roleName;
    private static BufferedReader serverReader;
    private static PrintWriter writer;
    private static Queue<String> notificationsQueue = new ConcurrentLinkedQueue<>();

    public void execute() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            connectToServer(socket);
            handleClientOperations();
        } catch (ConnectException e) {
            System.err.println("Connection refused. Server may not be running or unreachable.");
        } catch (IOException e) {
            System.err.println("IO error occurred: " + e.getMessage());
        }
    }

    private void connectToServer(Socket socket) throws IOException {
        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to server on " + SERVER_IP + ":" + SERVER_PORT);
    }

    private void handleClientOperations() {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            displayRoleSelectionMenu();
            
            while (true) {
                System.out.print("\nChoose your role (1-3): ");
                input = userInput.readLine();

                if (input == null || input.trim().isEmpty()) {
                    System.out.println("Invalid input. Please enter a valid number (1-3).");
                    continue;
                }

                roleName = input.trim();
                if (!roleName.matches("[1-3]")) {
                    System.out.println("Invalid input. Please enter a valid number (1-3).");
                    continue;
                }

                if (login(userInput)) {
                    receiveNotifications();
                    handleRoleBasedOperations(userInput);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in client operations: " + e.getMessage());
        }
    }

    private static void displayRoleSelectionMenu() {
        System.out.println("\nSelect your role:");
        System.out.println("1. Admin");
        System.out.println("2. Chef");
        System.out.println("3. Employee");
    }

    private static boolean login(BufferedReader userInput) throws IOException {
        System.out.print("\nEnter User ID: ");
        String userIdInput = userInput.readLine();
        if (userIdInput == null || userIdInput.trim().isEmpty()) {
            System.out.println("User ID input was null or empty, terminating.");
            return false;
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdInput.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid User ID format. It should be a number.");
            return false;
        }

        System.out.print("Enter Password: ");
        String password = userInput.readLine();
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password input was null or empty, terminating.");
            return false;
        }
        password = password.trim();

        String roleNameString = "";
        switch (roleName) {
            case "1":
                roleNameString = "Admin";
                break;
            case "2":
                roleNameString = "Chef";
                break;
            case "3":
                roleNameString = "Employee";
                break;
        }

        writer.println("LOGIN:" + userId + ":" + password + ":" + roleNameString);
        writer.flush(); // Ensure data is sent immediately to the server

        String response = serverReader.readLine();

        if (response == null) {
            System.out.println("Server closed connection or sent null response.");
            return false;
        }

        if (response.startsWith("SUCCESS")) {
            System.out.println(response);
            String[] parts = response.split(" ");
            if (parts.length >= 5) {
                String[] nameParts = parts[4].split("!");
                String userName;
                if (nameParts.length >= 2) {
                    userName = nameParts[0] + " " + nameParts[1];
                } else {
                    userName = parts[4].replace("!", "");
                }
                loggedInUser = new User(userId, userName, roleName);
                System.out.println("Welcome " + userName + "!");
            } else {
                System.out.println("Error: Unexpected response format.");
                return false;
            }
            return true;
        } else {
            System.out.println(response);
            return false;
        }
    }

    private static void receiveNotifications() {
        Thread notificationThread = new Thread(() -> {
            try {
                while (true) {
                    String notification = serverReader.readLine();
                    if (notification == null) {
                        System.err.println("Server closed connection or sent null notification.");
                        break;
                    }
                    if (notification.startsWith("NOTIFICATION:")) {
                        notificationsQueue.offer(notification.substring(14));
                        System.out.println("New notification: " + notification.substring(14));
                    } else {
                        System.out.println(notification);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error receiving notifications: " + e.getMessage());
            }
        });
        notificationThread.start();
    }

    private static void handleRoleBasedOperations(BufferedReader userInput) throws IOException {
        // Implement based on the selected role (1-3)
        // For example:
        switch (roleName) {
            case "1":
                // new EmployeeController(loggedInUser, notificationsQueue, writer, userInput).start();
                break;
            case "2":
                // new ChefController(loggedInUser, writer, userInput).start();
                break;
            case "3":
                // new AdminController(loggedInUser, writer, userInput).start();
                break;
            default:
                System.out.println("Invalid role selected.");
                break;
        }
    }

    public static void main(String[] args) {
        AuthClient client = new AuthClient();
        client.execute();
    }
}
