package cafemanagement.server;

import cafemanagement.client.AdminController;
import cafemanagement.client.ChefController;
import cafemanagement.client.EmployeeController;
import cafemanagement.model.User;
import cafemanagement.service.UserService;
import cafemanagement.service.PollService;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    public final UserService userService;  // Changed to package-private
    public final PollService pollService;  // Changed to package-private
    private final ConcurrentHashMap<String, ClientHandler> clients;
    private PrintWriter writer;
    private User loggedInUser;
    private String loggedInRole;
    private final Queue<String> notificationsQueue = new ConcurrentLinkedQueue<>();

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
        this.pollService = new PollService();
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
        } catch (SocketException e) {
            if ("Connection reset".equals(e.getMessage())) {
                System.err.println("Client disconnected: " + e.getMessage());
            } else {
                System.err.println("Socket error: " + e.getMessage());
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
                clients.remove(loggedInUser.getName());
                System.out.println("User " + loggedInUser.getName() + " has logged out.");
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
            writer.println("command: " + command);
            switch (loggedInRole) {
                case "Employee":
                    EmployeeController employee = new EmployeeController(loggedInUser, notificationsQueue, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                    employee.handleInput(command);
                    break;
                case "Chef":
                    ChefController chef = new ChefController(loggedInUser, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                    chef.handleInput(command);
                    break;
                case "Admin":
                    AdminController admin = new AdminController(loggedInUser, writer, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                    admin.handleInput(command);
                    break;
                default:
                    writer.println("ERROR: Invalid role.");
                    break;
            }
        }
    }

    private void handleLogin(int userId, String password, String roleName) {
        User user = userService.authenticate(userId, password, roleName);
        if (user != null) {
            loggedInUser = user;
            loggedInRole = roleName;
            clients.put(loggedInUser.getName(), this);
            System.out.println("User " + loggedInUser.getName() + " has logged in.");
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println("SUCCESS: Login successful.  Welcome " + user.getName() + "! " + currentDate);
            userService.logUserAttempt(userId, "success");
        } else {
            writer.println("ERROR: Invalid credentials or role. Please try again.");
            userService.logUserAttempt(userId, "failure");
        }
    }
}
