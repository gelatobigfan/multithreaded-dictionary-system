/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Client thread handling TCP connection and message exchange.
 */
public class Client extends Thread {
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket client;
    public String address;
    public int port;
    
    // Thread-safe queue for sending requests to the server
    public LinkedBlockingQueue<String> requestQueue = new LinkedBlockingQueue<>(1);

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
        System.out.println("🛠️ Client initialized with " + address + ":" + port);
    }
    
    /**
     * Thread entry point. Establishes connection and starts request processing loop.
     */
    @Override
    public void run() {
        try {
            initializeConnection();
            processRequests();
        } catch (IOException | InterruptedException e) {
            System.out.println("⚠️ Client error: " + e.getMessage());
        }
    }
    public volatile boolean connected = false;
    // Continuously send queued requests to server
    private void initializeConnection() throws IOException {
        client = new Socket(address, port);
        reader = new BufferedReader(
                new InputStreamReader(client.getInputStream(), "UTF-8"));
        writer = new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
        System.out.println("✅ Connected to server at " + address + ":" + port);
        connected = true;
    }
    
    public void sendRequest(String request) {
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            System.err.println("❌ Failed to queue request: " + e.getMessage());
        }
    }

    private void processRequests() throws IOException, InterruptedException {
        while (true) {
            String request = requestQueue.take();
            if (request != null && !request.trim().isEmpty()) {
                writer.write(request + System.lineSeparator());
                writer.flush();
            }
        }
    }
    
    // Read single-line response from server
    public String getResp() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("getResp failed: " + e.getMessage());
            return null;
        }
    }
}
