// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A window that contains buttons to direct the user to the Spanish and English chatbots
// IntroWindow

import javax.swing.*;

//import org.graalvm.compiler.nodes.java.PluginFactory_NewArrayNode;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Currently, this window is for display purposes only. The Spanish and English bots
 * can be more efficiently called from the SpanishRunner and EnglishRunner classes, respectively.
 */

public class IntroWindow implements ActionListener {

// Components
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel introPanel;
	private JPanel languagePanel;
	
//  private JPanel profileButtonPanel;
	private JLabel name;
	private JLabel greeting;
	private JLabel select;
	private JButton english;
	private JButton spanish;
//  private JButton profileButton;

	// Colors used in the UI
	private Color light = new Color(235, 230, 235);
	private Color lavender = new Color(188, 186, 234);
	private Color dark = new Color(30, 15, 65);

	/**
	 * Constructor that initializes components.
	 *
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if input or output exception occurs
	 * @throws InterruptedException  if timer is interrupted
	 */
	public IntroWindow() throws FileNotFoundException, IOException, InterruptedException {

		// Setting up the frame
		frame = new JFrame("Language Chatbot");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH); // full screen
		frame.getContentPane().setBackground(light); // light background color
		frame.setForeground(dark); // dark text color
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		// This JPanel is the "master" panel that contains all other JPanels
		mainPanel = new JPanel();

		// Panel to hold the JLabels
		introPanel = new JPanel();

		BoxLayout introboxlayout = new BoxLayout(introPanel, BoxLayout.Y_AXIS); // goes along the y
																				// axis
		introPanel.setLayout(introboxlayout);
		introPanel.setBackground(light); // light bg color
		introPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 30, 30)); // border between
																				// other panels

		// Name text
		name = new JLabel("Language Chatbot");
		name.setForeground(dark); // text color
		name.setFont(new Font("Century Gothic", Font.PLAIN, 80)); // font
		name.setAlignmentX(Component.CENTER_ALIGNMENT); // aligned center
		introPanel.add(name);

		// Greeting text
		greeting = new JLabel("Welcome!");
		greeting.setForeground(dark); // text color
		greeting.setFont(new Font("Century Gothic", Font.PLAIN, 40)); // font
		greeting.setAlignmentX(Component.CENTER_ALIGNMENT); // aligned center
		greeting.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // vertical border
																			// between other jlabels
		introPanel.add(greeting);

		// Text to select a language
		select = new JLabel("Select a language:");
		select.setForeground(dark); // text color
		select.setFont(new Font("Century Gothic", Font.PLAIN, 40)); // font
		select.setAlignmentX(Component.CENTER_ALIGNMENT); // aligned center
		introPanel.add(select);

		// Panel for the language buttons
		languagePanel = new JPanel();
		GridLayout gridlayout = new GridLayout(1, 3, 80, 20); // 3 x 1 grid w/ spacing
		languagePanel.setLayout(gridlayout);
		languagePanel.setBorder(BorderFactory.createEmptyBorder(30, 160, 110, 160)); // border
																						// between
																						// jpanels
		languagePanel.setBackground(light); // bg color
		languagePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // aligned to center

		// English button
		english = new JButton("English");
		english.setPreferredSize(new Dimension(200, 200)); // size for all jbuttons
		english.setFont(new Font("Century Gothic", Font.PLAIN, 40)); // font
		english.setBackground(lavender); // button color
		english.setBorder(BorderFactory.createRaisedSoftBevelBorder()); // border, looks raised
		english.addActionListener(this); // clicks have action
		languagePanel.add(english);

		// Spanish Button
		spanish = new JButton("Spanish");
		spanish.setPreferredSize(new Dimension(200, 200)); // size for all jbuttons
		spanish.setFont(new Font("Century Gothic", Font.PLAIN, 40)); // font
		spanish.setBackground(lavender); // button color
		spanish.setBorder(BorderFactory.createRaisedSoftBevelBorder()); // border, looks raised
		spanish.addActionListener(this); // clicks have action
		languagePanel.add(spanish);

		// Adds all components to the frame
		frame.setTitle("Language Chatbot"); // window title
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // can close
		frame.setResizable(false); // can't resize
		mainPanel.add(introPanel); // adds jlabel panels
		mainPanel.add(languagePanel, BorderLayout.SOUTH); // adds buttons to the bottom
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true); // frame is visible

	}

	/**
	 * Runs the different languages when buttons are pressed
	 *
	 * @param e user action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Runs English bot
		if (e.getSource().equals(english)) {
//			try {
//				frame.dispose();
//				// Calls English runner
//				// EnglishRunner.runEnglish();
//			} catch (IOException | InterruptedException e1) {
//				e1.printStackTrace();
//			}

			// Runs Spanish bot
		}
		if (e.getSource().equals(spanish)) {
//			// Calls Spanish runner
//			// Executors.newSingleThreadScheduledExecutor().schedule(() -> System.exit(0),
//			// 1, TimeUnit.SECONDS);
//
//			try {
//				frame.dispose();
//				SpanishRunner.runSpanish();
//			} catch (IOException | InterruptedException e1) {
//				e1.printStackTrace();
//			}
//		}

		}

	}

	/**
	 * Runs the main intro window.
	 *
	 * @param args console input
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if input or output exception occurs
	 * @throws InterruptedException  if timer is interrupted
	 */
	public static void main(String[] args)
			throws FileNotFoundException, IOException, InterruptedException {

		// Creates a new IntroWindow
		IntroWindow w = new IntroWindow();

	}

}