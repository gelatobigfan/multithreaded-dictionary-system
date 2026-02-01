/**
 * [Author:Junyi Pan] [Student ID:1242599] 
 */

package server;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Color;

/**
 * GUI class for the Dictionary Server.
 */
public class ServerUI extends Thread {

	private JFrame frmDictionaryServerWindow;
	private JTextField textFieldUsers;
	private JTextArea textArea;
	private JTextField textFieldAddress;
	private JTextField textFieldPort;

	public ServerUI() {
		initialize();
	}
	
	@Override
	public void run() {
	    try {
	        // Show the GUI window
	        frmDictionaryServerWindow.setVisible(true);
	    } catch (Exception e) {
	        System.err.println("❌ UI failed: " + e.getMessage());
	    }
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionaryServerWindow = new JFrame();
		frmDictionaryServerWindow.getContentPane().setBackground(new Color(206, 234, 243));
		frmDictionaryServerWindow.setTitle("DictionaryServer");
		frmDictionaryServerWindow.setBounds(100, 100, 738, 528);
		frmDictionaryServerWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryServerWindow.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("📘 Dictionary Server");
		lblNewLabel.setBounds(267, 24, 165, 16);
		frmDictionaryServerWindow.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("👥 Current Users Online:");
		lblNewLabel_1.setBounds(27, 68, 155, 16);
		frmDictionaryServerWindow.getContentPane().add(lblNewLabel_1);
		
		textFieldUsers = new JTextField();
		textFieldUsers.setEditable(false);
		textFieldUsers.setBounds(194, 63, 68, 26);
		frmDictionaryServerWindow.getContentPane().add(textFieldUsers);
		textFieldUsers.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("📖 Current Dictionary:");
		lblNewLabel_2.setBounds(27, 113, 155, 16);
		frmDictionaryServerWindow.getContentPane().add(lblNewLabel_2);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(27, 141, 671, 340);
		frmDictionaryServerWindow.getContentPane().add(textArea);
		
		JButton btnRefresh = new JButton("🔄 Refresh");
		btnRefresh.addActionListener(e -> {
		    Server.updateDictDisplay();  //update UI
		});
		btnRefresh.setBounds(581, 63, 117, 29);
		frmDictionaryServerWindow.getContentPane().add(btnRefresh);
		
		JButton btnExport = new JButton("💾 Export");
		btnExport.addActionListener(e -> {
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Save Dictionary As");
		    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
		    fileChooser.setSelectedFile(new File("dictionary_export.csv"));

		    int userSelection = fileChooser.showSaveDialog(null);
		    if (userSelection == JFileChooser.APPROVE_OPTION) {
		        File exportFile = fileChooser.getSelectedFile();
		        
		        // file .csv
		        if (!exportFile.getName().toLowerCase().endsWith(".csv")) {
		            exportFile = new File(exportFile.getAbsolutePath() + ".csv");
		        }

		        try (BufferedWriter writer = new BufferedWriter(
		                new OutputStreamWriter(new FileOutputStream(exportFile), StandardCharsets.UTF_8))) {
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


		btnExport.setBounds(581, 108, 117, 29);
		frmDictionaryServerWindow.getContentPane().add(btnExport);
		
		JLabel lblNewLabel_4 = new JLabel("Address:");
		lblNewLabel_4.setBounds(280, 68, 61, 16);
		frmDictionaryServerWindow.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Port:");
		lblNewLabel_5.setBounds(280, 101, 61, 16);
		frmDictionaryServerWindow.getContentPane().add(lblNewLabel_5);
		
		textFieldAddress = new JTextField();
		textFieldAddress.setEditable(false);
		textFieldAddress.setBounds(355, 63, 198, 26);
		frmDictionaryServerWindow.getContentPane().add(textFieldAddress);
		textFieldAddress.setColumns(10);
		
		textFieldPort = new JTextField();
		textFieldPort.setEditable(false);
		textFieldPort.setBounds(353, 104, 200, 26);
		frmDictionaryServerWindow.getContentPane().add(textFieldPort);
		textFieldPort.setColumns(10);
	}
	
	public void updateAddress(String addr) {
	    EventQueue.invokeLater(() -> textFieldAddress.setText(addr));
	}

	public void updatePort(int p) {
	    EventQueue.invokeLater(() -> textFieldPort.setText(String.valueOf(p)));
	}

	// Update client count field safely in UI thread
	public void updateClientCount(int count) {
		EventQueue.invokeLater(() -> textFieldUsers.setText(String.valueOf(count)));
	}

	// Update dictionary display
	public void updateDictDisplay(String content) {
		EventQueue.invokeLater(() -> textArea.setText(content));
	}
}
