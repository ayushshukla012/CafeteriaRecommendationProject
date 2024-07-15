package cafemanagement.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.*;

import cafemanagement.server.AuthServer;

public class AuthServerTest {
    private AuthServer authServer;
    private Thread serverThread;

    @Before
    public void setUp() {
        authServer = new AuthServer();
        serverThread = new Thread(authServer::startServer);
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        serverThread.interrupt();
    }

    @Test
    public void testServerClientInteraction() {
        try (Socket clientSocket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("LOGIN:1:password:Employee");

            String response = in.readLine();
            assertNotNull("Server should respond to login attempt", response);
            assertTrue("Server response should indicate success or failure", 
                        response.startsWith("SUCCESS") || response.startsWith("ERROR"));

            System.out.println("Server response: " + response);
        } catch (IOException e) {
            fail("Exception during client-server interaction: " + e.getMessage());
        }
    }
}
