// Anna F, Ulee K, Iris T, Meira C
// CS 3 Summer 2022-2023
// Final Project: Language Chatbot
// 7/25/22
// Runs an intelligent chatbot that converses with a Spanish learner
// SpanishRunner

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.model.ModelUtil;
import opennlp.tools.util.TrainingParameters;

public class SpanishRunner {

	private static TreeMap<String, Double> wordsRanked;

	/**
	 * Creates a GUI and a categorizer model for the Spanish chatbot. Runs the
	 * chatbot.
	 *
	 * @param args console input
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException           if input or output exception occurs
	 * @throws InterruptedException  if timer is interrupted
	 */
	public static void main(String[] args)
			throws FileNotFoundException, IOException, InterruptedException {

		SpanishGui g = new SpanishGui();
		Dimension d = new Dimension(560, 900);
		g.panel.setPreferredSize(d);
		g.panel.setVisible(true);

		wordsRanked = new TreeMap<>();

		Scanner masterListReader = new Scanner(new File("masterwordlist"));
		wordsRanked = Difficulty.readFileData(masterListReader);

		System.out.println("Preparing Spanish data...");

		File categoryFile = new File("categoriesresponses");
		Scanner categoryReader = new Scanner(categoryFile);

		ArrayList<String> categories = getCategories(categoryReader);

		FileWriter outputStream = new FileWriter("categoriesranked");

		// skips header row

		/*
		 * The response data only needs to be sorted once to generate the file with
		 * difficulties of responses. It is not necessary to re-write the file every
		 * time a new bot is created.
		 */
		// sortResponseData(categoryReader, outputStream);

		outputStream.close();
		categoryReader.close();

		// // Trains the model (this only needs to be done once, so the code is
		// commented out)
		// DoccatModel trainModel = trainSpanishModel();
		// BufferedOutputStream modelOut = new BufferedOutputStream(
		// new FileOutputStream("spanishmodel"));
		// trainModel.serialize(modelOut);
		// modelOut.close();

		// loading the pre-trained model from a file
		File test = new File("spanishmodel");
		String modelFilePath = test.getAbsolutePath();

		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(
				new File("mastercategorizer"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory,
				StandardCharsets.UTF_8);
		DoccatModel model = new DoccatModel(new FileInputStream(modelFilePath));

		Spanish spanishBot = new Spanish(categories, model);
		spanishBot.runBot();

	}

	/**
	 * Reads the file of all categories and responses that was used to train the
	 * bot. Stores only the possible categories in an ArrayList of type String.
	 *
	 * @param categoryReader the scanner used to read the file
	 * @return an ArrayList containing all possible categories
	 */
	public static ArrayList<String> getCategories(Scanner categoryReader) {
		ArrayList<String> categories = new ArrayList<>();

		categoryReader.nextLine(); // skips header row
		while (categoryReader.hasNextLine()) {
			String line = categoryReader.nextLine();
			String[] phrases = line.split(";");

			// the category is the 0th index of the array of phrases (first term on each
			// line)
			categories.add(phrases[0]);
		}
		return categories;

	}

	/**
	 * Reads a file containing categories of conversation topics and possible
	 * responses. Ranks the "difficulty" of each response on a scale of 0-1
	 * depending on how common the phrase is, and writes the categories with
	 * difficulty data to a new file.
	 *
	 * @param categoryReader the scanner to read the file containing conversation
	 *                       topics and responses
	 * @param outputStream   the FileWriter used to write data to a new file
	 * @throws IOException if input or output exception occurs is interrupted
	 */
	public static void sortResponseData(Scanner categoryReader, FileWriter outputStream)
			throws IOException {
		categoryReader.nextLine(); // skips header row
		while (categoryReader.hasNextLine()) {
			String line = categoryReader.nextLine();
			String[] phrases = line.split(";");

			// writes category to file
			outputStream.write(String.valueOf(phrases[0] + ";"));

			for (int i = 1; i < phrases.length; i++) {
				outputStream.write(phrases[i]);
				outputStream.write("#"); // token to separate phrase from keyword
				outputStream.write(String.valueOf(rankPhrases(phrases[i])));
				outputStream.write(";"); // token to separate phrases + their values from other
				// phrases
			}
			outputStream.write("\n");

		}
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
		int countOfWords = 0;
		double difficultiesSum = 0;

		String[] tokens = phrase.split(" ");

		// String[] tokens = Spanish.tokenizeSentence(phrase);
		// String[] partsOfSpeech = Spanish.detectPOSTags(tokens);

		// Finds the lemma or each token (for example, "break" is the lemma of broke,
		// break, broken...)
		// String[] wordsInPhrase = Spanish.lemmatizeTokens(tokens, partsOfSpeech);

		for (String word : tokens) {

			if (wordsRanked.containsKey(word)) {
				difficultiesSum += wordsRanked.get(word);
				countOfWords++;

			}

		}
		if (countOfWords != 0) { // to avoid error when dividing by 0

			// finds the mean difficulty of all recognized tokens in the sentence
			phraseDifficulty = difficultiesSum / countOfWords;

		} else {
			phraseDifficulty = 0; // if no words in the phrase were recognized
		}

		return phraseDifficulty;

	}

	/**
	 * Trains the chatbot model using the traning data provided in a txt file.
	 *
	 * @return the trained model of the bot
	 * @throws IOException if input or output exception occurs
	 */
	public static DoccatModel trainSpanishModel() throws IOException {
		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(
				new File("mastercategorizer"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory,
				StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(
				new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("es", sampleStream, params, factory);
		return model;

	}

}