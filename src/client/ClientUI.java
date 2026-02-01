/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JFileChooser;
import server.DictionaryLogic;
import java.io.*;
import java.util.Map;

public class ClientUI {
	// UI components
	private JFrame frmDictionaryClientWindow;
	
	private JTextField textFieldInput;
	private JTextField textFieldMeaning;
	private JTextField textFieldExisting;
	public JTextArea textAreaOutput;
	
	public JButton btnAdd;
    public JButton btnDelete;
    public JButton btnQuery;
    public JButton btnUpdate;
    public JButton btnAddMeaning;
	
	public static Client client;
	private JButton btnExport;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    // Validate arguments
	    if (args.length != 2) {
	        System.err.println("❌ Usage: java -jar DictionaryClient.jar <server-address> <server-port>");
	        System.exit(1);
	    }

	    try {
	        String address = args[0];
	        int inputPort = Integer.parseInt(args[1]);
	        if (inputPort < 1024 || inputPort > 65535) {
	            System.err.println("❌ Invalid port: must be between 1024 and 65535");
	            System.exit(1);
	        }

	        // Initialize client with address and port
	        client = new client.Client(address, inputPort);

	    } catch (NumberFormatException e) {
	        System.err.println("❌ Port must be a number");
	        System.exit(1);
	    }

	    // Launch UI
	    EventQueue.invokeLater(() -> {
	        try {
	        	ClientUI window = new ClientUI();
	        	window.getFrame().setVisible(true);
	        } catch (Exception e) {
	            System.err.println("❌ Failed to launch UI");
	        }
	    });
	}

	/**
	 * Create the application.
	 */
	public ClientUI() {
		client.start();
		initialize();
	}
	
	public JFrame getFrame() {
        return frmDictionaryClientWindow;
    }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionaryClientWindow = new JFrame();
		frmDictionaryClientWindow.getContentPane().setBackground(new Color(237, 220, 224));
		frmDictionaryClientWindow.setTitle("DictionaryClient");
		frmDictionaryClientWindow.setBounds(500, 500, 762, 510);
		frmDictionaryClientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryClientWindow.getContentPane().setLayout(null);
		
		// Add button
		btnAdd = new JButton("➕ Add Word");
		btnAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String inputWord = textFieldInput.getText().trim();
		        String inputMeaning = textFieldMeaning.getText().trim();

		        if (inputWord.isEmpty() || inputMeaning.isEmpty()) {
		            textAreaOutput.setText("Please enter both word and meaning.");
		            return;
		        }

		        try {
		            String request = "ADD#" + inputWord + "#" + inputMeaning;
		            client.requestQueue.put(request); 
		            Thread.sleep(200); 

		            String resp = client.getResp(); 

		            textAreaOutput.setText(resp);

		            textFieldInput.setText("");
		            textFieldMeaning.setText("");
		            textFieldExisting.setText("");

		        } catch (Exception ex) {
		            textAreaOutput.setText("❌ Add failed: " + ex.getMessage());
		        }
		    }
		});





		btnAdd.setBounds(90, 281, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnAdd);
		
		JLabel lblNewLabel = new JLabel("📘Multi-Threaded Dictionary");
		lblNewLabel.setBounds(265, 6, 272, 55);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("📥 Word:");
		lblNewLabel_1.setBounds(95, 113, 61, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("📖 Meaning:");
		lblNewLabel_2.setBounds(95, 156, 83, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("🧾 Existing:");
		lblNewLabel_3.setBounds(95, 202, 83, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("🔘 Action Buttons:");
		lblNewLabel_4.setBounds(91, 244, 130, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_4);
		
		textFieldInput = new JTextField();
		textFieldInput.setBounds(235, 108, 130, 26);
		frmDictionaryClientWindow.getContentPane().add(textFieldInput);
		textFieldInput.setColumns(10);
		
		textFieldMeaning = new JTextField();
		textFieldMeaning.setBounds(235, 151, 130, 26);
		frmDictionaryClientWindow.getContentPane().add(textFieldMeaning);
		textFieldMeaning.setColumns(10);
		
		textFieldExisting = new JTextField();
		textFieldExisting.setBounds(235, 197, 130, 26);
		frmDictionaryClientWindow.getContentPane().add(textFieldExisting);
		textFieldExisting.setColumns(10);
		
		// DELETE button
		btnDelete = new JButton("❌ Delete Word");
		
		btnDelete.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String inputWord = textFieldInput.getText().trim();
		        if (inputWord.isEmpty()) {
		            textAreaOutput.setText("Please enter a word to delete.");
		            return;
		        }

		        try {
		            String request = "DELETE#" + inputWord;
		            client.requestQueue.put(request); 
		            Thread.sleep(200);

		            String resp = client.getResp();
		            textAreaOutput.setText(resp);

		            textFieldInput.setText("");
		            textFieldMeaning.setText("");
		            textFieldExisting.setText("");
		        } catch (Exception ex) {
		            textAreaOutput.setText("❌ Delete failed: " + ex.getMessage());
		        }
		    }
		});

		btnDelete.setBounds(90, 322, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnDelete);
		
		// Query button
		btnQuery = new JButton("🔍 Query Word");
		
		btnQuery.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String inputWord = textFieldInput.getText().trim();
		        if (inputWord.isEmpty()) {
		            textAreaOutput.setText("Please enter a word to query.");
		            return;
		        }

		        try {
		            String request = "QUERY#" + inputWord;
		            client.requestQueue.put(request);
		            Thread.sleep(200);  

		            String resp = client.getResp();
		            textAreaOutput.setText(resp);
		        } catch (Exception ex) {
		            textAreaOutput.setText("❌ Query failed: " + ex.getMessage());
		        }
		    }
		});

		
		btnQuery.setBounds(90, 363, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnQuery);
		
		// Update button
		btnUpdate = new JButton("🔁 Update");
		
		btnUpdate.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String inputWord = textFieldInput.getText().trim();
		        String oldMeaning = textFieldExisting.getText().trim();
		        String newMeaning = textFieldMeaning.getText().trim();

		        if (inputWord.isEmpty() || oldMeaning.isEmpty() || newMeaning.isEmpty()) {
		            textAreaOutput.setText("Please fill in Word, Existing Meaning, and New Meaning.");
		            return;
		        }

		        try {
		            String request = "UPDATE#" + inputWord + "#" + newMeaning + "#" + oldMeaning;
		            client.requestQueue.put(request);
		            Thread.sleep(200);
		            String resp = client.getResp();
		            textAreaOutput.setText(resp);

		            textFieldInput.setText("");
		            textFieldMeaning.setText("");
		            textFieldExisting.setText("");
		        } catch (Exception ex) {
		            textAreaOutput.setText("❌ Update failed: " + ex.getMessage());
		        }
		    }
		});

		btnUpdate.setBounds(90, 403, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnUpdate);
		
		// Add meaning button
		btnAddMeaning = new JButton("➕AddMeaning");
		
		btnAddMeaning.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String inputWord = textFieldInput.getText().trim();
		        String inputMeaning = textFieldMeaning.getText().trim();

		        if (inputWord.isEmpty() || inputMeaning.isEmpty()) {
		            textAreaOutput.setText("Please enter both word and new meaning.");
		            return;
		        }

		        try {
		            String request = "ADD_MEANING#" + inputWord + "#" + inputMeaning;
		            client.requestQueue.put(request);
		            Thread.sleep(200);

		            String resp = client.getResp();
		            textAreaOutput.setText(resp);
		            
		            textFieldInput.setText("");
		            textFieldMeaning.setText("");

		        } catch (Exception ex) {
		            textAreaOutput.setText("❌ Add meaning failed: " + ex.getMessage());
		        }
		    }
		});

		btnAddMeaning.setBounds(90, 447, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnAddMeaning);
		
		JLabel lblNewLabel_5 = new JLabel("📋 Output:");
		lblNewLabel_5.setBounds(379, 244, 83, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("keep blank if not adding");
		lblNewLabel_6.setBounds(377, 156, 166, 16);
		frmDictionaryClientWindow.getContentPane().add(lblNewLabel_6);
		
		textAreaOutput = new JTextArea();
		textAreaOutput.setText("(Result or status will be displayed here)");
		textAreaOutput.setEditable(false);
		textAreaOutput.setBounds(365, 274, 311, 168);
		textAreaOutput.setLineWrap(true);
		textAreaOutput.setWrapStyleWord(true);
		frmDictionaryClientWindow.getContentPane().add(textAreaOutput);
		
		btnExport = new JButton("💾 Export");
		btnExport.addActionListener(e -> {
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Save Dictionary As");
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
		    fileChooser.setSelectedFile(new File("client_dictionary_export.csv"));

		    int userSelection = fileChooser.showSaveDialog(null);
		    if (userSelection == JFileChooser.APPROVE_OPTION) {
		        File exportFile = fileChooser.getSelectedFile();

		        if (!exportFile.getName().toLowerCase().endsWith(".csv")) {
		            exportFile = new File(exportFile.getAbsolutePath() + ".csv");
		        }

		        try (BufferedWriter writer = new BufferedWriter(
		                new OutputStreamWriter(new FileOutputStream(exportFile), "UTF-8"))) {
		            for (Map.Entry<String, String> entry : DictionaryLogic.getAll().entrySet()) {
		                writer.write("\"" + entry.getKey() + "\",\"" + entry.getValue() + "\"");
		                writer.newLine();
		            }
		            System.out.println("✅ Exported to " + exportFile.getAbsolutePath());
		        } catch (IOException ex) {
		            System.err.println("❌ Export failed: " + ex.getMessage());
		        }
		    } else {
		        System.out.println("ℹ️ Export cancelled by user.");
		    }
		});

		btnExport.setBounds(505, 70, 131, 29);
		frmDictionaryClientWindow.getContentPane().add(btnExport);
	}
}
