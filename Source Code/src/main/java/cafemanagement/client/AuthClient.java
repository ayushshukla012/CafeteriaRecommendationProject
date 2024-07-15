package cafemanagement.client;

import cafemanagement.model.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AuthClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;
    private static User loggedInUser;
    private static String userName;
    private static String roleName;
    private static BufferedReader serverReader;
    private static Queue<String> notificationsQueue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nClient shutting down...");
        }));

        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server on " + SERVER_IP + ":" + SERVER_PORT);
            serverReader = reader;

            while (true) {
                displayRoleSelectionMenu();
                System.out.print("\nChoose your role: ");
                roleName = userInput.readLine().trim();

                if (!roleName.equals("1") && !roleName.equals("2") && !roleName.equals("3")) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    continue;
                }

                if (login(writer, userInput)) {
                    receiveNotifications();
                    switch (roleName) {
                        case "1":
                            new EmployeeController(loggedInUser, notificationsQueue, writer, userInput).start();
                            break;
                        case "2":
                            new ChefController(loggedInUser, writer, userInput).start();
                            break;
                        case "3":
                            new AdminController(loggedInUser, writer, userInput).start();
                            break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't connect to the server: " + e.getMessage());
        }
    }

    private static void displayRoleSelectionMenu() {
        System.out.println("Select your role:");
        System.out.println("1. Employee");
        System.out.println("2. Chef");
        System.out.println("3. Admin");
    }

    private static boolean login(PrintWriter writer, BufferedReader userInput) throws IOException {
        System.out.print("\nEnter User ID: ");
        int userId = Integer.parseInt(userInput.readLine().trim());

        System.out.print("Enter Password: ");
        String password = userInput.readLine().trim();

        String roleNameString = "";
        switch (roleName) {
            case "1":
                roleNameString = "Employee";
                break;
            case "2":
                roleNameString = "Chef";
                break;
            case "3":
                roleNameString = "Admin";
                break;
        }

        writer.println("LOGIN:" + userId + ":" + password + ":" + roleNameString);
        String response = serverReader.readLine();

        if (response.startsWith("SUCCESS")) {
            System.out.println(response);
            String[] parts = response.split(" ");
            if (parts.length >= 5) {
                String[] nameParts = parts[4].split("!");
                if (nameParts.length >= 2) {
                    userName = nameParts[0] + " " + nameParts[1];
                    loggedInUser = new User(userId, userName, roleNameString);
                } else {
                    userName = parts[4].replace("!", "");
                    loggedInUser = new User(userId, userName, roleNameString);
                }
            } else {
                System.out.println("Error: Unexpected response format.");
                return false;
            }
            System.out.println("Welcome " + userName + "!");
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
}
