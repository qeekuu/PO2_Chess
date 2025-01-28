package network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ChessNetworkTest {
    private ExecutorService executorService;
    private ChessServer server;
    private Thread serverThread;

    @BeforeEach
    public void setUp() throws Exception {
        executorService = Executors.newFixedThreadPool(2);
        serverThread = new Thread(() -> {
            try {
                ChessServer.main(new String[]{});
            } catch (Exception e) {
                fail("Server failed to start");
            }
        });
        serverThread.start();
        Thread.sleep(5000); // start serwera
    }

    @AfterEach
    public void tearDown() throws Exception {
        serverThread.interrupt();
		serverThread.join(2000);
        executorService.shutdownNow();
    }

    @Test
    public void testClientConnection() throws Exception {
        try (Socket clientSocket = new Socket("127.0.0.1", 3000);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String welcomeMessage = in.readLine();
            assertTrue(welcomeMessage.startsWith("Welcome Player"), "Client should receive welcome message");

            String colorMessage = in.readLine();
            assertTrue(colorMessage.startsWith("COLOR"), "Client should receive color assignment");
        }
    }

    @Test
    public void testClientMoveHandling() throws Exception {
        Socket client1 = new Socket("127.0.0.1", 3000);
        BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
        PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);

        Socket client2 = new Socket("127.0.0.1", 3000);
        BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);

        in1.readLine(); in1.readLine();
        in2.readLine(); in2.readLine();

        out1.println("MOVE 1 2 3 4");
        String moveMessage = in2.readLine();

        assertEquals("MOVE 1 2 3 4", moveMessage, "Player 2 should receive the move");

        client1.close();
        client2.close();
    }

    @Test
    public void testMultipleClientsConnection() throws Exception {
        Socket client1 = new Socket("localhost", 3000);
        Socket client2 = new Socket("localhost", 3000);

        BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
        BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));

        assertNotNull(in1.readLine(), "Client 1 should receive a welcome message");
        assertNotNull(in2.readLine(), "Client 2 should receive a welcome message");

        client1.close();
        client2.close();
    }
}

