// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// A class that contains methods to determine and manipulate the difficulty of the chat conversation
// SpanishRunner

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import opennlp.tools.doccat.DoccatModel;

import java.util.Scanner;

public class Difficulty extends Spanish {

	/**
	 * A constructor for a Difficulty object.
	 *
	 * @param answerCategories the possible categories that the bot's answer can
	 *                         fall under
	 * @param model            the DoccatModel used to determine the category of the
	 *                         conversation
	 * @throws IOException           if input or output exception occurs
	 * @throws FileNotFoundException if file is not found
	 * @throws InterruptedException  if timer is interrupted
	 */
	public Difficulty(ArrayList<String> answerCategories, DoccatModel model)
			throws FileNotFoundException, IOException, InterruptedException {
		super(answerCategories, model);
	}

	/**
	 * Reads a master list of data and creates a TreeMap with each word and its
	 * corresponding difficulty ranking.
	 *
	 * @param scan the scanner used to read the file containing all Spanish words
	 *             and their difficulties.
	 * @return a TreeMap containing words and their difficulty rankings
	 */
	public static TreeMap<String, Double> readFileData(Scanner scan) {
		TreeMap<String, Double> wordsAndValues = new TreeMap<>();

		scan.nextLine();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] lineSep = line.split("#");

			String word = lineSep[0];
			double wordDifficulty = Double.valueOf(lineSep[1]);
			wordsAndValues.put(word, wordDifficulty);
		}

		return wordsAndValues;

	}

	/**
	 * Reads the list of recognized user input phrases that was used to train the
	 * Doccat categorizer model.
	 *
	 * @param scan the scanner used to read the file containing recognized user
	 *             input phrases
	 * @return a TreeMap containing categories as keys and possible phrases as the
	 *         values for a category
	 */
	public static TreeMap<String, String> readRecognizedInput(Scanner scan) {
		TreeMap<String, String> categoriesAndWords = new TreeMap<>();

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] lineSep = line.split(" ");
			String category = lineSep[0];

			String categoryWords[] = new String[lineSep.length - 1];
			for (int i = 1; i < lineSep.length; i++) {
				categoryWords[i - 1] = lineSep[i];
			}
			for (String word : categoryWords) {
				categoriesAndWords.put(category, word);
			}

		}

		return categoriesAndWords;

	}

	/**
	 * Updates the difficulty of the conversation when the user uses more advanced
	 * words that the bot does not have in its list of current vocabulary.
	 *
	 * @param lemmas       the String array of lemmatized tokens
	 * @param currentCateg the current conversation category
	 *
	 * @throws IOException if input or output exception occurs
	 */
	public static void updateDifficulty(String[] lemmas, String currentCateg) throws IOException {
		double difficultyFactor = 0;

		for (String s : lemmas) {
			// if the user-inputted word is not already in the TreeMap of recognized inputs
			if (!recognizedUserInput.values().contains(s)) {

				// adds the new user-inputted word to the list of current vocabulary in the
				// proper format
				recognizedUserInput.put(currentCateg, s);

				// determining the difficulty of the newly introduced word and updating the
				// difficulty factor
				// the difficulty of each new word is determined, then multiplied by 0.1 to
				// avoid increasing difficulty too fast
				difficultyFactor += rankPhrases(s) * 0.05;
			}
		}

		Spanish.setChatDifficulty(difficultyFactor);
	}

	/**
	 * Ranks the difficulty of each phrase in a possible response category by
	 * finding the mean of the pre-determined difficulties of each word. Updates the
	 * .txt file to have each word followed by its difficulty (scale of 0-1).
	 *
	 * @param phrase the phrase to have its difficulty analyzed
	 * @return a TreeMap containing the initial phrases in the file as keys and
	 *         their determined difficulties as values
	 * @throws IOException           if input or output exception occurs
	 * @throws FileNotFoundException if file is not found
	 */
	public static double rankPhrases(String phrase) throws FileNotFoundException, IOException {
		double phraseDifficulty = 0;

		// String[] tokens = Spanish.tokenizeSentence(phrase);
		String[] tokens = phrase.toLowerCase().split(" ");
		// String[] partsOfSpeech = Spanish.detectPOSTags(tokens);

		// Finds the lemma or each token (for example, "break" is the lemma of broke,
		// break, broken...)
		// String[] wordsInPhrase = Spanish.lemmatizeTokens(tokens, partsOfSpeech);

		for (String word : tokens) {
			word = word.toLowerCase();
			double difficultiesSum = 0;
			int countOfWords = 0;

			if (wordsRanked.containsKey(word)) {
				difficultiesSum += wordsRanked.get(word);
				countOfWords++;
			}

			if (countOfWords != 0) { // to avoid error when dividing by 0

				// finds the mean difficulty of all recognized tokens in the sentence
				phraseDifficulty += difficultiesSum / countOfWords;

			} else {
				phraseDifficulty = chatDifficulty; // stays the same if no words in the phrase were
													// recognized
			}
		}

		return phraseDifficulty;

	}

	/**
	 * Gets a a TreeMap of words and their difficulty rankings.
	 *
	 * @return a TreeMap of words and their difficulty rankings
	 */
	public static TreeMap<String, Double> getWordsRanked() {
		return wordsRanked;
	}

	/**
	 * Sets a TreeMap containing words and their difficulty rankings.
	 *
	 * @param wordsRanked the updated a TreeMap of words and their difficulty
	 *                    rankings
	 */
	public static void setWordsRanked(TreeMap<String, Double> wordsRanked) {
		Spanish.wordsRanked = wordsRanked;
	}

}