package cafemanagement.client;

import java.io.*;
import java.net.*;

public class AuthClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server on " + SERVER_IP + ":" + SERVER_PORT);

            while (true) {
                displayMenu();
                System.out.print("Choose an option: ");
                String input = userInput.readLine().trim();

                switch (input) {
                    case "1":
                        login(writer, reader, userInput);
                        break;
                    case "2":
                        logout(writer, reader);
                        break;
                    case "3":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid input. Please enter a number.");
                }
            }
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("1. Login");
        System.out.println("2. Logout");
        System.out.println("3. Exit");
    }

    private static void login(PrintWriter writer, BufferedReader reader, BufferedReader userInput) throws IOException {
        System.out.print("Enter User ID: ");
        int userId = Integer.parseInt(userInput.readLine().trim());

        System.out.print("Enter Password: ");
        String password = userInput.readLine().trim();

        System.out.print("Enter Role: ");
        String roleName = userInput.readLine().trim();

        writer.println("LOGIN:" + userId + ":" + password + ":" + roleName);
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }

    private static void logout(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("LOGOUT");
        String response = reader.readLine();
        System.out.println("Server response: " + response);
    }
}
