package cafemanagement.server;

import cafemanagement.exception.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthServer {
    private static final int PORT = 8080;
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            GlobalExceptionHandler.handle(new ServerException("Failed to start the server or accept client connection", e));
        }
    }
}
