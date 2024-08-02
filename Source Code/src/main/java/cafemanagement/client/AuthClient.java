package cafemanagement.client;

import cafemanagement.exception.*;
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
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 2000;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nClient shutting down...");
        }));

        int retryCount = 0;
        boolean connected = false;

        while (retryCount < MAX_RETRY_ATTEMPTS && !connected) {
            try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
            ) {
                System.out.println("Connected to server.");
                serverReader = reader;
                connected = true;

                while (true) {
                    System.out.println("\n=========== Welcome to Ayush's Cafe ===========");
                    displayRoleSelectionMenu();
                    System.out.print("\nChoose your role to proceed: ");
                    roleName = userInput.readLine().trim();

                    if (!roleName.equals("1") && !roleName.equals("2") && !roleName.equals("3")) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    try {
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
                    } catch (RetryException | FallbackException | ErrorReportingException e) {
                        GlobalExceptionHandler.handle(e);
                    }
                }
            } catch (UnknownHostException e) {
                GlobalExceptionHandler.handle(new ErrorReportingException("Server not found."));
                retryCount+=3;
            } catch (IOException e) {
                System.out.println("Retrying to connect");
                retryCount++;
                if (retryCount < MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Retry delay interrupted: " + ie.getMessage());
                    }
                } else {
                    GlobalExceptionHandler.handle(new ErrorReportingException("Unable to connect to the server."));
                }
            }
        }
    }

    private static void displayRoleSelectionMenu() {
        System.out.println("Select your role:");
        System.out.println("1. Employee");
        System.out.println("2. Chef");
        System.out.println("3. Admin");
    }

    private static boolean login(PrintWriter writer, BufferedReader userInput) throws IOException, RetryException, FallbackException, ErrorReportingException {
        System.out.print("\nEnter User ID: ");
        int userId = Integer.parseInt(userInput.readLine().trim());

        System.out.print("Enter Password: ");
        String password = userInput.readLine().trim();

        String roleNameString = getRoleNameString(roleName);

        writer.println("LOGIN:" + userId + ":" + password + ":" + roleNameString);
        String response = serverReader.readLine();

        if (response.startsWith("SUCCESS")) {
            handleLoginSuccess(response, userId, roleNameString);
            return true;
        } else {
            handleLoginFailure(response, userId);
            return false;
        }
    }

    private static String getRoleNameString(String roleName) throws FallbackException {
        switch (roleName) {
            case "1":
                return "Employee";
            case "2":
                return "Chef";
            case "3":
                return "Admin";
            default:
                throw new FallbackException("Invalid role selection.");
        }
    }

    private static void handleLoginSuccess(String response, int userId, String roleNameString) throws ErrorReportingException {
        System.out.println(response);
        String[] parts = response.split(" ");
        if (parts.length >= 5) {
            String[] nameParts = parts[4].split("!");
            if (nameParts.length >= 2) {
                userName = nameParts[0] + " " + nameParts[1];
            } else {
                userName = parts[4].replace("!", "");
            }
            loggedInUser = new User(userId, userName, roleNameString);
        } else {
            System.out.println("Error: Unexpected response format.");
            throw new ErrorReportingException("Unexpected response format during login.");
        }
    }

    private static void handleLoginFailure(String response, int userId) throws RetryException, ErrorReportingException {
        System.out.println(response);
        try {
            retryLogin(userId);
        } catch (RetryException e) {
            GlobalExceptionHandler.handle(e);
            throw new ErrorReportingException("Login failed after retries.");
        }
    }

    private static void retryLogin(int userId) throws RetryException {
        int retryCount = 3;
        while (retryCount > 0) {
            try {
                System.out.println("Retrying login...");
                // Implement retry logic here, e.g., prompt user again
                retryCount--;
            } catch (Exception e) {
                retryCount--;
                if (retryCount <= 0) {
                    throw new RetryException("Retry attempts exhausted.");
                }
            }
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
                GlobalExceptionHandler.handle(new ErrorReportingException("Error receiving notifications: " + e.getMessage()));
            }
        });
        notificationThread.start();
    }
}
