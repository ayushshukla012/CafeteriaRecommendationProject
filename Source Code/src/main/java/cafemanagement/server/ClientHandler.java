package cafemanagement.server;

import cafemanagement.authentication.AuthService;
import cafemanagement.client.Admin;
import cafemanagement.client.Chef;
import cafemanagement.client.Employee;
import cafemanagement.model.User;
import cafemanagement.service.NotificationService;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final AuthService authService;
    private final ConcurrentHashMap<String, ClientHandler> clients;
    private PrintWriter writer;
    private String loggedInUser;
    private String loggedInRole;
    private final NotificationService notificationService;
    private final Queue<String> notificationsQueue = new ConcurrentLinkedQueue<>();

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.authService = new AuthService();
        this.notificationService = new NotificationService();
        this.clients = clients;
    }

    @Override
    public void run() {
        try (InputStream input = clientSocket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = clientSocket.getOutputStream()) {
            writer = new PrintWriter(output, true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                if (loggedInUser == null) {
                    handleLoginInput(inputLine);
                } else {
                    handleUserInput(inputLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }

            if (loggedInUser != null) {
                clients.remove(loggedInUser);
                sendNotificationToAll("User " + loggedInUser + " has logged out.");
            }
        }
    }

    private void handleLoginInput(String inputLine) {
        String[] parts = inputLine.split(":");
        if (parts.length == 4 && "LOGIN".equals(parts[0])) {
            try {
                int userId = Integer.parseInt(parts[1]);
                String password = parts[2];
                String roleName = parts[3];
                handleLogin(userId, password, roleName);
            } catch (NumberFormatException e) {
                writer.println("ERROR: Invalid user ID. Please enter a valid number.");
            }
        } else {
            writer.println("ERROR: Invalid login command.");
        }
    }

    private void handleUserInput(String inputLine) throws IOException {
        if ("LOGOUT".equalsIgnoreCase(inputLine)) {
            loggedInUser = null;
            writer.println("You have been logged out.");
        } else {
        String command = inputLine.split(":")[0];
        switch (loggedInRole) {
            case "Employee":
                Employee employee = new Employee(loggedInUser, notificationsQueue, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                employee.handleInput(command);
                break;
            case "Chef":
                Chef chef = new Chef(loggedInUser, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                chef.handleInput(command);
                break;
            case "Admin":
                Admin admin = new Admin(loggedInUser, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                admin.handleInput(command);
                break;
            default:
                writer.println("ERROR: Invalid role.");
                break;
        }
    }
    }
    

    private void handleLogin(int userId, String password, String roleName) {
        User user = authService.login(userId, password, roleName);
        if (user != null) {
            loggedInUser = user.getName();
            loggedInRole = roleName;
            clients.put(loggedInUser, this);
            sendNotificationToAll("User " + loggedInUser + " has logged in.");
            sendStoredNotifications(loggedInUser);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println("SUCCESS: Login successful.  Welcome " + user.getName() + "! " + currentDate);
            //displayMenuByRole(loggedInRole);
        } else {
            writer.println("ERROR: Invalid credentials or role. Please try again.");
        }
    }

    private void displayMenuByRole(String role) {
        writer.println("Enter your choice:");
        switch (role) {
            case "Employee":
                writer.println("1. See notifications");
                writer.println("2. Provide Feedback");
                writer.println("3. Select Meal for tomorrow");
                writer.println("4. Vote for Meal");
                writer.println("5. See Menu");
                writer.println("6. Logout");
                break;
            case "Chef":
                writer.println("1. Send notifications");
                writer.println("2. View user feedback");
                writer.println("3. Generate Feedback Report");
                writer.println("4. Send dishes to review");
                writer.println("5. Logout");
                break;
            case "Admin":
                writer.println("1. Add menu item");
                writer.println("2. Update menu item");
                writer.println("3. Delete menu item");
                writer.println("4. Check item availability");
                writer.println("5. Logout");
                break;
            default:
                writer.println("ERROR: Invalid role.");
                break;
        }
    }

    private void sendNotificationToAll(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            if (clientHandler != this) {
                clientHandler.writer.println("NOTIFICATION: " + message);
            }
        }
    }

    private void sendStoredNotifications(String username) {
        String notification;
        while ((notification = notificationsQueue.poll()) != null) {
            writer.println("NOTIFICATION: " + notification);
        }
    }
}
