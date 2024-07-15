package com.cafeteriamanagement.controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Properties;

import com.cafeteriamanagement.service.MenuService;
import com.cafeteriamanagement.service.UserService;
import com.cafeteriamanagement.service.impl.MenuServiceImpl;
import com.cafeteriamanagement.dao.UserDao;
import com.cafeteriamanagement.dao.impl.MenuDaoImpl;
import com.cafeteriamanagement.dto.Menu;
import com.cafeteriamanagement.dto.User;
import com.cafeteriamanagement.util.ConfigLoader;
import com.cafeteriamanagement.util.ConnectionPool;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ConcurrentHashMap<String, Socket> connectedUsers;
    private String userAddress;
    private UserService userService;
    private MenuService menuService;
    private ConnectionPool connectionPool;

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, Socket> connectedUsers) {
        this.clientSocket = clientSocket;
        this.connectedUsers = connectedUsers;
        this.userAddress = clientSocket.getInetAddress().getHostAddress();
        this.userService = createUserService();
        this.menuService = createMenuService(connectionPool);
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
        switch (clientInput) {
            case "VIEW_MENU":
                viewMenu(serverResponse);
                break;
            // Add cases for other client inputs as needed
            default:
                serverResponse.println("INVALID_REQUEST");
        }
    }

    private void viewMenu(PrintWriter serverResponse) {
        try {
            // Retrieve menu data from MenuService
            List<Menu> menus = menuService.getAllMenus();
            StringBuilder menuData = new StringBuilder();
            for (Menu menu : menus) {
                menuData.append(menu.getMenuId()).append(",")
                        .append(menu.getName()).append(",")
                        .append(menu.getCategoryId()).append(",")
                        .append(menu.getPrice()).append(",")
                        .append(menu.isAvailability()).append(",")
                        .append(menu.getSpiceLevel()).append(",")
                        .append(menu.getCuisineType()).append(",")
                        .append(menu.isSweet()).append(",")
                        .append(menu.getDietaryPreference()).append(":");
            }

            serverResponse.println("MENU_LIST:" + menus.size() + ":" + menuData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            serverResponse.println("ERROR_FETCHING_MENU");
        }
    }

private UserService createUserService() {
    Properties config = ConfigLoader.loadConfig("config.properties");
    String dbUrl = config.getProperty("db.url");
    String dbUsername = config.getProperty("db.username");
    String dbPassword = config.getProperty("db.password");
    int initialPoolSize = Integer.parseInt(config.getProperty("db.pool.size", "10"));

    ConnectionPool connectionPool = new ConnectionPool(dbUrl, dbUsername, dbPassword, initialPoolSize);
    UserDao userDao = new UserDao(connectionPool);
    return new UserService(userDao);
}

private MenuService createMenuService(ConnectionPool connectionPool) {
    MenuDaoImpl menuDao = new MenuDaoImpl(connectionPool);
    return new MenuServiceImpl(menuDao);
}

    
}
