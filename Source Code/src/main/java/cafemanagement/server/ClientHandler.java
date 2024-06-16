package cafemanagement.server;

import cafemanagement.authentication.AuthService;
import cafemanagement.model.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final AuthService authService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.authService = new AuthService();
    }

    @Override
    public void run() {
        try (InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)) {

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                // Parse and handle client request
                String[] parts = inputLine.split(":");
                String command = parts[0];

                switch (command) {
                    case "LOGIN":
                        int userId = Integer.parseInt(parts[1]);
                        String password = parts[2];
                        String roleName = parts[3];

                        String loginResult = handleLogin(userId, password, roleName);
                        writer.println(loginResult);
                        break;
                    case "LOGOUT":
                        String logoutResult = handleLogout();
                        writer.println(logoutResult);
                        break;
                    default:
                        writer.println("ERROR:Invalid command");
                        break;
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
        }
    }

    private String handleLogin(int userId, String password, String roleName) {
        User user = authService.login(userId, password, roleName);
        if (user != null) {
            return "SUCCESS:Login successful. Welcome " + user.getName() + "!";
        } else {
            return "ERROR:Invalid credentials or role. Please try again.";
        }
    }

    private String handleLogout() {
        String result = authService.logout();
        return "SUCCESS:" + result;
    }
}
