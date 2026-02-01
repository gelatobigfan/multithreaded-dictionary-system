/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import javax.swing.SwingUtilities;

public class Server {
	
	private static ServerUI serverUI;
    private static Server server;

	
	static int port;
	static String dir;
	
	public static void main(String[] args) {
	    // Check for required arguments: port and dictionary file
	    if (args.length != 2) {
	        System.err.println("❌ Usage: java Server <port> <dictionary-file>");
	        return;
	    }

	    // Parse and validate port
	    try {
	        port = Integer.parseInt(args[0]);
	        if (port < 1024 || port > 65535) {
	            System.err.println("❌ Port must be between 1024 and 65535.");
	            return;
	        }
	    } catch (NumberFormatException e) {
	        System.err.println("❌ Invalid port number.");
	        return;
	    }

	    // Resolve file path and create if missing
	    dir = args[1];
	    File dictFile = new File(System.getProperty("user.dir"), dir);

	    if (!dictFile.exists()) {
	        try {
	            if (dictFile.createNewFile()) {
	                System.out.println("📄 Dictionary file created: " + dir);
	            }
	        } catch (IOException e) {
	            System.err.println("❌ Cannot create dictionary file: " + e.getMessage());
	            return;
	        }
	    }

	    // Launch GUI
	    serverUI = new ServerUI();
	    serverUI.start();
	    
	    serverUI.updatePort(port);
	    try {
	        String serverIP = java.net.InetAddress.getLocalHost().getHostAddress();
	        serverUI.updateAddress(serverIP);
	    } catch (Exception e) {
	        serverUI.updateAddress("Unavailable");
	    }

	    // Load existing dictionary entries
	    server = new Server();
	    server.readDict();

	    try {
	        Thread.sleep(100); // UI initialize
	    } catch (InterruptedException e) {
	    	System.err.println("⚠️ UI startup sleep interrupted: " + e.getMessage());
	    }

	    updateDictDisplay();
	    server.runServer();
	}

	
	private static final AtomicInteger userCntAtomic = new AtomicInteger(0);
	public void runServer() {
	    // Start server socket
	    try (ServerSocket serverSocket = new ServerSocket(port)) {
	        System.out.println("✅ Server started on port " + port);

	        while (true) {
	            try {
	                Socket clientSocket = serverSocket.accept();
	                userCntAtomic.incrementAndGet();

	                // Update UI with client count
	                serverUI.updateClientCount(userCntAtomic.get());


	                // Handle client in a new thread
	                Connection clientConnection = new Connection(clientSocket);
	                Thread clientThread = new Thread(clientConnection);
	                clientThread.start();
	            } catch (IOException e) {
	                System.err.println("⚠️ Connection failed: " + e.getMessage());
	            }
	        }

	    } catch (IOException e) {
	        System.err.println("❌ Cannot start server: " + e.getMessage());
	    }
	}
    
	// Load dictionary entries from file into memory
	public void readDict() {
		File dictFile = new File(System.getProperty("user.dir"), dir);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictFile), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", 2);
                if (parts.length == 2) {
                    DictionaryLogic.addWord(parts[0], parts[1]);
                }
            }
            System.out.println("✅ Dictionary loaded from file: " + dir);
        } catch (IOException e) {
            System.err.println("❌ Failed to read dictionary file: " + e.getMessage());
        }
    }
    
	// Write current dictionary contents to file
    public static void writeDict() {
    	File dictFile = new File(System.getProperty("user.dir"), dir);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dictFile), "UTF-8"))) {
            for (Map.Entry<String, String> entry : DictionaryLogic.getAll().entrySet()) {
                writer.write(entry.getKey() + "\t" + entry.getValue());
                writer.newLine();
            }
            System.out.println("✅ Dictionary written to file: " + dir);
        } catch (IOException e) {
            System.err.println("❌ Failed to write dictionary: " + e.getMessage());
        }
    }

    public static void updateDictDisplay() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : DictionaryLogic.getAll().entrySet()) {
            builder.append(entry.getKey()).append(" → ").append(entry.getValue()).append("\n");
        }

        // Fresh Swing
        SwingUtilities.invokeLater(() -> {
            serverUI.updateDictDisplay(builder.toString());
        });
    }
    
    public static void decrementClientCount() {
        int current = userCntAtomic.decrementAndGet();
        serverUI.updateClientCount(current);
    }

}
