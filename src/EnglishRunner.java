// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// Runs an intelligent chatbot that converses with an English learner
// EnglishRunner

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class EnglishRunner {

	/**
	 * Runs the main English bot.
	 * 
	 * @param args console input
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException if input or output exception occurs
	 * @throws InterruptedException if timer was interrupted
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		int DifficultyLevel = 1;
		English.difficultyLevel(DifficultyLevel);
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("greetings");
		categories.add("endconvo");
		
		// Creates the main English GUI
		EnglishGui g = new EnglishGui();
		g.panel.requestFocus();
		English englishBot = new English(categories);
		englishBot.runBot();
		English.createAnswers();
		
	}

}