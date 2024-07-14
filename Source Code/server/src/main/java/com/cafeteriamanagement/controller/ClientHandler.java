package com.cafeteriamanagement.controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

import com.cafeteriamanagement.service.UserService;
import com.cafeteriamanagement.dao.UserDao;
import com.cafeteriamanagement.dto.User;
import com.cafeteriamanagement.util.ConfigLoader;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ConcurrentHashMap<String, Socket> connectedUsers;
    private String userAddress;
    private UserService userService;

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, Socket> connectedUsers) {
        this.clientSocket = clientSocket;
        this.connectedUsers = connectedUsers;
        this.userAddress = clientSocket.getInetAddress().getHostAddress();
        this.userService = createUserService();
    }

    @Override
    public void run() {
        try (BufferedReader clientRequest = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter serverResponse = new PrintWriter(clientSocket.getOutputStream(), true)) {

            handleLogin(clientRequest, serverResponse);

            while (true) {
                String clientInput = clientRequest.readLine();
                if (clientInput == null || clientInput.equalsIgnoreCase("LOGOUT")) {
                    System.out.println("Client " + userAddress + " logged out.");
                    break;
                }

                handleClientInput(clientInput, serverResponse);
            }

        }  catch (SocketException e) {
            System.err.println("Connection reset by client: " + userAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthServer.userDisconnected(userAddress);
        }
    }

    private void handleLogin(BufferedReader clientRequest, PrintWriter serverResponse) throws IOException {
        try {
            String loginRequest = clientRequest.readLine();
            if (loginRequest != null && loginRequest.startsWith("LOGIN:")) {
                String[] credentials = loginRequest.split(":");
                if (credentials.length == 4) {
                    int userId = Integer.parseInt(credentials[1]);
                    String password = credentials[2];
                    String role = credentials[3];
                    System.out.println("userid "+userId+" pass "+password+" role "+ role );

                    User user = userService.authenticate(userId, password, role);
                    if (user != null) {
                        serverResponse.println("SUCCESS Welcome " + user.getName() + "! Your role is: " + user.getRole());
                        connectedUsers.put(userAddress, clientSocket);
                    } else {
                        serverResponse.println("FAILURE Invalid user ID, password, or role.");
                    }
                } else {
                    serverResponse.println("FAILURE Invalid login request format.");
                }
            } else {
                serverResponse.println("FAILURE Invalid request.");
            }
        } catch (SocketException e) {
            System.err.println("Connection reset while handling login for: " + userAddress);
            throw e;
        } catch (IOException e) {
            System.err.println("IOException while handling login for: " + userAddress);
            throw e;
        }
    }

    private void handleClientInput(String clientInput, PrintWriter serverResponse) {
        // Implement further client operations here based on the input received
    }

    private UserService createUserService() {
        Properties config = ConfigLoader.loadConfig("config.properties");
        String dbUrl = config.getProperty("db.url");
        String dbUsername = config.getProperty("db.username");
        String dbPassword = config.getProperty("db.password");

        UserDao userDao = new UserDao(dbUrl, dbUsername, dbPassword);
        return new UserService(userDao);
    }

    
}
