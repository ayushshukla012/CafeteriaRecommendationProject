package cafemanagement.client;

import org.junit.Test;

import java.io.*;

public class AuthClientTest {

    @Test
    public void testClientServerInteraction() throws IOException {
        try (PipedOutputStream clientOutput = new PipedOutputStream();
             PipedInputStream serverInput = new PipedInputStream(clientOutput);
             BufferedReader clientReader = new BufferedReader(new InputStreamReader(new PipedInputStream(clientOutput)));
             PrintWriter serverWriter = new PrintWriter(new PipedOutputStream(serverInput), true);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverInput))) {

            serverWriter.println("LOGIN:1:password:Employee");

            String serverResponse = serverReader.readLine();
            System.out.println("Server response: " + serverResponse);
        }
    }
}
