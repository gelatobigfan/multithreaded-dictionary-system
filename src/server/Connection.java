/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package server;

import java.io.*;
import java.net.Socket;

/**
 * Handles a single client connection in its own thread.
 */
public class Connection extends Thread {
    private final Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Connection(Socket socket) {
        this.clientSocket = socket;
        initStreams();
    }

    // Initialize input/output streams
    private void initStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            System.err.println("❌ Failed to set up I/O streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String inputLine;
        try {
            while ((inputLine = reader.readLine()) != null) {
                String response = handleRequest(inputLine);
                writer.write(response + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("⚠️ Communication error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("⚠️ Could not close socket: " + e.getMessage());
            } finally {
                // update UI when client close the window
                Server.decrementClientCount();
            }
        }
    }

    // Process a single request and return a response
    private String handleRequest(String request) {
        String[] parts = request.split("#");
        if (parts.length < 2) return "Invalid request";

        String cmd = parts[0].trim().toUpperCase();
        String word = parts[1].trim();
        String meaning = parts.length > 2 ? parts[2].trim() : "";
        String existing = parts.length > 3 ? parts[3].trim() : "";

        String response;

        switch (cmd) {
            case "ADD":
                response = DictionaryLogic.addWord(word, meaning);
                Server.writeDict();
                Server.updateDictDisplay();  
                return response;

            case "QUERY":
                return DictionaryLogic.queryWord(word);  

            case "DELETE":
                response = DictionaryLogic.deleteWord(word);
                Server.writeDict();
                Server.updateDictDisplay();  
                return response;

            case "UPDATE":
                response = DictionaryLogic.updateWord(word, meaning, existing);
                Server.writeDict();
                Server.updateDictDisplay(); 
                return response;

            case "ADD_MEANING":
                response = DictionaryLogic.addMeaning(word, meaning);
                Server.writeDict();
                Server.updateDictDisplay();  
                return response;

            default:
                return "Unknown command";
        }
    }
}
