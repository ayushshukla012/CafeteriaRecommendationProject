package com.cafeteriamanagement.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final ConcurrentHashMap<String, Socket> connectedUsers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                userConnected(clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket, connectedUsers);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    public static synchronized void userConnected(Socket socket) {
        String userAddress = socket.getInetAddress().getHostAddress();
        connectedUsers.put(userAddress, socket);
        System.out.println("User connected from " + userAddress);
        System.out.println("Total users connected: " + connectedUsers.size());
    }

    public static synchronized void userDisconnected(String userAddress) {
        connectedUsers.remove(userAddress);
        System.out.println("User disconnected from " + userAddress);
        System.out.println("Total users connected: " + connectedUsers.size());
    }
}
