// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// An intelligent chatbot that converses with a Spanish learner
// Language processing API: Apache OpenNPL - https://opennlp.apache.org/
// Spanish Parts of Speech ML Model - https://cavorite.com/labs/nlp/opennlp-models-es/
// Spanish

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class SpanishGui implements ActionListener {

	static boolean messageSent;
	private JLabel tu;
	private static JLabel pablo;
	private JFrame frame;
	static JTextArea value1;
	private static JTextArea value2;
	private static JButton send;
	protected static String userInput;
	protected static JLabel writeLabel;
	protected JPanel panel;
	private Color lavender = new Color(226, 216, 242);
	private Color light = new Color(235, 230, 235);
	private Color green = new Color(63, 110, 67);


	public SpanishGui() throws FileNotFoundException, IOException, InterruptedException {
		
		messageSent = false;
		
		// Main frame
		frame = new JFrame();
		frame.setTitle("Spanish Chat Interface");
		frame.setLayout(new BorderLayout(100, 100));
		
		// Main panel that will go on the frame
		panel = new JPanel(new GridLayout(8, 2));
		Dimension d = new Dimension(560, 900);
		frame.setPreferredSize(d);
		frame.setLocation(800, 0);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		panel.setBorder(new EmptyBorder(100, 100, 100, 100));
		panel.setBackground(lavender);

		pablo = new JLabel("Pablo", SwingConstants.CENTER);
		pablo.setOpaque(true);
		pablo.setBackground(light);
		pablo.setFont(new Font("Arial", Font.PLAIN, 30));

		value1 = new JTextArea();
		value1.setLineWrap(true);
		value1.setWrapStyleWord(true);

		userInput = value1.getText();
				
		writeLabel = new JLabel("");
		writeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		writeLabel.setForeground(green);
		
		JLabel empty = new JLabel(); // to add empty space in layout
		
		tu = new JLabel("Tú", SwingConstants.CENTER);
		tu.setOpaque(true);
		tu.setBackground(light);
		tu.setFont(new Font("Arial", Font.PLAIN, 30));

		value2 = new JTextArea();
		value2.setLineWrap(true);
		value2.setWrapStyleWord(true);
		
		send = new JButton("Send");
		send.addActionListener(this);
		
		send.setFont(new Font("Arial", Font.PLAIN, 30));

		// Adding labels to panel
		panel.setBackground(lavender);
		panel.add(tu);
		panel.add(value1);
		panel.add(empty);
		panel.add(writeLabel);
		panel.add(pablo);
		panel.add(value2);
		panel.add(empty);
		panel.add(send);
		frame.add(panel);
		
		// Adding the panel to the JFrame and making everything visible
		//frame.add(panel, BorderLayout.CENTER);
		SwingUtilities.updateComponentTreeUI(panel);
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * Sets user input variable to user's text when the send button is pressed.
	 * 
	 * @param e the ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == send) {
			userInput = value1.getText();
			messageSent = true;
		}

	}

	/**
	 * @return the messageSent
	 */
	public static boolean isMessageSent() {
		return messageSent;
	}

	/**
	 * @param messageSent the messageSent to set
	 */
	public static void setMessageSent(boolean messageSent) {
		messageSent = false;
	}


	/**
	 * Lets the user know when the bot is writing an answer.
	 * 
	 * @param inProgress the String describing the bot's status
	 */
	public static void updateStatus(String inProgress) {
		writeLabel.setText(inProgress);
	}

	/**
	 * Gets the user input from the GUI.
	 * 
	 * @return the the user input
	 */
	public static String getUserInput() {
		return userInput;
	}

	/**
	 * Updates the GUI with the bot's answer.
	 * 
	 * @param newText the text containing the bot's response
	 */
	public static void pabloAnswer(String newText) {
		value2.setText(newText);
	}

}