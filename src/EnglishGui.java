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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class EnglishGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	static boolean messageSent;
	private JLabel you;
	private static JLabel dave;
	private static JTextArea value1;
	private static JTextArea value2;
	private static JButton send;
	protected static String userInput;
	protected static JLabel writeLabel;
	protected JPanel panel;
	private Color lavender = new Color(226, 216, 242);
	private Color light = new Color(235, 230, 235);
	private Color green = new Color(63, 110, 67);

	/**
	 * A constructor for the English GUI created in EnglishRunner.
	 */
	public EnglishGui() {
		setTitle("English Chat Interface");
		setLayout(new BorderLayout(100, 100));
		getRootPane().setBorder(new EmptyBorder(100, 100, 100, 100));
		getRootPane().setBackground(lavender);
		
		panel = new JPanel(new GridLayout(8, 2));
		Dimension d = new Dimension(560, 900);
		setPreferredSize(d);
		setLocation(800, 0);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		dave = new JLabel("Dave", SwingConstants.CENTER);
		dave.setOpaque(true);
		dave.setBackground(light);
		dave.setFont(new Font("Arial", Font.PLAIN, 30));

		value1 = new JTextArea();
		
		writeLabel = new JLabel("");
		writeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		writeLabel.setForeground(green);

		JLabel empty = new JLabel(); // to add empty space in layout
		
		you = new JLabel("You", SwingConstants.CENTER);
		you.setFont(new Font("Arial", Font.PLAIN, 30));
		you.setBackground(light);
		you.setOpaque(true);

		value2 = new JTextArea();
				
		send = new JButton("Send");
		send.addActionListener(this);
		
		send.setFont(new Font("Arial", Font.PLAIN, 30));

		// Adding labels to panel
		panel.add(you);
		panel.add(value1);
		panel.add(empty);
		panel.add(writeLabel);
		panel.add(dave);
		panel.add(empty);
		panel.add(value2);
		panel.add(empty);
		panel.add(send);
		panel.setBackground(lavender);
		
		add(panel, BorderLayout.CENTER);
		pack();
		add(panel);
		panel.setVisible(true);
		setVisible(true);

		messageSent = false;
		userInput = value1.getText();

	}

	/**
	 * Sets the user input to the text in the text field when the "send" button is pressed.
	 * 
	 * @param e the ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == send) {
			userInput = value1.getText();
			System.out.println(userInput);
			messageSent = true;
		}

	}

	/**
	 * Updates whether the user has sent a message to the bot.
	 * 
	 * @return the messageSent boolean
	 */
	public static boolean isMessageSent() {
		return messageSent;
	}

	/**
	 * Sets whether the user has sent a message to the bot.
	 * 
	 * @param messageSent the messageSent to set (true or false)
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
	 * @return the user input
	 */
	public static String getUserInput() {
		return userInput;
	}

	/**
	 * Updates the GUI with the bot's answer.
	 * 
	 * @param newText the text containing the bot's response
	 */
	public static void daveAnswer(String newText) {
		value2.setText(newText);
	}

}